package com.yeppi.findonnuri.service;

import com.yeppi.findonnuri.model.Market;
import com.yeppi.findonnuri.model.Person;
import com.yeppi.findonnuri.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {
    @Autowired
    private MarketRepository marketRepository;

    public List<Market> getMarketWhereGeoInfoNotUpdated(int limit) {
        return marketRepository.getMarketWhereGeoInfoNotUpdated(limit);
    }

    public void updateMarketGeoInfo(String marketName, String affiliatedAddress, String address, Double longitude, Double latitude) {
        marketRepository.updateMarketGeoInfo(marketName, affiliatedAddress, address, longitude, latitude);
    }

    public void updateMarketGeoLastUpdateDate(String marketName, String affiliatedAddress, String address) {
        marketRepository.updateLastUpdateDate(marketName, affiliatedAddress, address);
    }

    public long getMarketCntWhereGeoInfoNotUpdated() {
        return marketRepository.getMarketCntWhereGeoInfoNotUpdated();
    }
}
