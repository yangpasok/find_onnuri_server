package com.yeppi.findonnuri.controller;

import com.yeppi.findonnuri.model.Person;
import com.yeppi.findonnuri.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController를 사용하면 @ResponseBody를 추가 할 필요가 없고, @ResponseBody 어노테이션은 기본적으로 활성화되어 있다.
@RestController
public class TestRestController {
    @Autowired
    private PersonService personService;

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

//    @GetMapping("/test2/{marketName}")
//    public ResponseEntity<String> test2Response(@PathVariable("marketName") String marketName) {
//        System.out.println(personService.getPersonByName(marketName));
//        return new ResponseEntity<String>("hihi", HttpStatus.OK);
//    }
//}

}
