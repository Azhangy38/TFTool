package com.example.demo.Model;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Units {
    private String character_id;
    private String chosen;  // this might cause an error as the chosen variable only appears if the unit is chosen
    private int[] items;
    private String name;
    private int rarity;
    private int tier;

}

