package com.by.dallinday.member.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberPatchRequest {
    @Size(min = 1, max = 10)
    private String username;
}
