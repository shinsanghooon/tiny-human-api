package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumUploadResponse;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.FileUtils;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;
import com.tinyhuman.tinyhumanapi.integration.service.port.ImageService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserBabyRelation;
import com.tinyhuman.tinyhumanapi.user.infrastructure.UserBabyMappingId;
import com.tinyhuman.tinyhumanapi.user.service.port.UserBabyRelationRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;

    private final ImageService imageService;

    private final UserBabyRelationRepository userBabyRelationRepository;

    private final AuthService authService;

    private final UuidHolder uuidHolder;


    @Builder
    public AlbumServiceImpl(AlbumRepository albumRepository, ImageService imageService, UserBabyRelationRepository userBabyRelationRepository,
                            AuthService authService, UuidHolder uuidHolder) {
        this.albumRepository = albumRepository;
        this.imageService = imageService;
        this.userBabyRelationRepository = userBabyRelationRepository;
        this.authService = authService;
        this.uuidHolder = uuidHolder;
    }

    private final String ALBUM_UPLOAD_PATH = "baby/babyId/album/";

    @Override
    public AlbumResponse findByIdAndBabyId(Long albumId, Long babyId) {
        Album album = albumRepository.findByIdAndBabyId(albumId, babyId);
        return AlbumResponse.fromModel(album);
    }

    /**
     * 아기에 대한 전체 앨범 조회
     * 생성 순으로 정렬 할 수 있도록 하기 위해 DynamoDB에 저장된 original_create_at 값을 불러온다.
     *
     * @param babyId
     * @return
     */
    @Override
    public PageCursor<AlbumResponse> getAlbumsByBaby(Long babyId, CursorRequest cursorRequest) {
        List<AlbumResponse> albumResponses = findByBabyIdWithCursor(babyId, cursorRequest);

        long nextKey = getNextKey(albumResponses);
        return new PageCursor<>(cursorRequest.next(nextKey), albumResponses);
    }

    private List<AlbumResponse> findByBabyIdWithCursor(Long babyId, CursorRequest cursorRequest) {
        return albumRepository.findByBabyId(babyId, cursorRequest).stream()
                .map(AlbumResponse::fromModel)
                .toList();
    }

    private static long getNextKey(List<AlbumResponse> albumResponses) {
        return albumResponses.stream()
                .mapToLong(AlbumResponse::id)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }


    @Override
    public List<AlbumUploadResponse> uploadAlbums(Long babyId, List<AlbumCreate> files) {

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
            String mimeType = FileUtils.guessMimeType(fileName);
            ContentType contentType = FileUtils.getContentType(mimeType);


            String fileNameWithEpoch = FileUtils.generateFileNameWithUUID(fileName, uuidHolder.random());
            String keyName = FileUtils.addBabyIdToImagePath(ALBUM_UPLOAD_PATH, babyId, fileNameWithEpoch);

            Album album = Album.builder()
                    .contentType(contentType)
                    .keyName(keyName)
                    .babyId(babyId)
                    .build();

            albums.add(album);
        }

        return albumRepository.saveAll(albums).stream()
                .map(this::getAlbumUploadResponse)
                .toList();
    }

    private AlbumUploadResponse getAlbumUploadResponse(Album album) {
        String keyName = album.keyName();
        String fileName = FileUtils.extractFileNameFromPath(keyName);
        String mimeType = FileUtils.guessMimeType(fileName);
        String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);
        return AlbumUploadResponse.fromModel(album).with(fileName, preSignedUrl, null);
    }

    @Override
    public List<Album> delete(AlbumDelete albumDelete) {
        List<Album> albums = albumRepository.findAllByIds(albumDelete.ids());
        List<Album> newAlbums = albums.stream().map(Album::deleteAlbum).toList();
        return albumRepository.saveAll(newAlbums);
    }


}
