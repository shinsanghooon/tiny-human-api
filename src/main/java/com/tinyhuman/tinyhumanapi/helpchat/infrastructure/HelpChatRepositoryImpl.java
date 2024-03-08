package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;

import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;
import com.tinyhuman.tinyhumanapi.helpchat.service.port.HelpRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HelpChatRepositoryImpl implements HelpRequestRepository {

    private final HelpChatJpaRepository helpChatJpaRepository;

    @Override
    public HelpRequest save(HelpRequest helpRequest) {
        HelpRequestEntity helpRequestEntity = HelpRequestEntity.fromModel(helpRequest);
        return helpChatJpaRepository.save(helpRequestEntity).toModel();
    }

    @Override
    public List<HelpRequest> findByUserId(Long userId) {
        return helpChatJpaRepository.findByUserId(userId).stream()
                .map(HelpRequestEntity::toModel)
                .toList();
    }

    @Override
    public Optional<HelpRequest> findById(Long id) {
        return helpChatJpaRepository.findById(id).map(HelpRequestEntity::toModel);
    }

    @Override
    public Optional<HelpRequest> findByIdAndUserId(Long id, Long userId) {
        return helpChatJpaRepository.findByIdAndUserId(id, userId).map(HelpRequestEntity::toModel);
    }
}
