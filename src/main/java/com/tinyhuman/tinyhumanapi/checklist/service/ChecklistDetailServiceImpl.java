package com.tinyhuman.tinyhumanapi.checklist.service;

import com.tinyhuman.tinyhumanapi.checklist.controller.port.ChecklistDetailService;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
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

    @Override
    public ChecklistDetail checkUpdate(Long checklistId, Long id) {

        ChecklistDetail checklistDetail = checklistDetailRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - ChecklistDetail Id:{}", id);
                    return new ResourceNotFoundException("ChecklistDetail", id);
                });
        ChecklistDetail updatedCheck = checklistDetail.updateCheck();

        return checklistDetailRepository.save(updatedCheck);
    }
}
