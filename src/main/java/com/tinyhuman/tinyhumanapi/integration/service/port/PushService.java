package com.tinyhuman.tinyhumanapi.integration.service.port;

import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;

public interface PushService {

    void pushMessage(Long userId, RequestType requestType, String contents);

    void pushMessageToUser(Long fromUserId, Long toUserId, String contents);
}
