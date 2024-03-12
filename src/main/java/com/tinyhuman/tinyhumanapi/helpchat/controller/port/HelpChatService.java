package com.tinyhuman.tinyhumanapi.helpchat.controller.port;

import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatCreate;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatLatestMessage;
import com.tinyhuman.tinyhumanapi.helpchat.controller.port.dto.HelpChatResponse;

import java.util.List;

public interface HelpChatService {

    HelpChatResponse register(HelpChatCreate helpChatCreate);

    List<HelpChatResponse> getHelpChats();


    void updateLatestMessage(Long hepRequestId, HelpChatLatestMessage helpChatLatestMessage);

    void delete(Long id);
}
