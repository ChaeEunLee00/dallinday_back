package com.by.dallinday.run.dto;

import com.by.dallinday.run.Run;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RunMapper {
    @BeanMapping(ignoreByDefault = true)
    Run runPostRequestToRun(RunPostRequest runPostRequest);
}
