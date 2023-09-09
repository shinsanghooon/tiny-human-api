package com.tinyhuman.tinyhumanapi.album.controller;

import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumCreate;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumDelete;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumResponse;
import com.tinyhuman.tinyhumanapi.album.controller.dto.AlbumUploadResponse;
import com.tinyhuman.tinyhumanapi.album.controller.port.AlbumService;
import com.tinyhuman.tinyhumanapi.common.utils.CursorRequest;
import com.tinyhuman.tinyhumanapi.common.utils.PageCursor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/babies")
@Tag(name = "AlbumController", description = "앨범에 대한 작업을 위한 컨트롤러입니다.")
public class AlbumController {

    private final AlbumService albumService;

    @Builder
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(summary = "앨범 추가 API", responses = {
            @ApiResponse(responseCode = "201", description = "앨범 업로드 성공")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{babyId}/albums")
    public ResponseEntity<List<AlbumUploadResponse>> uploadAlbums(@PathVariable("babyId") Long babyId,
                                                                  @RequestBody List<AlbumCreate> files) {
        return ResponseEntity.
                status(HttpStatus.CREATED)
                .body(albumService.uploadAlbums(babyId, files));
    }

    @Operation(summary = "앨범 단건 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "앨범 단건 상세 조회")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{babyId}/albums/{albumId}")
    public AlbumResponse getAlbum(@PathVariable("babyId") Long babyId, @PathVariable("albumId") Long albumId) {
        return albumService.findByIdAndBabyId(albumId, babyId);
    }

    @Operation(summary = "앨범 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "아기에 대한 전체 앨범 조회")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{babyId}/albums")
    public PageCursor<AlbumResponse> getAllAlbums(@PathVariable("babyId") Long babyId, @RequestParam("order") String order, @RequestBody CursorRequest cursorRequest) {
        return albumService.getAlbumsByBaby(babyId, cursorRequest);
    }

    @Operation(summary = "앨범 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "앨범 삭제")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{babyId}/albums")
    public void deleteAlbums(@PathVariable("babyId") Long babyId, @RequestBody AlbumDelete albums) {
        albumService.delete(albums);
    }


}
