package com.lims.lims_study.application.test.dto;

public class CompletionCreateDto {
    private String completionNotes;
    private boolean requiresApproval;

    public String getCompletionNotes() {
        return completionNotes;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }
}