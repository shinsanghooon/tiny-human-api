package com.tinyhuman.tinyhumanapi.integration.service;

import com.google.firebase.messaging.*;
import com.tinyhuman.tinyhumanapi.helpchat.enums.RequestType;
import com.tinyhuman.tinyhumanapi.integration.service.port.PushService;
import com.tinyhuman.tinyhumanapi.user.domain.User;
import com.tinyhuman.tinyhumanapi.user.domain.UserPushToken;
import com.tinyhuman.tinyhumanapi.user.service.port.UserPushTokenRepository;
import com.tinyhuman.tinyhumanapi.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebasePushService implements PushService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final UserPushTokenRepository userPushTokenRepository;

    @Override
    public void chatCreatePushMessage(Long userId, Long helpRequestId, RequestType requestType, String contents) {
        /**
         * 1. 특정 조건에 해당하는 유저를 찾는다.
         * 2. 조건에 해당하는 유저의 FCM 토큰을 불러온다
         * 3. 푸시 메시지를 만들어서 날린다.
         */

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - toUserId:{}", userId);
                    return null;
                });

        List<User> targetUsers = userRepository.findRandomUser(25);
        if (requestType.equals(RequestType.LOCATION)) {
            System.out.println("requestType.getKorean() = " + requestType.getKorean());
        } else if (requestType.equals(RequestType.KEYWORD)) {
            System.out.println("requestType.getKorean() = " + requestType.getKorean());
        } else {
            targetUsers = userRepository.findRandomUser(25);
        }

        List<Long> targetUserIds = targetUsers.stream().map(User::id).filter(id -> !id.equals(userId)).toList();
        List<UserPushToken> targetUserTokens = userPushTokenRepository.findByUserIds(targetUserIds);
        List<String> registrationTokens = targetUserTokens.stream().map(UserPushToken::fcmToken).toList();

        String message;
        if (user.name() == null || user.name().isEmpty() || user.name().isBlank()) {
            message = "도움이 필요해요!";
        } else {
            message = user.name() + "님이 도움을 요청하고 있어요!";
        }

        Map<String, String> data = new HashMap<>();
        data.put("helpRequestId", helpRequestId.toString());

        sendMultiMessageWithTokens(message, data, contents, registrationTokens);
    }

    @Override
    public void pushMessageToUser(Long chatId, Long fromUserId, Long toUserId, String contents) {

        User user = userRepository.findById(fromUserId)
                .orElseThrow(() -> {
                    log.error("ResourceNotFoundException(User) - toUserId:{}", toUserId);
                    return null;
                });

        System.out.println("fromUserId = " + fromUserId);
        System.out.println("toUserId = " + toUserId);
        List<UserPushToken> toUserTokens = userPushTokenRepository.findByUserId(toUserId);
        List<String> registrationTokens = toUserTokens.stream().map(UserPushToken::fcmToken).toList();

        String message;
        if (user.name() == null || user.name().isEmpty() || user.name().isBlank()) {
            message = "메시지가 도착했습니다.";
        } else {
            message = user.name() + "님이 메시지를 보냈습니다.";
        }

        Map<String, String> data = new HashMap<>();
        data.put("chatId", chatId.toString());

        sendMultiMessageWithTokens(message, data, contents, registrationTokens);
    }

    private void sendMultiMessageWithTokens(String pushTitle, Map<String, String> data, String contents, List<String> registrationTokens) {
        Notification notification = Notification.builder()
                .setTitle(pushTitle)
                .setBody(contents)
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
                .putAllData(data)
                .addAllTokens(registrationTokens)
                .build();

        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(message);
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                List<String> failedTokens = new ArrayList<>();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        // The order of responses corresponds to the order of the registration tokens.
                        System.out.println("Token = " + registrationTokens.get(i));
                        System.out.println("Message = " + responses.get(i).getException().getMessage());
                        failedTokens.add(registrationTokens.get(i));
                    }
                }
                System.out.println("List of tokens that caused failures: " + failedTokens);
            } else {
                System.out.println("All messages are delivered well!" + registrationTokens.size() + " messages.");
            }
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}
