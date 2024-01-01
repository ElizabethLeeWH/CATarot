package com.catarot.server.models;

// import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tarot {
    private String name;
    private Integer number;
    private String arcana;
    private String suit;
    private String meaningUp;
    private String meaningRev;
    private String desc;
    private String img;

}
