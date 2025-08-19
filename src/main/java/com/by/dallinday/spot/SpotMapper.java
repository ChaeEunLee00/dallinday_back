package com.by.dallinday.spot;


import com.by.dallinday.spot.dto.SpotResponse;
import com.by.dallinday.spot.tourAPI.SpotItem;
import org.springframework.stereotype.Component;

@Component
public class SpotMapper {

    public SpotResponse spotItemToSpotResponse(SpotItem item) {
        if ( item == null ) {
            return null;
        }

        SpotResponse spotResponse = new SpotResponse();

        spotResponse.setSpotId(item.getSpotId());
        spotResponse.setName(item.getTitle());
        spotResponse.setLongitude(item.getMapx());
        spotResponse.setLatitude(item.getMapy());
        spotResponse.setDist(item.getDist());
        spotResponse.setFirstimage(item.getFirstimage());
        spotResponse.setFirstimage2(item.getFirstimage2());
        spotResponse.setAddr1(item.getAddr1());
        spotResponse.setAddr2(item.getAddr2());
        spotResponse.setZipcode(item.getZipcode());
        spotResponse.setAreacode(item.getAreacode());
        spotResponse.setContenttypeid(item.getContenttypeid());
        spotResponse.setCpyrhtDivCd(item.getCpyrhtDivCd());
        spotResponse.setMlevel(item.getMlevel());
        spotResponse.setCreatedtime(item.getCreatedtime());
        spotResponse.setModifiedtime(item.getModifiedtime());

        return spotResponse;
    }
}
