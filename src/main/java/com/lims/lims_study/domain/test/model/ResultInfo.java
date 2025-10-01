package com.lims.lims_study.domain.test.model;

import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;

public class ResultInfo {

    private Long id;
    private String resultData;
    private String resultValue;
    private String resultUnit;
    private boolean requiresApproval;

    // MyBatis용 기본 생성자
    public ResultInfo() {
    }

    public ResultInfo(String resultData, String resultValue, String resultUnit, boolean requiresApproval) {
        validateInputs(resultData);
        this.resultData = resultData;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.requiresApproval = requiresApproval;
    }

    public ResultInfo(String resultData, boolean requiresApproval) {
        validateInputs(resultData);
        this.resultData = resultData;
        this.requiresApproval = requiresApproval;
    }

    private void validateInputs(String resultData) {
        if (resultData == null || resultData.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "결과 데이터는 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public void updateResult(String resultData, String resultValue, String resultUnit) {
        if (resultData != null && !resultData.trim().isEmpty()) {
            this.resultData = resultData;
        }
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
    }
}
