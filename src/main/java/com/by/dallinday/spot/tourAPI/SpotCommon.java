package com.by.dallinday.spot.tourAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotCommon {
    private Long contentid;

    private String title;

    private String addr1;

    private String addr2;

    private String overview;
}
