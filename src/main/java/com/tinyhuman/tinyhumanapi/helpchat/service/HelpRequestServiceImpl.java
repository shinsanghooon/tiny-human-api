package com.tinyhuman.tinyhumanapi.helpchat.service;

import com.tinyhuman.tinyhumanapi.auth.controller.port.AuthService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.HelpRequestService;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestResponse;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestCreate;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;
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
public class HelpRequestServiceImpl implements HelpRequestService {

    private final AuthService authService;

    private final HelpRequestRepository helpRequestRepository;

    private final PushService pushService;

    @Builder
    public HelpRequestServiceImpl(AuthService authService, HelpRequestRepository helpRequestRepository, PushService pushService) {
        this.authService = authService;
        this.helpRequestRepository = helpRequestRepository;
        this.pushService = pushService;
    }

    @Override
    public HelpRequestResponse register(HelpRequestCreate helpRequestCreate) {
        User user = authService.getUserOutOfSecurityContextHolder();
        if (!user.id().equals(helpRequestCreate.userId())) {
            throw new IllegalArgumentException("사용자 아이디가 일치하지 않습니다.");
        }

        HelpRequest savedHelpRequest = helpRequestRepository.save(HelpRequest.fromCreate(helpRequestCreate));
        pushService.pushMessage(user.id(), helpRequestCreate.requestType(), helpRequestCreate.contents());
        return savedHelpRequest.toResponse();
    }

    @Override
    public List<HelpRequestResponse> getHelpRequest() {
        User user = authService.getUserOutOfSecurityContextHolder();
        return helpRequestRepository.findByUserId(user.id()).stream().map(HelpRequest::toResponse).toList();
    }
    @Override
    public List<HelpRequestResponse> getHelpAllRequest() {
        return helpRequestRepository.findAll().stream().map(HelpRequest::toResponse).toList();
    }

    @Override
    public HelpRequestResponse update(HelpRequest helpRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

}
