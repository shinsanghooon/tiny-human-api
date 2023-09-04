package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.dto.CursorAlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;
import com.tinyhuman.tinyhumanapi.common.utils.FileUtils;
import com.tinyhuman.tinyhumanapi.common.utils.ImageUtils;
import com.tinyhuman.tinyhumanapi.integration.controller.dto.LastEvaluatedKey;
import com.tinyhuman.tinyhumanapi.integration.domain.ExifInfo;
import com.tinyhuman.tinyhumanapi.integration.service.ExifDynamoDBRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.*;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    private final ImageService imageService;

    private final UserBabyRelationRepository userBabyRelationRepository;

    private final AuthService authService;

    private final ClockHolder clockHolder;

    private final ExifDynamoDBRepository exifDynamoDBRepository;

    @Builder
    public AlbumServiceImpl(AlbumRepository albumRepository, ImageService imageService, UserBabyRelationRepository userBabyRelationRepository,
                            AuthService authService, ClockHolder clockHolder, ExifDynamoDBRepository exifDynamoDBRepository) {
        this.albumRepository = albumRepository;
        this.imageService = imageService;
        this.userBabyRelationRepository = userBabyRelationRepository;
        this.authService = authService;
        this.clockHolder = clockHolder;
        this.exifDynamoDBRepository = exifDynamoDBRepository;
    }

    private final String ALBUM_UPLOAD_PATH = "baby/babyId/album/";

    @Override
    public AlbumResponse findByIdAndBabyId(Long albumId, Long babyId) {
        Album album = albumRepository.findByIdAndBabyId(albumId, babyId);
        String preSignedUrl = imageService.getPreSignedUrlForRead(album.keyName(), 1000);
        return AlbumResponse.fromModel(album, preSignedUrl);
    }

    /**
     * 아기에 대한 전체 앨범 조회
     * 생성 순으로 정렬 할 수 있도록 하기 위해 DynamoDB에 저장된 original_create_at 값을 불러온다.
     *
     * @param babyId
     * @return
     */
    @Override
    public List<AlbumResponse> getAlbumsByBaby(Long babyId) {
        return albumRepository.findByBabyId(babyId).stream().map(album -> {
            String preSignedUrl = imageService.getPreSignedUrlForRead(album.keyName(), 300);
            return AlbumResponse.fromModel(album, preSignedUrl);
        }).toList();
    }

    @Override
    public CursorAlbumResponse getAlbumsByBaby(Long babyId, LastEvaluatedKey lastEvaluatedKey) {

        Iterator<Page<ExifInfo>> exifPage = exifDynamoDBRepository.getExif(babyId, lastEvaluatedKey);
        Page<ExifInfo> exif = exifPage.next();

        List<ExifInfo> items = exif.items();

        Map<String, String> keyNameMap = new HashMap<>();
        for (ExifInfo item : items) {
            keyNameMap.put(item.getKeyName(), item.getOriginalCreatedAt());
        }
        Set<String> keyNameSet = keyNameMap.keySet();

        LastEvaluatedKey lastKey = LastEvaluatedKey.builder()
                .babyId(exif.lastEvaluatedKey().get("baby_id").s())
                .originalCreatedAt(exif.lastEvaluatedKey().get("original_created_at").s())
                .build();

        List<AlbumResponse> albumResponses = albumRepository.findByBabyIdAndKeyNameIn(babyId, keyNameSet).stream()
                .map(album -> {
                    String preSignedUrl = imageService.getPreSignedUrlForRead(album.keyName(), 300);
                    String originalCreatedAt = keyNameMap.get(album.keyName());
                    return AlbumResponse.fromModel(album).with(preSignedUrl, originalCreatedAt);
                }).toList();

        return CursorAlbumResponse.builder()
                .albumResponses(albumResponses)
                .lastEvaluatedKey(lastKey)
                .build();

    }

    @Override
    public List<AlbumResponse> uploadAlbums(Long babyId, List<AlbumCreate> files) {

        User user = authService.getUserOutOfSecurityContextHolder();
        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(UserBabyMappingId.builder()
                        .babyId(babyId)
                        .userId(user.id())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("UserBabyRelation", user.id() + "/" + babyId));

        if (!userBabyRelation.isParent()) {
            throw new UnauthorizedAccessException("Album", babyId);
        }

        List<Album> albums = new ArrayList<>();

        for (AlbumCreate albumCreate : files) {
            String fileName = albumCreate.fileName();
            String mimeType = ImageUtils.guessMimeType(fileName);

            String fileNameWithEpoch = FileUtils.generateFileNameWithEpochTime(fileName, clockHolder);
            String keyName = FileUtils.addBabyIdToImagePath(ALBUM_UPLOAD_PATH, babyId, fileNameWithEpoch);

            String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);
            ContentType contentType = ImageUtils.getContentType(mimeType);

            Album album = Album.builder()
                    .contentType(contentType)
                    .keyName(keyName)
                    .preSignedUrl(preSignedUrl)
                    .babyId(babyId)
                    .build();

            albums.add(album);
        }

        return albumRepository.saveAll(albums).stream()
                .map(AlbumResponse::fromModel)
                .toList();
    }

    @Override
    public List<Album> delete(AlbumDelete albumDelete) {
        List<Album> albums = albumRepository.findAllByIds(albumDelete.ids());
        List<Album> newAlbums = albums.stream().map(Album::deleteAlbum).toList();
        return albumRepository.saveAll(newAlbums);
    }


}
