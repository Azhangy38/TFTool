package com.example.demo.Model;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Units {
    private String character_id;
    private String chosen;
    private String[] itemNames;
    private int[] items;
    private String name;
    private int rarity;
    private int tier;

}

