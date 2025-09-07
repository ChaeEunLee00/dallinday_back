package com.by.dallinday.spot.tourAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotDetail {
    private String restdate;

    private String usetime;

    private String parking;

    private String chkbabycarriage;

    private String chkcreditcard;

    private String infocenter;
}
