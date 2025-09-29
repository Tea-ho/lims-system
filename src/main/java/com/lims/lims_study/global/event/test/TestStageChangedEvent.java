package com.lims.lims_study.global.event.test;

import com.lims.lims_study.global.event.BaseEvent;
import lombok.Getter;

@Getter
public class TestStageChangedEvent extends BaseEvent {
    private final Long testId;
    private final String previousStage;
    private final String currentStage;
    private final Long userId;

    public TestStageChangedEvent(Object source, Long testId, String previousStage, String currentStage, Long userId) {
        super(source, "TEST_STAGE_CHANGED");
        this.testId = testId;
        this.previousStage = previousStage;
        this.currentStage = currentStage;
        this.userId = userId;
    }
}
