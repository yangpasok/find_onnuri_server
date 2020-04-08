package com.yeppi.findonnuri.repository;
import com.yeppi.findonnuri.model.Location;
import com.yeppi.findonnuri.model.Market;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

class CustomMarketRepositoryImpl implements CustomMarketRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public long getMarketCntWhereGeoInfoNotUpdated() {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("geo_updated_time").exists(false),
                Criteria.where("geo_updated_time").is(null)));
        List<Market> list = mongoTemplate.find(query, Market.class, "onnuri");
        return list.size();
    }

    @Override
    public List<Market> getMarketWhereGeoInfoNotUpdated(int limit) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("last_location_update_time").exists(false),
                Criteria.where("last_location_update_time").is(null))).limit(limit);
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

            Location location = new Location();
            ArrayList<Double> list = new ArrayList<Double>();
            list.add(longitude);
            list.add(latitude);
            location.type = "Point";
            location.coordinates = list;

            Update update = new Update();
            update.set("location", location);
            mongoTemplate.updateMulti(query, update, Market.class);
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
            update.set("last_location_update_time", LocalDateTime.now());
            mongoTemplate.updateMulti(query, update, Market.class);
            System.out.println(marketName + "/" + affiliatedAddress + "/" + address + " 의 마지막 업데이트 시간이 기록됨");
        }
    }
}
