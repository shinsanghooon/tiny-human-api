package com.tinyhuman.tinyhumanapi.album.mock;

import com.tinyhuman.tinyhumanapi.album.domain.Album;
import com.tinyhuman.tinyhumanapi.album.service.port.AlbumRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakeAlbumRepository implements AlbumRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0L);

    private final List<Album> data = new ArrayList<>();

    @Override
    public List<Album> saveAll(List<Album> albums) {
        List<Album> newAlbums = new ArrayList<>();

        for (Album album : albums) {
            data.removeIf(s -> Objects.equals(s.id(), album.id()));

            Album newAlbum = Album.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .babyId(album.babyId())
                    .contentType(album.contentType())
                    .originalS3Url(album.originalS3Url())
                    .isDeleted(album.isDeleted())
                    .build();

            data.add(newAlbum);
            newAlbums.add(newAlbum);
        }

        return newAlbums;
    }
}
