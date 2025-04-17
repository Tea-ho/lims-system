package com.lims.lims_study.domain.test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestInfo {

    private Long id;
    private String title;
    private String description;
    private boolean requiresApproval;
    private Long approvalId;

    public RequestInfo(String title, String description, boolean requiresApproval) {
        this.title = title;
        this.description = description;
        this.requiresApproval = requiresApproval;
    }
}