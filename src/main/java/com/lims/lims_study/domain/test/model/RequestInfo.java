package com.lims.lims_study.domain.test.model;

import com.lims.lims_study.application.test.dto.TestUpdateDto;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;

public class RequestInfo {

    private Long id;
    private String title;
    private String description;

    // MyBatis용 기본 생성자
    public RequestInfo() {
    }

    public RequestInfo(String title, String description) {
        validateInputs(title, description);
        this.title = title;
        this.description = description;
    }

    private void validateInputs(String title, String description) {
        if (title == null || title.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "제목은 필수입니다.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "설명은 필수입니다.");
        }
    }

    public void updateRequestInfo(TestUpdateDto dto){
        this.title = dto.getTitle();
        this.description = dto.getDescription();
    }

    // getter and setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}