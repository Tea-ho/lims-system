package com.lims.lims_study.global.common;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;
    private final boolean empty;

    public PageResponse(List<T> content, PageRequest pageRequest, long totalElements) {
        this.content = content;
        this.page = pageRequest.getPage();
        this.size = pageRequest.getSize();
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.first = page == 0;
        this.last = page >= totalPages - 1;
        this.empty = content.isEmpty();
    }

    public static <T> PageResponse<T> of(List<T> content, PageRequest pageRequest, long totalElements) {
        return new PageResponse<>(content, pageRequest, totalElements);
    }
}
