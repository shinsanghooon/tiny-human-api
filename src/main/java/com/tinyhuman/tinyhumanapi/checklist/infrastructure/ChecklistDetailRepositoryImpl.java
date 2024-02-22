package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.domain.ChecklistDetail;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChecklistDetailRepositoryImpl implements ChecklistDetailRepository {

    private final ChecklistDetailJpaRepository checklistDetailJpaRepository;

    @Override
    public ChecklistDetail save(ChecklistDetail checklistDetail) {
        return checklistDetailJpaRepository.save(ChecklistDetailEntity.fromModel(checklistDetail)).toModel();
    }

    @Override
    public ChecklistDetail save(ChecklistDetail checklistDetail, Checklist checklist) {
        return checklistDetailJpaRepository.save(ChecklistDetailEntity.fromModel(checklistDetail, checklist)).toModel();
    }

    @Override
    public List<ChecklistDetail> saveAll(List<ChecklistDetail> checklistDetails, Checklist checklist) {
        List<ChecklistDetailEntity> checklistDetailEntities = checklistDetailJpaRepository.saveAll(
                checklistDetails.stream()
                        .map(checklistDetail -> ChecklistDetailEntity.fromModel(checklistDetail, checklist))
                        .toList());

        return checklistDetailEntities.stream()
                .map(c -> ChecklistDetail.builder()
                        .id(c.getId())
                        .contents(c.getContents())
                        .reason(c.getReason())
                        .isChecked(c.isChecked())
                        .checklistId(c.getChecklist().getId())
                        .build()
                ).toList();

    }

    @Override
    public Optional<ChecklistDetail> findById(Long id) {
        return checklistDetailJpaRepository.findById(id).map(ChecklistDetailEntity::toModel);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        checklistDetailJpaRepository.deleteAllById(ids);
    }


}
