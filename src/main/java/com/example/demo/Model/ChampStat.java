package com.example.demo.Model;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;


@Getter
@Setter
public class ChampStat{
    private String name;
    private int numGames;
    private double avgRank;
}
