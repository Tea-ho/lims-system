package com.lims.lims_study.global.event.user;

import com.lims.lims_study.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class UserCreatedEvent extends BaseEvent {
    private final Long userId;
    private final String username;
    private final String authorities;

    public UserCreatedEvent(Object source, Long userId, String username, String authorities) {
        super(source, "USER_CREATED");
        this.userId = userId;
        this.username = username;
        this.authorities = authorities;
    }
}
