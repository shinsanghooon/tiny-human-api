package com.tinyhuman.tinyhumanapi.integration.service.port;

import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;

public interface PushService {

    void chatCreatePushMessage(Long userId, Long helpRequestId, RequestType requestType, String contents);

    void pushMessageToUser(Long chatId, Long fromUserId, Long toUserId, String contents);
}
