package com.yeppi.findonnuri.model;

import com.mongodb.lang.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document("onnuri")
@Getter
@Setter
public class Location {
    public String type;

    @Nullable
    public List<Double> coordinates;
}