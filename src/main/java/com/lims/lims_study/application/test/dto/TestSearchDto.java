package com.lims.lims_study.application.test.dto;

import com.lims.lims_study.domain.test.model.TestStage;

public class TestSearchDto {
    private String title;
    private String username;
    private TestStage stage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TestStage getStage() {
        return stage;
    }

    public void setStage(TestStage stage) {
        this.stage = stage;
    }
}