package com.tinyhuman.tinyhumanapi.helpchat.service.port;

import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpChat;

import java.util.List;
import java.util.Optional;

public interface HelpChatRepository {
    HelpChat save(HelpChat helpChat);

    List<HelpChat> findByHelpRequestUserIdOrHelpAnswerUserId(Long requestUserId, Long answerUserId);

    Optional<HelpChat> findById(Long id);

    Optional<HelpChat> findByHelpRequestIdAndHelpRequestUserIdAndHelpAnswerUserId(Long requestId, Long requestUserId, Long answerUserId);

}
