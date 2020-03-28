package com.yeppi.findonnuri.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("person")
@Getter
@Setter
public class Person {
    @Id
    private String id;

    private String name;

    private int age;
}
