package com.example.demo.Model;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Entry extends BaseResponseRiotAPI {
    private String summonerId;
    private String summonerName;
    private int leaguePoints;
    private String rank;
    private int wins;
    private int losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    public Boolean hotStreak;
}
