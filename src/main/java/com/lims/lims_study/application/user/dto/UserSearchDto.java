package com.lims.lims_study.application.user.dto;

import com.lims.lims_study.global.common.PageRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserSearchDto extends PageRequest {
    
    @Size(min = 2, max = 50, message = "사용자명은 2자 이상 50자 이하여야 합니다.")
    private String username;
    
    private String authorities;

    public UserSearchDto(String username, String authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public UserSearchDto(String username, String authorities, int page, int size) {
        super(page, size);
        this.username = username;
        this.authorities = authorities;
    }
}
