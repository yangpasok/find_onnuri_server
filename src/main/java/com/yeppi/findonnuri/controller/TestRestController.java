package com.yeppi.findonnuri.controller;

import com.yeppi.findonnuri.model.Market;
import com.yeppi.findonnuri.model.Person;
import com.yeppi.findonnuri.service.MarketService;
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
    public List<Market> getMarketInfo() {

        final List<Market> marketList = marketService.getMarketWhereGeoInfoNotUpdated(10);

        ListIterator iterator = marketList.listIterator();
        while (iterator.hasNext()) {
            Market market = (Market) iterator.next();
            System.out.println("마켓명 : " + market.marketName + " , " + "마켓주소 : " + market.address);
        }
        return marketList;
    }
}
