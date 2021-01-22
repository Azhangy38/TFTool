package com.example.demo.Response;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummonerIDResponse extends BaseResponseRiotAPI {

    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private String profileIconId;
    private long revisionDate;
    private int summonerLevel;

    @Override
    public String toString() {
        return "SummonerIDResponse{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", puuid='" + puuid + '\'' +
                ", name='" + name + '\'' +
                ", profileIconId='" + profileIconId + '\'' +
                ", revisionDate=" + revisionDate +
                ", summonerLevel=" + summonerLevel +
                '}';
    }



}