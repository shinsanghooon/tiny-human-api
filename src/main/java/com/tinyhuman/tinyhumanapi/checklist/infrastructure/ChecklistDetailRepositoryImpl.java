package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChecklistDetailRepositoryImpl implements ChecklistDetailRepository {

    private final ChecklistDetailJpaRepository checklistDetailJpaRepository;

    @Override
    public ChecklistDetail save(ChecklistDetail checklistDetail, Checklist checklist) {
        return checklistDetailJpaRepository.save(ChecklistDetailEntity.fromModel(checklistDetail, checklist)).toModel();
    }

    @Override
    public List<ChecklistDetail> saveAll(List<ChecklistDetail> checklistDetails, Checklist checklist) {
        List<ChecklistDetailEntity> checklistDetailEntities = checklistDetailJpaRepository.saveAll(checklistDetails.stream()
                .map(checklistDetail -> ChecklistDetailEntity.fromModel(checklistDetail, checklist))
                .toList());

        return checklistDetailEntities.stream().map(ChecklistDetailEntity::toModel).toList();
    }

}
