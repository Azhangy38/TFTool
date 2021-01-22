package com.example.demo.Model;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Traits {
    private String name;
    private int num_units;
    private int style;              // determines color of trait in-game
    private int tier_current;
    private int tier_total;

}
