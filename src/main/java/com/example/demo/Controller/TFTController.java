package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.demo.Model.*;
import com.example.demo.Queries.GameQuery;
import com.example.demo.Queries.LowDivisionQuery;
import com.example.demo.Response.*;
import com.example.demo.Service.TFTService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class TFTController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Inject
    private TFTService tftService;

    @GetMapping("/matchHistory")
    public String[] getMatchHistory(@RequestParam String summonerID) {
        SummonerIDResponse summoner = tftService.getSummonerID(summonerID);
        String[] matches = tftService.getMatchHistory(summoner.getPuuid(), 20);
        return matches;
    }

    @GetMapping("/highEloList") // No Challenger or GM players
    public HighTierResponse highTierList(@RequestParam String tier){
        HighTierResponse response = new HighTierResponse();
        HighEloResponse eloResponse = tftService.getHighElo(tier);
        List<Summoner> summonersList = tftService.getHighSummonersList(eloResponse);
        response.setTier(tier);
        response.setSummoner(summonersList);
        return response;
    }

    @GetMapping("/matchHistoryAnalysis")
    public MatchHistoryAnalysisResponse getMatchHistoryAnalysis(@RequestParam String summonerID) {
        SummonerIDResponse summoner = tftService.getSummonerID(summonerID);
        HashMap<String, Integer> champCount = new HashMap<>();
        HashMap<String, Integer> avgRank = new HashMap<>();
        tftService.createChampionMap(champCount);
        tftService.createChampionMap(avgRank);
        String[] matches = tftService.getMatchHistory(summoner.getPuuid(), 30);
        tftService.analyzeChampionStats(summoner, matches, champCount, avgRank);
        List<ChampStat> champStatList = tftService.setAnalysisMap(champCount, avgRank);
        MatchHistoryAnalysisResponse response = new MatchHistoryAnalysisResponse();
        response.setSummonerName(summoner.getName());
        response.setChampStats(champStatList);
        return response;
    }

    @GetMapping("/gameDetails1") // made irrelevant by the POST below
    public GameDetailsResponse getGameDetails(@RequestParam String summonerID){
        SummonerIDResponse summoner = tftService.getSummonerID(summonerID);
        String[] matches = tftService.getMatchHistory(summoner.getPuuid(), 20);
        MatchDetailsResponse matchDetails = tftService.getMatchDetails(matches[0]);
        List<Player> playersList = tftService.getPlayersList(matchDetails);
        //playersList.set
        GameDetailsResponse response = new GameDetailsResponse();
        response.setMatchID(matches[0]);
        response.setPlayers(playersList);
        return response;
    }

    @PostMapping("/gameDetails2")
    public GameDetailsResponse gameDetails(@RequestBody GameQuery gameQuery){
        SummonerIDResponse summoner = tftService.getSummonerID(gameQuery.getSummonerName());
        String[] matches = tftService.getMatchHistory(summoner.getPuuid(), gameQuery.getNumMatches());
        MatchDetailsResponse matchDetails = tftService.getMatchDetails(matches[gameQuery.getMatchIdx()]);
        List<Player> playersList = tftService.getPlayersList(matchDetails);
        //playersList.set
        GameDetailsResponse response = new GameDetailsResponse();
        response.setMatchID(matches[0]);
        response.setPlayers(playersList);
        return response;
    }

    @PostMapping("/lowEloList")
    public LowTierResponse lowTierList(@RequestBody LowDivisionQuery lowDivisionQuery){
        LowTierResponse response = new LowTierResponse();
        LowEloResponse[] eloResponse = tftService.getLowElo(lowDivisionQuery.getTier(), lowDivisionQuery.getDivision(), lowDivisionQuery.getPage());
        List<Summoner> summonersList = tftService.getLowSummonersList(eloResponse);
        response.setDivision(lowDivisionQuery.getDivision());
        response.setTier(lowDivisionQuery.getTier());
        response.setPage(lowDivisionQuery.getPage());
        response.setSummoner(summonersList);
        return response;
    }



}
