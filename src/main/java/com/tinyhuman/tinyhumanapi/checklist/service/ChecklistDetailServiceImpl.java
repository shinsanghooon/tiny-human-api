package com.tinyhuman.tinyhumanapi.checklist.service;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistDetailService;
import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistRepository;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChecklistDetailServiceImpl implements ChecklistDetailService {

    private final ChecklistDetailRepository checklistDetailRepository;

    private final ChecklistRepository checklistRepository;

    @Override
    public ChecklistDetail checkUpdate(Long checklistId, Long checklistDetailId) {

        ChecklistDetail checklistDetail = checklistDetailRepository.findById(checklistDetailId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - ChecklistDetail Id:{}", checklistDetailId);
                    return new ResourceNotFoundException("ChecklistDetail", checklistDetailId);
                });

        Checklist checklist = checklistRepository.findById(checklistId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - Checklist Id:{}", checklistId);
                    return new ResourceNotFoundException("Checklist", checklistId);
                });

        ChecklistDetail updatedCheck = checklistDetail.updateCheck();
        return checklistDetailRepository.save(updatedCheck, checklist);
    }
}
