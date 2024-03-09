package com.tinyhuman.tinyhumanapi.helpchat.controller.port;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestCreate;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;

import java.util.List;

public interface HelpRequestService {

    HelpChatResponse register(HelpRequestCreate helpRequestCreate);

    List<HelpChatResponse> getHelpRequest();

    HelpChatResponse update(HelpRequest helpRequest);

    void delete(Long id);
}
