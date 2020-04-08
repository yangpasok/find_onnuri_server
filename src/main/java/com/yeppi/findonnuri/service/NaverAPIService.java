package com.yeppi.findonnuri.service;

import com.yeppi.findonnuri.model.NaverMapGeocodeAPIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverAPIService {
    //application.properties에 설정된 naverservice.application.clientID=propertiestest 의 값인 "propertiestest" 입력 된다.
    @Value("${naverservice.application.clientID}")
    private String naverServiceClientID;

    @Value("${naverservice.application.clientsecret}")
    private String naverServiceSecret;

    @Autowired
    RestTemplate restTemplate;

    public NaverMapGeocodeAPIResponse getGeoCode(String address) {
        String urlTemplate = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query={address}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-NCP-APIGW-API-KEY-ID", naverServiceClientID);
        httpHeaders.add("X-NCP-APIGW-API-KEY", naverServiceSecret);
        HttpEntity<String> requestHttpEntity = new HttpEntity<>(httpHeaders);

        System.out.println("");

        Map<String, Object> params = new HashMap<>();
        params.put("address", address);

        NaverMapGeocodeAPIResponse res = restTemplate.exchange(urlTemplate, HttpMethod.GET, requestHttpEntity, NaverMapGeocodeAPIResponse.class, params).getBody();
        return res;
    }
}

