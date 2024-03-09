package com.tinyhuman.tinyhumanapi.helpchat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpChatJpaRepository extends JpaRepository<HelpChatEntity, Long> {

    List<HelpChatEntity> findByHelpRequestUserIdOrHelpAnswerUserId(Long requestUserId, Long answerUserId);

}
