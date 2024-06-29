package com.tinyhuman.tinyhumanapi.user.controller.port;

import com.tinyhuman.tinyhumanapi.user.domain.NotificationSettingsUpdate;
import com.tinyhuman.tinyhumanapi.user.domain.UserCreate;
import com.tinyhuman.tinyhumanapi.user.domain.UserResponse;

public interface UserService {

    UserResponse registerUser(UserCreate userCreate);

    UserResponse getUser(Long userId);

    void checkEmailDuplicated(String email);

    UserResponse updateNotificationSettings(Long userId, NotificationSettingsUpdate notificationSettingsUpdate);
}
