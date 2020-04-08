package com.yeppi.findonnuri.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NaverMapGeocodeAPIResponse {
    public static class AddressInfo {
        @JsonProperty("x")
        public Double longtitude;

        @JsonProperty("y")
        public Double latitude;
    }

    @JsonProperty("addresses")
    public AddressInfo[] addressInfos;
}

