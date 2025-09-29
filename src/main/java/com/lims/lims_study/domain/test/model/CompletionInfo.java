package com.lims.lims_study.domain.test.model;

public class CompletionInfo {

    private Long id;
    private String completionNotes;
    private boolean requiresApproval;

    public CompletionInfo(String completionNotes, boolean requiresApproval) {
        this.completionNotes = completionNotes;
        this.requiresApproval = requiresApproval;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompletionNotes() {
        return completionNotes;
    }

    public void setCompletionNotes(String completionNotes) {
        this.completionNotes = completionNotes;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

}