package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.*;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.NotSupportedContentTypeException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
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
    public List<Album> updateOriginalDate(Long babyId, AlbumDateUpdate albumDateUpdate) {

        Long userId = authService.getUserOutOfSecurityContextHolder().id();
        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(UserBabyMappingId.builder()
                .userId(userId)
                .babyId(babyId)
                .build())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(UserBabyRelation) - userId:{},babyId{}",  userId, babyId);
                    return new ResourceNotFoundException("UserBabyRelation", userId + " " + babyId);
                });

        if (!userBabyRelation.isParent()) {
            log.error("UnauthorizedAccessException(Album) - userId:{},babyId{}",  userId, babyId);
            throw new UnauthorizedAccessException("Baby", babyId);
        }

        List<Album> updatedAlbums = albumRepository.findAllByIds(albumDateUpdate.ids()).stream()
                .map(album -> album.updateOriginalDate(albumDateUpdate.originalCreatedAt()))
                .toList();
        return albumRepository.saveAll(updatedAlbums);
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
            log.error("IllegalArgumentException - files count:{}", files.size());
            throw new IllegalArgumentException("사진 및 동영상은 최대 업로드 개수는 20개입니다.");
        }

        if (isNotImageOrVideo(files)) {
            throw new NotSupportedContentTypeException("지원하지 않는 파일 형식입니다. 다시 한 번 확인해주세요.");
        }

        User user = authService.getUserOutOfSecurityContextHolder();
        UserBabyRelation userBabyRelation = userBabyRelationRepository.findById(UserBabyMappingId.builder()
                        .babyId(babyId)
                        .userId(user.id())
                        .build())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(UserBabyRelation) - userId:{},babyId{}",  user.id(), babyId);
                    return new ResourceNotFoundException("UserBabyRelation", user.id() + " " + babyId);
                });

        if (isUserOwnsBaby(userBabyRelation)) {
            log.error("UnauthorizedAccessException(Album) - userId:{},babyId{}",  user.id(), babyId);
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

    private boolean isNotImageOrVideo(List<AlbumCreate> files) {
        for (AlbumCreate file : files) {
            String mimeType = FileUtils.guessMimeType(file.fileName());
            ContentType contentType = FileUtils.getContentType(mimeType);

            boolean isNotPhotoOrVideo = contentType != ContentType.PHOTO && contentType != ContentType.VIDEO;
            if (isNotPhotoOrVideo) {
                log.error("NotSupportedContentTypeException - file:{},contentType:{}", file.fileName(), contentType);
                return true;
            }
        }
        return false;
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
