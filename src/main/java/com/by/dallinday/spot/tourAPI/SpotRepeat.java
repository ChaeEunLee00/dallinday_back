package com.by.dallinday.spot.tourAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotRepeat {
    private String infoname;

    private String infotext;
}
