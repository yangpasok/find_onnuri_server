package com.yeppi.findonnuri.repository;

import com.yeppi.findonnuri.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PersonRepository extends MongoRepository<Person, String> {
    public List<Person> findByName(String name);
}
