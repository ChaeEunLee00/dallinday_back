package com.by.dallinday.spot;

import com.by.dallinday.member.Member;
import org.springframework.stereotype.Service;

@Service
public class SpotService {
//    private final SpotRepository spotRepository;

    // 관광지 리스트 조회
    public Spot findSpotList() {
        return new Spot();
    }
}
