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

    @Override
    public PageCursor<AlbumResponse> getAlbumsByBaby(Long babyId, CursorRequest cursorRequest) {
        List<AlbumResponse> albumResponses = albumRepository.findByBabyId(babyId, cursorRequest).stream()
                .map(AlbumResponse::fromModel)
                .toList();

        long nextKey = getNextKey(albumResponses);
        return new PageCursor<>(cursorRequest.next(nextKey), albumResponses);
    }

    private static long getNextKey(List<AlbumResponse> albumResponses) {
        return albumResponses.stream()
                .mapToLong(AlbumResponse::id)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }

    @Override
    public List<AlbumUploadResponse> uploadAlbums(Long babyId, List<AlbumCreate> files) {

        if (isExceededMaxUploadCount(files)) {
            throw new IllegalArgumentException("사진 및 동영상은 최대 업로드 개수는 20개입니다.");
        }

        User user = authService.getUserOutOfSecurityContextHolder();
        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(UserBabyMappingId.builder()
                        .babyId(babyId)
                        .userId(user.id())
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("UserBabyRelation", user.id() + "/" + babyId));

        if (isUserOwnsBaby(userBabyRelation)) {
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
                    .originalCreatedAt(albumCreate.originalCreatedAt())
                    .gpsLat(albumCreate.gpsLat())
                    .gpsLon(albumCreate.gptLon())
                    .babyId(babyId)
                    .build();

            albums.add(album);
        }

        return albumRepository.saveAll(albums).stream()
                .map(this::getAlbumUploadResponse)
                .toList();
    }

    private boolean isUserOwnsBaby(UserBabyRelation userBabyRelation) {
        return !userBabyRelation.isParent();
    }

    private boolean isExceededMaxUploadCount(List<AlbumCreate> files) {
        return files.size() > 20;
    }

    private AlbumUploadResponse getAlbumUploadResponse(Album album) {
        String keyName = album.keyName();
        String fileName = FileUtils.extractFileNameFromPath(keyName);
        String mimeType = FileUtils.guessMimeType(fileName);
        String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);
        return AlbumUploadResponse.fromModel(album).with(fileName, preSignedUrl);
    }

    @Override
    public List<Album> delete(AlbumDelete albumDelete) {
        List<Album> albums = albumRepository.findAllByIds(albumDelete.ids());
        List<Album> newAlbums = albums.stream().map(Album::deleteAlbum).toList();
        return albumRepository.saveAll(newAlbums);
    }
}
