package com.tinyhuman.tinyhumanapi.baby.controller;

import com.tinyhuman.tinyhumanapi.baby.domain.BabyCreate;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyResponse;
import com.tinyhuman.tinyhumanapi.baby.domain.BabyUpdate;
import com.tinyhuman.tinyhumanapi.baby.service.BabyServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/babies")
@RequiredArgsConstructor
public class BabyCreateController {

    private final BabyServiceImpl babyService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BabyResponse register(@RequestPart @Valid BabyCreate babyCreate,
                                 @RequestPart(required = false) MultipartFile file) {
        return babyService.register(babyCreate, file);
    }

    @GetMapping("my")
    @ResponseStatus(HttpStatus.OK)
    public List<BabyResponse> getMyBabies() {
        return babyService.getMyBabies();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        babyService.delete(id);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public BabyResponse update(@PathVariable("id") Long id, @RequestPart @Valid BabyUpdate babyUpdate,
                               @RequestPart(value="file", required = false) MultipartFile file) {
        return babyService.update(id, babyUpdate, file);
    }

}
