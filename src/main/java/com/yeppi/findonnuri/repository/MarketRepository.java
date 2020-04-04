package com.yeppi.findonnuri.repository;

import com.yeppi.findonnuri.model.Market;
import com.yeppi.findonnuri.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;
import sun.jvm.hotspot.oops.Mark;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ListIterator;

@Repository
public interface MarketRepository extends MongoMarketRepository, CustomMarketRepository {

}

interface MongoMarketRepository extends MongoRepository<Market, String> {

}

interface CustomMarketRepository {
    List<Market> getMarketWhereGeoInfoNotUpdated(int limit);
    long getMarketCntWhereGeoInfoNotUpdated();
    void updateMarketGeoInfo(String marketName, String affiliatedAddress, String address, Double longitude, Double latitude);
    void updateLastUpdateDate(String marketName, String affiliatedAddress, String address);
}

//@RequiredArgsConstructor
class CustomMarketRepositoryImpl implements CustomMarketRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public long getMarketCntWhereGeoInfoNotUpdated() {
        Query query = new Query();
        query.addCriteria(Criteria.where("last_updated_time").exists(false));
        List<Market> list = mongoTemplate.find(query, Market.class, "onnuri");
        return list.size();
    }

    @Override
    public List<Market> getMarketWhereGeoInfoNotUpdated(int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("last_updated_time").exists(false)).limit(limit);
        List<Market> marketList = mongoTemplate.find(query, Market.class,"onnuri");
        return marketList;
    }

    @Override
    public void updateMarketGeoInfo(String marketName, String affiliatedAddress, String marketAddress, Double longitude, Double latitude) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("market_name").is(marketName),
                Criteria.where("affiliated_market_name").is(affiliatedAddress),
                Criteria.where("address").is(marketAddress)));

        List<Market> marketList = mongoTemplate.find(query, Market.class, "onnuri");

        if (marketList.size() > 1) {
            System.out.println(marketName + " 이름으로 등록된 정보가 2개 이상임" );
        }

        ListIterator iterator = marketList.listIterator();
        while (iterator.hasNext()) {
            Market market = (Market) iterator.next();
            market.latitude = latitude;
            market.longitude = longitude;

            Update update = new Update();
            update.set("latitude", market.latitude);
            update.set("longitude", market.longitude);
            mongoTemplate.updateMulti(query, update, Market.class);
            //mongoTemplate.save(market,"onnuri");
            System.out.println(marketName + "/" + affiliatedAddress + "/" + marketAddress + " 의 위도/경도 값이 기록됨");
        }
    }

    @Override
    public void updateLastUpdateDate(String marketName, String affiliatedAddress, String address) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("market_name").is(marketName),
                Criteria.where("affiliated_market_name").is(affiliatedAddress),
                Criteria.where("address").is(address)));

        List<Market> marketList = mongoTemplate.find(query, Market.class, "onnuri");

        if (marketList.size() > 1) {
            System.out.println(marketName + " 이름으로 등록된 정보가 2개 이상임" );
        }

        ListIterator iterator = marketList.listIterator();
        while (iterator.hasNext()) {
            Market market = (Market) iterator.next();

            Update update = new Update();
            update.set("last_updated_time", LocalDateTime.now());
            mongoTemplate.updateMulti(query, update, Market.class);
            //mongoTemplate.save(market, "onnuri");
            System.out.println(marketName + "/" + affiliatedAddress + "/" + address + " 의 마지막 업데이트 시간이 기록됨");
        }
    }
}
