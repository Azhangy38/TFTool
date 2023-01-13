package com.example.demo.Controller;

import java.util.Collections;
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
    public String[] getMatchHistory(@RequestParam String summonerId) {
        SummonerIDResponse summoner = tftService.getSummonerID(summonerId);
        String[] matches = tftService.getMatchHistory(summoner.getPuuid(), 20);
        return matches;
    }

    @GetMapping("/highEloList")
    public HighTierResponse highTierList(@RequestParam String tier){
        HighTierResponse response = new HighTierResponse();
        HighEloResponse eloResponse = tftService.getHighElo(tier);
        List<Summoner> summonersList = tftService.getHighSummonersList(eloResponse);
        summonersList.sort((s1,s2) -> s2.getLeaguePoints()-s1.getLeaguePoints());
        response.setTier(tier);
        response.setSummoner(summonersList);
        return response;
    }

    @PostMapping("/lowEloList")
    public LowTierResponse lowTierList(@RequestBody LowDivisionQuery lowDivisionQuery){
        // might have to had pages back in
        LowTierResponse response = new LowTierResponse();
        LowEloResponse[] eloResponse = tftService.getLowElo(lowDivisionQuery.getTier(), lowDivisionQuery.getDivision());
        List<Summoner> summonersList = tftService.getLowSummonersList(eloResponse);
        summonersList.sort((s1,s2) -> s2.getLeaguePoints() - s1.getLeaguePoints());
        response.setDivision(lowDivisionQuery.getDivision());
        response.setTier(lowDivisionQuery.getTier());
        response.setSummoner(summonersList);
        return response;
    }

    @GetMapping("/matchHistoryAnalysis")
    public MatchHistoryAnalysisResponse getMatchHistoryAnalysis(@RequestParam String summonerId) {
        SummonerIDResponse summoner = tftService.getSummonerID(summonerId);
        HashMap<String, Integer> champCount = new HashMap<>();
        HashMap<String, Integer> avgRank = new HashMap<>();
        //tftService.createChampionMap(champCount);
        //tftService.createChampionMap(avgRank);
        String[] matches = tftService.getMatchHistory(summoner.getPuuid(), 20);
        tftService.analyzeChampionStats(summoner, matches, champCount, avgRank);
        List<ChampStat> champStatList = tftService.setAnalysisMap(champCount, avgRank);
        champStatList.sort((c1,c2) -> c2.getNumGames() - c1.getNumGames());
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





}
