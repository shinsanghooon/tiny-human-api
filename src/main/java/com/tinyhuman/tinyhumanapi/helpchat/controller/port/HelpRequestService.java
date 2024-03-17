package com.tinyhuman.tinyhumanapi.helpchat.controller.port;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestResponse;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpRequestCreate;
import com.tinyhuman.tinyhumanapi.helpchat.domain.HelpRequest;

import java.util.List;

public interface HelpRequestService {

    HelpRequestResponse register(HelpRequestCreate helpRequestCreate);

    List<HelpRequestResponse> getHelpRequest();

    List<HelpRequestResponse> getHelpAllRequest();

    HelpRequestResponse update(HelpRequest helpRequest);

    void delete(Long id);

}
