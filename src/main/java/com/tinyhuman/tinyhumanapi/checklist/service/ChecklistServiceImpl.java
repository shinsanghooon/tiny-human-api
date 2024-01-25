package com.tinyhuman.tinyhumanapi.checklist.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;
import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistRepository;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {

    private final AuthService authService;

    private final ChecklistRepository checklistRepository;

    private final ChecklistDetailRepository checklistDetailRepository;

    @Builder
    public ChecklistServiceImpl(AuthService authService, ChecklistRepository checklistRepository, ChecklistDetailRepository checklistDetailRepository) {
        this.authService = authService;
        this.checklistRepository = checklistRepository;
        this.checklistDetailRepository = checklistDetailRepository;
    }

    @Override
    public ChecklistResponse register(ChecklistCreate checklistCreate) {
        User user = authService.getUserOutOfSecurityContextHolder();
        Checklist savedChecklist = checklistRepository.save(Checklist.fromCreate(checklistCreate, user));

        List<ChecklistDetail> checklistDetails = checklistCreate.checklistDetailCreate().stream()
                .map(ChecklistDetail::fromCreate)
                .toList();
        checklistDetailRepository.saveAll(checklistDetails, savedChecklist);

        savedChecklist.addChecklistDetail(checklistDetails);

        return checklistRepository.save(savedChecklist).toModel();
    }

    public List<ChecklistResponse> getChecklist() {
        User user = authService.getUserOutOfSecurityContextHolder();
        return checklistRepository.findByUserId(user.id()).stream().map(Checklist::toModel).toList();
    }

}
