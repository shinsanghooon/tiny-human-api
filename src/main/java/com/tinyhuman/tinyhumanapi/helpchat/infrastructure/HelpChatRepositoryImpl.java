package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;

import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpChat;
import com.tinyhuman.tinyhumanapi.helpchat.service.port.HelpChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HelpChatRepositoryImpl implements HelpChatRepository {

    private final HelpChatJpaRepository helpChatJpaRepository;

    @Override
    public HelpChat save(HelpChat helpChat) {
        HelpChatEntity helpChatEntity = HelpChatEntity.fromModel(helpChat);
        return helpChatJpaRepository.save(helpChatEntity).toModel();
    }

    @Override
    public List<HelpChat> findByHelpRequestUserIdOrHelpAnswerUserId(Long requestUserId, Long answerUserId) {
        return helpChatJpaRepository.findByHelpRequestUserIdOrHelpAnswerUserId(requestUserId, answerUserId).stream()
                .map(HelpChatEntity::toModel)
                .toList();
    }

    @Override
    public Optional<HelpChat> findById(Long id) {
        return helpChatJpaRepository.findById(id).map(HelpChatEntity::toModel);
    }

    @Override
    public Optional<HelpChat> findByHelpRequestIdAndHelpRequestUserIdAndHelpAnswerUserId(Long requestId, Long requestUserId, Long answerUserId) {
        return helpChatJpaRepository.findByHelpRequestIdAndHelpRequestUserIdAndHelpAnswerUserId(requestId, requestUserId, answerUserId)
                .map(HelpChatEntity::toModel);
    }

}
