package com.tinyhuman.tinyhumanapi.helpchat.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.HelpChatService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatCreate;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpChat;
import com.tinyhuman.tinyhumanapi.helpchat.service.port.HelpChatRepository;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class HelpChatServiceImpl implements HelpChatService {

    private final AuthService authService;

    private final HelpChatRepository helpChatRepository;

    @Builder
    public HelpChatServiceImpl(AuthService authService, HelpChatRepository helpChatRepository) {
        this.authService = authService;
        this.helpChatRepository = helpChatRepository;
    }

    @Override
    public HelpChatResponse register(HelpChatCreate HelpChatCreate) {
        HelpChat savedHelpChat = helpChatRepository.save(HelpChat.fromCreate(HelpChatCreate));
        return savedHelpChat.toResponse();
    }

    @Override
    public List<HelpChatResponse> getHelpChats() {
        User user = authService.getUserOutOfSecurityContextHolder();
        return helpChatRepository.findByHelpRequestUserIdOrHelpAnswerUserId(user.id(), user.id()).stream().map(HelpChat::toResponse).toList();
    }

    @Override
    public void delete(Long id) {

    }
}
