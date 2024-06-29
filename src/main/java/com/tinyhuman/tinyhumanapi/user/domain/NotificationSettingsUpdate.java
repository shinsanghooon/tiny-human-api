package com.tinyhuman.tinyhumanapi.user.domain;

import lombok.Builder;

public record NotificationSettingsUpdate (boolean isAllowChatNotifications, boolean isAllowDiaryNotifications){

    @Builder
    public NotificationSettingsUpdate {
    }

}
