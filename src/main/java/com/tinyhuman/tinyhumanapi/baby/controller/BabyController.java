package com.tinyhuman.tinyhumanapi.baby.controller;

import com.tinyhuman.tinyhumanapi.baby.controller.dto.*;
import com.tinyhuman.tinyhumanapi.baby.controller.port.BabyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/babies")
@Tag(name = "BabyController", description = "아기 생성을 위한 컨트롤러입니다.")
public class BabyController {

    private final BabyService babyService;

    @Builder
    public BabyController(BabyService babyService) {
        this.babyService = babyService;
    }

    @Operation(summary = "아기 등록 API", responses = {
            @ApiResponse(responseCode = "201", description = "아기 등록 성공")})
    @PostMapping
    public ResponseEntity<BabyPreSignedUrlResponse> register(@RequestBody @Valid BabyCreate babyCreate) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(babyService.register(babyCreate));
    }

    @Operation(summary = "나의 아기 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "나에게 등록된 아기 조회 성공")})
    @GetMapping("my")
    public ResponseEntity<List<BabyResponse>> getMyBabies() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(babyService.getMyBabies());
    }


    @Operation(summary = "아기 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "아기 삭제 성공")})
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        babyService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @Operation(summary = "아기 정보 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "아기 정보 수정 성공")})
    @PatchMapping("{id}")
    public ResponseEntity<BabyResponse> update(@PathVariable("id") Long id, @RequestBody @Valid BabyUpdate babyUpdate) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(babyService.update(id, babyUpdate));
    }

    @Operation(summary = "아기 프로필 사진 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "아기 이미지 수정 성공")})
    @PatchMapping("{id}/image")
    public ResponseEntity<BabyPreSignedUrlResponse> updateProfileImage(@PathVariable("id") Long id, @RequestBody @Valid BabyImageUpdate babyImageUpdate) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(babyService.updateProfileImage(id, babyImageUpdate.fileName()));
    }

}
