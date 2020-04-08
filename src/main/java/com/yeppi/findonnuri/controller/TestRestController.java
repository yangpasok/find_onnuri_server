package com.yeppi.findonnuri.controller;

import com.yeppi.findonnuri.model.Market;
import com.yeppi.findonnuri.model.NaverMapGeocodeAPIResponse;
import com.yeppi.findonnuri.model.Person;
import com.yeppi.findonnuri.service.MarketService;
import com.yeppi.findonnuri.service.NaverAPIService;
import com.yeppi.findonnuri.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.jvm.hotspot.oops.Mark;

import java.util.List;
import java.util.ListIterator;

//@RestController를 사용하면 @ResponseBody를 추가 할 필요가 없고, @ResponseBody 어노테이션은 기본적으로 활성화되어 있다.
@RestController
public class TestRestController {
    @Autowired
    private PersonService personService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private NaverAPIService naverAPIService;

    @RequestMapping(value="/testValue", method = RequestMethod.GET)
    public String getTestValue(){
        String TestValue = "레스트 컨트롤러 테스트";
        return TestValue;
    }

    //(@RequestParam(value="name", defaultValue="")
    @GetMapping(value = "/getPersonInfo")
    public List<Person> getPersonInfo(String name) {
        return personService.getPersonByName(name);
    }

    @GetMapping(value = "/getMarketInfo")
    public String getMarketInfo() {
        String before = marketService.getMarketCntWhereGeoInfoNotUpdated() + "개 업데이트 더 해야됨 (before)";

        final List<Market> marketList = marketService.getMarketWhereGeoInfoNotUpdated(10000);
        System.out.println("총 " + marketList.size() + "개의 마켓이 검색됨");
        ListIterator iterator = marketList.listIterator();
        while (iterator.hasNext()) {
            Market market = (Market) iterator.next();
            System.out.println("------------------------------------------------------------------");
            System.out.println("마켓명 : " + market.marketName + " , " + "마켓주소 : " + market.address);
            NaverMapGeocodeAPIResponse response = naverAPIService.getGeoCode(market.address);

            NaverMapGeocodeAPIResponse.AddressInfo[] addressInfo = response.addressInfos;
            if (addressInfo.length == 0) {
                //잘못된 주소지로 조회해서 x,y 좌표가 없는 경우
                System.out.println("api 호출 결과 : " + "x(경도)-" + "null" + ", y(위도)-" + "null");
                marketService.updateMarketGeoInfo(market.marketName, market.affiliatedMarketName, market.address, null, null);
            } else {
                System.out.println("api 호출 결과 : " + "x(경도)-" + addressInfo[0].longtitude + ", y(위도)-" + addressInfo[0].latitude);
                marketService.updateMarketGeoInfo(market.marketName, market.affiliatedMarketName, market.address, addressInfo[0].longtitude, addressInfo[0].latitude);
            }

            marketService.updateMarketGeoLastUpdateDate(market.marketName, market.affiliatedMarketName, market.address);
        }
        String after = marketService.getMarketCntWhereGeoInfoNotUpdated() + "개 업데이트 더 해야됨 (after)";

        return before + "\n" + after;
    }
}
