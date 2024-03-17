package com.tinyhuman.tinyhumanapi.helpchat.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.common.exception.ResourceNotFoundException;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.HelpChatService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatCreate;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatLatestMessage;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpChat;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;
import com.tinyhuman.tinyhumanapi.helpchat.service.port.HelpChatRepository;
import com.tinyhuman.tinyhumanapi.helpchat.service.port.HelpRequestRepository;
import com.tinyhuman.tinyhumanapi.integration.service.port.PushService;
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

    private final HelpRequestRepository helpRequestRepository;

    private final PushService pushService;

    @Builder
    public HelpChatServiceImpl(AuthService authService, HelpChatRepository helpChatRepository, HelpRequestRepository helpRequestRepository, PushService pushService) {
        this.authService = authService;
        this.helpChatRepository = helpChatRepository;
        this.helpRequestRepository = helpRequestRepository;
        this.pushService = pushService;
    }

    @Override
    public HelpChatResponse register(HelpChatCreate HelpChatCreate) {
        HelpChat savedHelpChat = helpChatRepository.save(HelpChat.fromCreate(HelpChatCreate));
        HelpRequest helpRequest = helpRequestRepository.findById(savedHelpChat.helpRequestId())
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(HelpRequest) - HelpRequest:{}", savedHelpChat.helpRequestId());
                    return new ResourceNotFoundException("HelpRequest", savedHelpChat.helpRequestId());
                });
        HelpChatResponse response = savedHelpChat.toResponse();
        return response.addHelpRequest(helpRequest.toResponse());
    }

    @Override
    public List<HelpChatResponse> getHelpChats() {
        User user = authService.getUserOutOfSecurityContextHolder();
        List<HelpChatResponse> helpChatResponses = helpChatRepository.findByHelpRequestUserIdOrHelpAnswerUserId(user.id(), user.id()).stream().map(HelpChat::toResponse).toList();

        List<HelpChatResponse> helpChatResponseWithRequest = helpChatResponses.stream().map(helpChatResponse -> {
            HelpRequest helpRequest = helpRequestRepository.findById(helpChatResponse.helpRequestId())
                    .orElseThrow(() -> {
                        log.error("ResourceNotFoundException(HelpRequest) - HelpRequest:{}", helpChatResponse.helpRequestId());
                        return null;
                    });
            return helpChatResponse.addHelpRequest(helpRequest.toResponse());
        }).toList();

        for (HelpChatResponse helpChatResponse : helpChatResponseWithRequest) {
            System.out.println("helpChatResponse.toString() = " + helpChatResponse.toString());
        }

        return helpChatResponseWithRequest;
    }

    @Override
    public void updateLatestMessage(Long helpChatId, HelpChatLatestMessage helpChatLatestMessage) {
        User user = authService.getUserOutOfSecurityContextHolder();

        HelpChat helpChat = helpChatRepository.findById(helpChatId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(HelpChat) - HelpChat:{}", helpChatId);
                    return new ResourceNotFoundException("HelpChat", helpChatId);
                });

        HelpChat messageUpdatedHelpChat = helpChat.addLatestMessage(helpChatLatestMessage);
        helpChatRepository.save(messageUpdatedHelpChat);

        Long helpAnswerUserId = helpChatLatestMessage.helpAnswerUserId();
        Long helpRequestUserId = helpChatLatestMessage.helpRequestUserId();
        Long userId = user.id();

        Long toUserId;
        if (userId == helpAnswerUserId) {
            toUserId = helpRequestUserId;
        } else {
            toUserId = helpAnswerUserId;
        }

        pushService.pushMessageToUser(userId, toUserId, helpChatLatestMessage.message());
    }

    @Override
    public void delete(Long id) {

    }
}
