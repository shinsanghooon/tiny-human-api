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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebasePushService implements PushService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final UserPushTokenRepository userPushTokenRepository;
    @Override
    public void pushMessage(Long userId, RequestType requestType, String contents) {
        /**
         * 1. 특정 조건에 해당하는 유저를 찾는다.
         * 2. 조건에 해당하는 유저의 FCM 토큰을 불러온다
         * 3. 푸시 메시지를 만들어서 날린다.
         */
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

        Notification notification = Notification.builder()
                .setTitle("도움이 필요해요!")
                .setBody(contents)
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(notification)
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
