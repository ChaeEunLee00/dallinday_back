package com.by.dallinday.spot.dto;

import com.by.dallinday.spot.tourAPI.SpotRepeat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpotDetailResponse {
    private Long spotId;

    private String name;

    private String addr1;

    private String addr2;

    private String description;

    private String closedDays;

    private String openingHours;

    private String parking;

    private String babyCarriage;

    private String creditCard;

    private String infoCenter;

    private List<SpotRepeat> etc;
}
