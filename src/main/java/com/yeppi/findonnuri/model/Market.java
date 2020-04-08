package com.yeppi.findonnuri.model;

import com.mongodb.lang.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document("onnuri")
@Getter
@Setter
public class Market {
    @Id
    private String id;

    @Field("market_name")
    public String marketName;

    @Field("affiliated_market_name")
    public String affiliatedMarketName;

    public String address;

    @Nullable
    public Double longitude;

    @Nullable
    public Double latitude;

    @Field("geo_updated_time")
    public LocalDate geoUpdatedTime;
}
