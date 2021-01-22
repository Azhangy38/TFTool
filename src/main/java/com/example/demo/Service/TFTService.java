package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Response.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class TFTService {

    private static final String MATCH_IDS_URI = "https://na1.api.riotgames.com/tft/summoner/v1/summoners/by-name/";
    private static final String PUUID_URI = "https://na1.api.riotgames.com/tft/summoner/v1/summoners/by-puuid/";
    private static final String MATCH_HISTORY = "https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/";
    private static final String MATCH = "https://americas.api.riotgames.com/tft/match/v1/matches/";
    private static final String HIGH_ELO = "https://na1.api.riotgames.com/tft/league/v1/";
    private static final String LOW_ELO = "https://na1.api.riotgames.com/tft/league/v1/entries/";

    private static final String apiKey_temp = "RGAPI-4c518d81-3e9e-4907-b916-8bf0aedc0bcd";

    enum Numeral {
        I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100), CD(400), D(500), CM(900), M(1000);
        int weight;
        Numeral(int weight) {
            this.weight = weight;
        }
    };

    public static String roman(long n) {
        if( n <= 0) {
            throw new IllegalArgumentException();
        }

        StringBuilder buf = new StringBuilder();

        final Numeral[] values = Numeral.values();
        for (int i = values.length - 1; i >= 0; i--) {
            while (n >= values[i].weight) {
                buf.append(values[i]);
                n -= values[i].weight;
            }
        }
        return buf.toString();
    }

    private Client client = ClientBuilder.newClient();

    public PuuidResponse getPuuid() throws IOException {
        PuuidResponse response = client
                .target(PUUID_URI)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(PuuidResponse.class);
        return response;
    }

    public SummonerIDResponse getSummonerID(String name) {
        String searchUrl = MATCH_IDS_URI+name;
        SummonerIDResponse response = client
                .target(searchUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(SummonerIDResponse.class);
        return response;
    }

    public SummonerIDResponse getSummonerName(String puuid) {
        String searchUrl = PUUID_URI+puuid;
        SummonerIDResponse response = client
                .target(searchUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(SummonerIDResponse.class);
        return response;
    }

    public HighEloResponse getHighElo(String division) { // Master and above, division names will need toUpperCase() method
        String highEloUrl = HIGH_ELO + division;
        HighEloResponse response = client
                .target(highEloUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(HighEloResponse.class);
        return response;
    }

    public LowEloResponse[] getLowElo(String tier, int division, int page){
        // change to enum
        String TIER = tier.toUpperCase(Locale.ROOT);
        String numeral = roman(division);

        String lowEloUrl = LOW_ELO+TIER+"/"+numeral+"?page="+page;

        LowEloResponse[] response = client
                .target(lowEloUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(LowEloResponse[].class);

        return response;
    }

    public String[] getMatchHistory(String puuid, int numMatches){
        String matchHistoryURL = MATCH_HISTORY+puuid+"/ids?count="+numMatches; // 20 is the number of matches shown
        String[] response = client
                .target(matchHistoryURL)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(String[].class);
        return response;
    }

    public MatchDetailsResponse getMatchDetails(String matchID){
        String matchURL = MATCH+matchID;
        MatchDetailsResponse response = client
                .target(matchURL)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(MatchDetailsResponse.class);
        return response;
    }

    public void getMatchAnalysis(String matchID){
        MatchDetailsResponse matchDetails = getMatchDetails(matchID);
        Info info = matchDetails.getInfo();
        System.out.print(info.getGame_datetime());
        for (int i = 0; i < info.getParticipants().size(); i++){
            printTraits(info, i);
            printUnits(info, i);
        }

    }

    public void printTraits(Info info, int idx){
        Participants player = info.getParticipants().get(idx);
        System.out.println("\nSummoner: "+ getSummonerName(player.getPuuid()).getName());
        System.out.println("Rank: " + player.getPlacement());
        System.out.println("----------Traits---------");

        for (int j = 0; j < player.getTraits().size(); j++){
            Traits comp = player.getTraits().get(j);
            if (comp.getName().length() > 4){ // maybe a use a try/catch instead
                if (comp.getName().startsWith("Set4_")){		// gets rid of prefix
                    System.out.println(comp.getName().substring(5) + ": "+ comp.getNum_units());
                }
                else{
                    System.out.println(comp.getName() + ": "+ comp.getNum_units());
                }
            }
            else{
                System.out.println(comp.getName() + ": "+ comp.getNum_units());
            }

        }
    }

    public void printUnits(Info info, int idx){
        Participants player = info.getParticipants().get(idx);
        System.out.println("----------Units----------");

        for (int k = 0; k < player.getUnits().size(); k++){
            Units characters = player.getUnits().get(k);
            // gets rid of prefix

            if (characters.getChosen() != null && characters.getChosen().length() > 0){
                if (characters.getChosen().length() > 5){
                    if (characters.getChosen().startsWith("Set4_")){		// gets rid of prefix
                        System.out.println("Chosen "+ characters.getChosen().substring(5) + " "+ characters.getCharacter_id().substring(5) + " " + characters.getTier());
                    }
                    else{
                        System.out.println("Chosen "+ characters.getChosen() + " "+ characters.getCharacter_id().substring(5) + " " + characters.getTier());
                    }
                }

                else{
                    System.out.println("Chosen "+ characters.getChosen() + " "+ characters.getCharacter_id().substring(5) + " " + characters.getTier());
                }
            }
            else{
                System.out.println(characters.getCharacter_id().substring(5) + " " + characters.getTier());
            }
        }
    }

    public HashMap<String, Integer> createChampionMap(HashMap hm){
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("champions.json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray championList = (JSONArray) obj;
            championList.forEach(champ -> parseChampionList((JSONObject) champ, hm));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hm;
    }

    public static void parseChampionList(JSONObject champion, HashMap champCount){
        champCount.put(champion.get("name"), 0); // initialize each champ to 0
//        System.out.println(champion.get("name")+", "+ champCount.get(champion.get("name")));

    }

    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        Collections.reverse(list); // We want the list from high to low

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public List<ChampStat> setAnalysisMap(HashMap<String, Integer> map1, HashMap<String, Integer> map2){
        List<ChampStat> champStatList = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: map1.entrySet()){
            ChampStat champStat = new ChampStat();
            if (entry.getValue() > 0){
                double avgRank = (double) map2.get(entry.getKey())/(double) entry.getValue();
                champStat.setName(entry.getKey());
                champStat.setAvgRank(avgRank);
                champStat.setNumGames(entry.getValue());
                // print statements for test cases
                System.out.println(entry.getKey() + ", Game(s) played: " + entry.getValue() + ", Avg Rank: " + avgRank);
            }else{
                System.out.println(entry.getKey() + ", Game(s) played: " + entry.getValue() + ", Avg Rank: N/A" );
                champStat.setName(entry.getKey());
                champStat.setAvgRank(0);
                champStat.setNumGames(entry.getValue());
            }
            champStatList.add(champStat);
        }
        return champStatList;
    }

    public List<Player> getPlayersList(MatchDetailsResponse matchDetails){
        List <Player> playersList = new ArrayList<>();
        Info info = matchDetails.getInfo();
        int size = matchDetails.getInfo().getParticipants().size();
        for (int i = 0; i < size; i++){
            Player player = new Player();
            Participants participants = info.getParticipants().get(i);
            player.setSummonerName(getSummonerName(participants.getPuuid()).getName());
            player.setRank(participants.getPlacement());
            player.setTraits(participants.getTraits());
            player.setUnits(participants.getUnits());
            playersList.add(player);
        }
        return playersList;
    }

    public List<Summoner> getLowSummonersList(LowEloResponse[] response){
        List<Summoner> summonerList = new ArrayList<>();
        for (int i = 0; i < response.length; i++){
            Summoner summoner = new Summoner();
            summoner.setName(response[i].getSummonerName());
            summoner.setLeaguePoints(response[i].getLeaguePoints());
            summonerList.add(summoner);
        }
        return summonerList;
    }

    public List<Summoner> getHighSummonersList(HighEloResponse response){
        List<Summoner> summonerList = new ArrayList<>();
        for (int i = 0; i < response.getEntries().size(); i++){
            Summoner summoner = new Summoner();
            Entry entry = response.getEntries().get(i);
            summoner.setName(entry.getSummonerName());
            summoner.setLeaguePoints(entry.getLeaguePoints());
            summonerList.add(summoner);
        }
        return summonerList;
    }

    public void updateMap(HashMap<String, Integer> map1, HashMap<String, Integer> map2, Info info, int idx){
        Participants player = info.getParticipants().get(idx);
        for (int k = 0; k < player.getUnits().size(); k++){
            Units characters = player.getUnits().get(k);
            String key = characters.getCharacter_id().substring(5);  // substring is to get rid of prefix
            if (map1.containsKey(key)){
                map1.put(key, map1.get(key)+1);
                map2.put(key, map2.get(key)+player.getPlacement());
            }
        }
    }

    public void analyzeChampionStats(SummonerIDResponse summoner,
                                   String[] matches, HashMap champCount,
                                   HashMap avgRank){
        int idx = 0;
        for (int i = 0; i < matches.length; i++){
            MatchDetailsResponse matchDetails = getMatchDetails(matches[i]);
            Info info = matchDetails.getInfo();
            for (int j = 0; j < info.getParticipants().size(); j++){    // s
                if (info.getParticipants().get(j).getPuuid() == summoner.getPuuid()){
                    idx = j;
                }
            }
            updateMap(champCount, avgRank, info, idx);
        }

        HashMap<String, Integer> sorted = sortByValue(champCount);
        setAnalysisMap(sorted, avgRank);
    }


}