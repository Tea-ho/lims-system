package com.lims.lims_study.global.common;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class PageRequest {
    
    @Min(value = 0, message = "페이지는 0 이상이어야 합니다.")
    private int page = 0;
    
    @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
    @Max(value = 100, message = "사이즈는 100 이하여야 합니다.")
    private int size = 20;
    
    private String sort;
    
    public PageRequest() {}
    
    public PageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }
    
    public PageRequest(int page, int size, String sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
    
    public int getOffset() {
        return page * size;
    }
    
    public void setPage(int page) {
        this.page = Math.max(0, page);
    }
    
    public void setSize(int size) {
        this.size = size > 100 ? 100 : Math.max(1, size);
    }
    
    public void setSort(String sort) {
        this.sort = sort;
    }
}
