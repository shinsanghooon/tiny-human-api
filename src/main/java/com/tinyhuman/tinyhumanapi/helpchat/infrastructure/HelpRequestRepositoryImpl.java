package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;

import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;
import com.tinyhuman.tinyhumanapi.helpchat.service.port.HelpRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HelpRequestRepositoryImpl implements HelpRequestRepository {

    private final HelpRequestJpaRepository helpRequestJpaRepository;

    @Override
    public HelpRequest save(HelpRequest helpRequest) {
        HelpRequestEntity helpRequestEntity = HelpRequestEntity.fromModel(helpRequest);
        return helpRequestJpaRepository.save(helpRequestEntity).toModel();
    }

    @Override
    public List<HelpRequest> findByUserId(Long userId) {
        return helpRequestJpaRepository.findByUserId(userId).stream()
                .map(HelpRequestEntity::toModel)
                .toList();
    }

    @Override
    public List<HelpRequest> findAll() {
        return helpRequestJpaRepository.findAll().stream()
                .map(HelpRequestEntity::toModel)
                .toList();
    }

    @Override
    public Optional<HelpRequest> findById(Long id) {
        return helpRequestJpaRepository.findById(id).map(HelpRequestEntity::toModel);
    }

    @Override
    public Optional<HelpRequest> findByIdAndUserId(Long id, Long userId) {
        return helpRequestJpaRepository.findByIdAndUserId(id, userId).map(HelpRequestEntity::toModel);
    }
}
