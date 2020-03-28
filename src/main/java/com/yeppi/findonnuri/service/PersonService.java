package com.yeppi.findonnuri.service;

import com.yeppi.findonnuri.model.Person;
import com.yeppi.findonnuri.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepo;

    public List<Person> getPersonByName(String name) {
        return personRepo.findByName(name);
    }
}
