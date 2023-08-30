package com.tinyhuman.tinyhumanapi.baby.controller;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyUpdate;
import com.tinyhuman.tinyhumanapi.baby.service.BabyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/babies")
@RequiredArgsConstructor
@Tag(name = "BabyController", description = "아기 생성을 위한 컨트롤러입니다.")
public class BabyController {

    private final BabyServiceImpl babyService;
    @Operation(summary = "아기 등록 API", responses = {
            @ApiResponse(responseCode = "201", description = "아기 등록 성공")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BabyResponse register(@RequestPart @Valid BabyCreate babyCreate,
                                 @RequestPart(required = false) MultipartFile file) {
        return babyService.register(babyCreate, file);
    }

    @Operation(summary = "나의 아기 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "나에게 등록된 아기 조회 성공")})
    @GetMapping("my")
    @ResponseStatus(HttpStatus.OK)
    public List<BabyResponse> getMyBabies() {
        return babyService.getMyBabies();
    }


    @Operation(summary = "아기 삭제 API", responses = {
            @ApiResponse(responseCode = "204", description = "아기 삭제 성공")})
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        babyService.delete(id);
    }


    @Operation(summary = "아기 정보 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "아기 정보 수정 성공")})
    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BabyResponse update(@PathVariable("id") Long id, @RequestPart @Valid BabyUpdate babyUpdate,
                               @RequestPart(value="file", required = false) MultipartFile file) {
        return babyService.update(id, babyUpdate, file);
    }

}