package com.by.dallinday.spot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpotResponse {

    private Long spotId;;

    private String name;

    private Double longitude;

    private Double latitude;

    private Double dist;

    private String firstimage;

    private String firstimage2;

    private String addr1;

    private String addr2;

    private String areacode;

    private String zipcode;

    private String contenttypeid;

    private String cpyrhtDivCd;

    private String mlevel;

    private String createdtime;

    private String modifiedtime;
}
