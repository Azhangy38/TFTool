package com.example.demo.Response;

import com.example.demo.Model.ChampStat;
import com.example.demo.Model.Entry;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter

public class HighEloResponse extends BaseResponseRiotAPI {
    private String tier;
    private String leagueId;
    private String queue;
    private String name;
    private List<Entry> entries;

}
