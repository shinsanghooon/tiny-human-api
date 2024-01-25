package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChecklistDetailRepositoryImpl implements ChecklistDetailRepository {

    private final ChecklistDetailJpaRepository checklistDetailJpaRepository;

    @Override
    public ChecklistDetail save(ChecklistDetail checklistDetail, Checklist checklist) {
        return checklistDetailJpaRepository.save(ChecklistDetailEntity.fromModel(checklistDetail, checklist)).toModel();
    }

}
