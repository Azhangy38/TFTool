package com.example.demo.Response;

import com.example.demo.Model.ChampStat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchHistoryAnalysisResponse{
    private String summonerName;
    private List<ChampStat> champStats;
}
