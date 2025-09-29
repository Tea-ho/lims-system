package com.lims.lims_study.domain.test.model;

public class ResultInfo {

    private Long id;
    private String resultData;
    private boolean requiresApproval;

    public ResultInfo(String resultData, boolean requiresApproval) {
        this.resultData = resultData;
        this.requiresApproval = requiresApproval;
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

}