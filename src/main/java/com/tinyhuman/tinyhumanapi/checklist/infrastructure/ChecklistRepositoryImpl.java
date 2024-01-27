package com.tinyhuman.tinyhumanapi.checklist.infrastructure;

import com.tinyhuman.tinyhumanapi.checklist.domain.Checklist;
import com.tinyhuman.tinyhumanapi.checklist.service.port.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChecklistRepositoryImpl implements ChecklistRepository {

    private final ChecklistJpaRepository checklistJpaRepository;

    @Override
    public Checklist save(Checklist checklist) {
        return checklistJpaRepository.save(ChecklistEntity.fromModel(checklist)).toModel();
    }

    @Override
    public List<Checklist> findByUserId(Long userId) {
        return checklistJpaRepository.findByUserId(userId).stream()
                .map(ChecklistEntity::toModel)
                .toList();
    }

    @Override
    public Optional<Checklist> findById(Long id) {
        return checklistJpaRepository.findById(id).map(ChecklistEntity::toModel);
    }
}
