package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelpChatJpaRepository extends JpaRepository<HelpChatEntity, Long> {

    List<HelpChatEntity> findByHelpRequestUserIdOrHelpAnswerUserId(Long requestUserId, Long answerUserId);

    Optional<HelpChatEntity> findByHelpRequestIdAndHelpRequestUserIdAndHelpAnswerUserId(
            Long requestId, Long requestUserId, Long answerUserId);

}
