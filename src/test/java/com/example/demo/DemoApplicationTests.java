package com.example.demo;

import com.example.demo.Response.HighEloResponse;
import com.example.demo.Response.LowEloResponse;
import com.example.demo.Response.MatchDetailsResponse;
import com.example.demo.Response.SummonerIDResponse;
import com.example.demo.Service.TFTService;
import com.example.demo.Model.Entry;
import com.example.demo.Model.Info;
import com.example.demo.Model.Metadata;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class DemoApplicationTests {

	@Test
	void testHighEloList(){
		TFTService test = new TFTService();

		HighEloResponse response = test.getHighElo("challenger");
		List<Entry> challenger = response.getEntries();
		assertNotNull(response);
		for (int i = 0; i < challenger.size(); i++){ // maybe use a for each loop
			System.out.println(challenger.get(i).getSummonerName() + ", " + challenger.get(i).getLeaguePoints()+", " + i);
		}
	}

	@Test
	void testLowEloList(){
		TFTService test = new TFTService();
		// has to be between 1 and 4
		LowEloResponse[] response = test.getLowElo("gold", 4, 1);
		assertNotNull(response);
		for (int i = 0; i < response.length; i++){ // maybe use a for each loop
			System.out.println(response[i].getSummonerName() + ", " + response[i].getLeaguePoints()+", " + i);
		}
	}

	@Test
	void testSummonerName(){
		TFTService test = new TFTService();
		SummonerIDResponse summoner = test.getSummonerID("SpicyN0odlez");
		assertEquals("SpicyN0odlez", summoner.getName());
	}

	@Test
	void testMatchHistory(){
		TFTService test = new TFTService();
		SummonerIDResponse summoner = test.getSummonerID("SpicyN0odlez");
		System.out.println("name: "+ summoner.getName());
		System.out.println("puuid: "+ summoner.getPuuid());
		String[] matches = test.getMatchHistory(summoner.getPuuid(), 20);

		int j;
		for (int i = 0; i < matches.length; i++){
			j = i+1;
			MatchDetailsResponse matchDetails = test.getMatchDetails(matches[i]);
			Info info = matchDetails.getInfo();
			System.out.println("Match #" + j + ": " + matches[i] + ", Date: " + info.getGame_datetime());
		}

		assertNotNull(matches);
		assertTrue(matches.length > 0);
	}

	@Test
	void testMatchDetails(){ // irrelevant test, shortened version of one below
		TFTService test = new TFTService();
		SummonerIDResponse summoner = test.getSummonerID("goroutine");
		String[] matches = test.getMatchHistory(summoner.getPuuid(), 20);
		MatchDetailsResponse matchDetails = test.getMatchDetails(matches[19]);
		Metadata meta = matchDetails.getMetadata();
		Info info = matchDetails.getInfo();

		assertEquals(matches[19], meta.getMatch_id());
		for (int i = 0; i < info.getParticipants().size(); i++){
			String puuid = info.getParticipants().get(i).getPuuid();
			System.out.println("#"+ info.getParticipants().get(i).getPlacement() + ": "+ test.getSummonerName(puuid).getName());
		}
	}

	@Test
	void testRecentMatchDetails(){ // irrelevant test, shortened version of one below
		TFTService test = new TFTService();
		SummonerIDResponse summoner = test.getSummonerID("goroutine");
		String[] matches = test.getMatchHistory(summoner.getPuuid(), 20);
		for (int i = 0; i < 20; i++){
			MatchDetailsResponse matchDetails = test.getMatchDetails(matches[i]);
			Metadata meta = matchDetails.getMetadata();
			assertEquals(matches[i], meta.getMatch_id());
		}

	}

	@Test
	void testGameAnalysis(){
		TFTService tft = new TFTService();
		SummonerIDResponse summoner = tft.getSummonerID("SpicyN0odlez");
		String[] matches = tft.getMatchHistory(summoner.getPuuid(), 20);
		MatchDetailsResponse matchDetails = tft.getMatchDetails(matches[0]);
		Metadata meta = matchDetails.getMetadata();
		assertEquals(matches[0], meta.getMatch_id());
		tft.getMatchAnalysis(matches[0]);
	}

	@Test
	void testPlayerAnalysis(){			 // player history analysis
		TFTService tft = new TFTService();
		HashMap<String, Integer> champCount = new HashMap<>();
		HashMap<String, Integer> avgRank = new HashMap<>();
		tft.createChampionMap(champCount);
		tft.createChampionMap(avgRank);
		SummonerIDResponse summoner = tft.getSummonerID("Andy Zhou");
		String[] matches = tft.getMatchHistory(summoner.getPuuid(), 30);
		assertNotNull(matches[0]);
		System.out.println("Summoner: " + summoner.getName());
		tft.analyzeChampionStats(summoner, matches, champCount, avgRank);

	}




}
