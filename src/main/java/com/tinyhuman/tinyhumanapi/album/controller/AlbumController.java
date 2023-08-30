package com.tinyhuman.tinyhumanapi.album.controller;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/babies")
@RequiredArgsConstructor
@Tag(name = "AlbumController", description = "앨범에 대한 작업을 위한 컨트롤러입니다.")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(summary = "앨범 추가 API", responses = {
            @ApiResponse(responseCode = "201", description = "앨범 업로드 성공")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{babyId}/albums")
    public List<AlbumResponse> uploadAlbums(@PathVariable("babyId") Long babyId,
                                         @RequestPart(required=false) @Size(max = 20, message="사진 및 동영상은 최대 업로드 개수는 20개입니다.") List<MultipartFile> files) {
        return albumService.uploadAlbums(babyId, files);
    }

    @Operation(summary = "앨범 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "아기에 대한 전체 앨범 조회")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{babyId}/albums")
    public List<AlbumResponse> getAlbums(@PathVariable("babyId") Long babyId) {
        return albumService.getAlbumsByBaby(babyId);
    }

    @Operation(summary = "앨범 단건 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "앨범 단건 상세 조회")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{babyId}/albums/{albumId}")
    public AlbumResponse getAlbum(@PathVariable("babyId") Long babyId, @PathVariable("albumId") Long albumId) {
        return albumService.findByIdAndBabyId(albumId, babyId);
    }

    @Operation(summary = "앨범 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "앨범 삭제")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{babyId}/albums")
    public void deleteAlbums(@PathVariable("babyId") Long babyId, @RequestBody AlbumDelete albums) {
        albumService.delete(albums);
    }


}
