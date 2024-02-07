package com.tinyhuman.tinyhumanapi.checklist.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistService;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistDetailCreate;
import com.tinyhuman.tinyhumanapi.checklist.controller.port.dto.ChecklistResponse;
import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
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
                .map(c -> ChecklistDetail.builder()
                        .contents(c.contents())
                        .reason(c.contents())
                        .checklistId(savedChecklist.id()).build())
                .toList();

        List<ChecklistDetail> savedChecklistDetail = checklistDetailRepository.saveAll(checklistDetails, savedChecklist);
        Checklist checklist = savedChecklist.addChecklistDetail(savedChecklistDetail);

        checklistRepository.save(checklist);

        return checklist.toResponseModel();
    }

    public List<ChecklistResponse> getChecklist() {
        User user = authService.getUserOutOfSecurityContextHolder();
        return checklistRepository.findByUserId(user.id()).stream().map(Checklist::toResponseModel).toList();
    }

    @Override
    public ChecklistResponse update(ChecklistCreate checklistCreate) {
        User user = authService.getUserOutOfSecurityContextHolder();

        Checklist originalChecklist = checklistRepository.findByIdAndUserId(checklistCreate.id(), user.id())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - Checklist Id:{}", checklistCreate.id());
                    return new ResourceNotFoundException("Checklist", checklistCreate.id());
                });

        deleteChecklistDetails(checklistCreate, originalChecklist);

        Checklist savedChecklist = checklistRepository.save(Checklist.fromCreate(checklistCreate, user));

        List<ChecklistDetail> checklistDetails = checklistCreate.checklistDetailCreate().stream()
                .map(c -> ChecklistDetail.builder()
                        .id(c.id())
                        .contents(c.contents())
                        .reason(c.contents())
                        .isChecked(c.isChecked())
                        .checklistId(savedChecklist.id()).build())
                .toList();

        List<ChecklistDetail> savedChecklistDetail = checklistDetailRepository.saveAll(checklistDetails, savedChecklist);
        Checklist checklist = savedChecklist.addChecklistDetail(savedChecklistDetail);

        checklistRepository.save(checklist);

        return checklist.toResponseModel();
    }

    private void deleteChecklistDetails(ChecklistCreate checklistCreate, Checklist originalChecklist) {
        List<Long> originalChecklistDetailIds = originalChecklist.checklistDetails().stream()
                .map(ChecklistDetail::id)
                .toList();

        List<Long> updateChecklistDetailIds = checklistCreate.checklistDetailCreate().stream()
                .map(ChecklistDetailCreate::id)
                .toList();

        // originalChecklistDetailIds에만 속하고 updateChecklistDetailIds에는 속하지 않는 요소들을 추출
        List<Long> exclusiveOriginalIds = originalChecklistDetailIds.stream()
                .filter(id -> !updateChecklistDetailIds.contains(id))
                .toList();

        checklistDetailRepository.deleteAllById(exclusiveOriginalIds);
    }

    @Override
    public void delete(Long id) {
        User user = authService.getUserOutOfSecurityContextHolder();
        Checklist checklist = checklistRepository.findByIdAndUserId(id, user.id())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - Checklist Id:{}", id);
                    return new ResourceNotFoundException("Checklist", id);
                });

        Checklist deletedChecklist = checklist.delete();

        checklistDetailRepository.deleteAllById(checklist.checklistDetails().stream().map(ChecklistDetail::id).toList());
        checklistRepository.save(deletedChecklist);
    }

}
