package com.tinyhuman.tinyhumanapi.album.controller;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/babies")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{babyId}/albums")
    public List<AlbumResponse> uploadAlbums(@PathVariable("babyId") Long babyId,
                                         @RequestPart(required=false) @Size(max = 20, message="사진 및 동영상은 최대 업로드 개수는 20개입니다.") List<MultipartFile> files) {
        return albumService.uploadAlbums(babyId, files);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{babyId}/albums")
    public List<AlbumResponse> getAlbums(@PathVariable("babyId") Long babyId) {
        return albumService.getAlbumsByBaby(babyId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{babyId}/albums/{albumId}")
    public AlbumResponse getAlbums(@PathVariable("babyId") Long babyId, @PathVariable("albumId") Long albumId) {
        return albumService.findById(albumId, babyId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{babyId}/albums")
    public void deleteAlbums(@PathVariable("babyId") Long babyId, @RequestBody AlbumDelete albums) {
        albumService.delete(albums);
    }


}
