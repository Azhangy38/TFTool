package com.example.demo.Response;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class LowEloResponse extends BaseResponseRiotAPI {
    private String leagueId;
    private String queueType;
    private String tier;
    private String rank;
    private String summonerId;
    private String summonerName;
    private int leaguePoints;
    private int wins;
    private int losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    public Boolean hotStreak;
}