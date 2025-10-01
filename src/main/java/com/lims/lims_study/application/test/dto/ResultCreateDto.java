package com.lims.lims_study.application.test.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultCreateDto {
    private String resultData;
    private String resultValue;
    private String resultUnit;
    private boolean requiresApproval;
}
