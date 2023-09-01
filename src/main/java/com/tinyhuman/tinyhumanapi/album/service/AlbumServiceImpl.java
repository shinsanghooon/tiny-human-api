package com.tinyhuman.tinyhumanapi.album.service;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;
import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.common.enums.ContentType;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.common.exception.UnauthorizedAccessException;
import com.tinyhuman.tinyhumanapi.integration.aws.S3Util;
import com.tinyhuman.tinyhumanapi.integration.service.ImageService;
import com.tinyhuman.tinyhumanapi.integration.util.ImageUtil;
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

    @Builder
    public AlbumServiceImpl(AlbumRepository albumRepository, ImageService imageService, UserBabyRelationRepository userBabyRelationRepository, AuthService authService) {
        this.albumRepository = albumRepository;
        this.imageService = imageService;
        this.userBabyRelationRepository = userBabyRelationRepository;
        this.authService = authService;
    }

    private final String ALBUM_UPLOAD_PATH = "images/babyId/album/";

    @Override
    public AlbumResponse findByIdAndBabyId(Long albumId, Long babyId) {
        Album album = albumRepository.findByIdAndBabyId(albumId, babyId);
        String preSignedUrl = getPreSignedUrlFromKeyName(album);
        return AlbumResponse.fromModel(album, preSignedUrl);
    }

    @Override
    public List<AlbumResponse> getAlbumsByBaby(Long babyId) {
        return albumRepository.findByBabyId(babyId).stream().map(album -> {
            String preSignedUrl = getPreSignedUrlFromKeyName(album);
            return AlbumResponse.fromModel(album, preSignedUrl);
        }).toList();
    }

    private String getPreSignedUrlFromKeyName(Album album) {
        String keyName = album.keyName();
        String[] split = keyName.split("/");
        String fileName = split[split.length - 1];
        String mimeType = ImageUtil.guessMimeType(fileName);

        return imageService.getPreSignedUrlForUpload(keyName, mimeType);
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
            String keyName = S3Util.addBabyIdToImagePath(ALBUM_UPLOAD_PATH, babyId, fileName);
            String mimeType = ImageUtil.guessMimeType(fileName);

            String preSignedUrl = imageService.getPreSignedUrlForUpload(keyName, mimeType);
            ContentType contentType = ImageUtil.getContentType(mimeType);

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
