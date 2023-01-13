package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Response.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import static com.example.demo.Service.Constants.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class TFTService{
    public static String roman(long n) {
        if (n <= 0) {
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

    private final Client client = ClientBuilder.newClient();

    /**
     * Each response returns the data from Riot's endpoint
     */
    public SummonerIDResponse getSummonerID(String name) {
        String searchUrl = MATCH_IDS_URI + name;
        SummonerIDResponse response = client
                .target(searchUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(SummonerIDResponse.class);
        return response;
    }

    public SummonerIDResponse getSummonerName(String puuid) {
        String searchUrl = PUUID_URI + puuid;
        SummonerIDResponse response = client
                .target(searchUrl)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(SummonerIDResponse.class);
        return response;
    }

    public HighEloResponse getHighElo(String division) { // Master and above, division names will need toUpperCase() method
        String highEloUrl = HIGH_ELO + division.toLowerCase()+"?api_key="+apiKey_temp;
        HighEloResponse response = client
                .target(highEloUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(HighEloResponse.class);
        return response;
    }

    public LowEloResponse[] getLowElo(String tier, int division) {
        String TIER = tier.toUpperCase();
        String numeral = roman(division);

        String lowEloUrl = LOW_ELO + TIER + "/" + numeral + "?api_key="+apiKey_temp;

        LowEloResponse[] response = client
                .target(lowEloUrl)
                .request(MediaType.APPLICATION_JSON)
                .get(LowEloResponse[].class);

        return response;
    }

    /**
     * Input: summoner's puuid and the number of matches that we want to look at
     * @returns a list of matchID's that pertain to the most recent matches
     */
    public String[] getMatchHistory(String puuid, int numMatches) {
        String matchHistoryURL = MATCH_HISTORY + puuid + "/ids?count=" + numMatches;
        String[] response = client
                .target(matchHistoryURL)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(String[].class);
        return response;
    }

    /**
     * Input: MatchID of the specific game we want to analyze
     * @returns placements and details regarding the results of the match
     */
    public MatchDetailsResponse getMatchDetails(String matchID) {
        String matchURL = MATCH + matchID;
        MatchDetailsResponse response = client
                .target(matchURL)
                .request(MediaType.APPLICATION_JSON)
                .header("X-Riot-Token", apiKey_temp)
                .get(MatchDetailsResponse.class);
        return response;
    }

    /**
     * Input: MatchID of the specific game we want to analyze
     * @returns prints the units and traits that were played by each of the participants
     */
    public void getMatchAnalysis(String matchID) {
        MatchDetailsResponse matchDetails = getMatchDetails(matchID);
        Info info = matchDetails.getInfo();
        System.out.print(info.getGame_datetime());
        for (int i = 0; i < info.getParticipants().size(); i++) {
            printTraits(info, i);
            printUnits(info, i);
        }

    }

    /**
     * Input: info taken from matchDetails and the index of each participant
     * Function: Called by getMatchAnalysis and is used to print the to the terminal
     */
    public void printTraits(Info info, int idx) {
        Participants player = info.getParticipants().get(idx);
        System.out.println("\nSummoner: " + getSummonerName(player.getPuuid()).getName());
        System.out.println("Rank: " + player.getPlacement());
        System.out.println("----------Traits---------");

        for (int j = 0; j < player.getTraits().size(); j++) {
            Traits comp = player.getTraits().get(j);
            if (comp.getName().length() > 4) { // maybe a use a try/catch instead
                if (comp.getName().startsWith("Set")) {        // gets rid of prefix
                    System.out.println(comp.getName().substring(5) + ": " + comp.getNum_units());
                } else {
                    System.out.println(comp.getName() + ": " + comp.getNum_units());
                }
            } else {
                System.out.println(comp.getName() + ": " + comp.getNum_units());
            }

        }
    }

    /**
     * Input: info taken from matchDetails and the index of each participant
     * Function: Called by getMatchAnalysis and is used to print the to the terminal
     */
    public void printUnits(Info info, int idx) {
        Participants player = info.getParticipants().get(idx);
        System.out.println("----------Units----------");

        for (int k = 0; k < player.getUnits().size(); k++) {
            Units characters = player.getUnits().get(k);
            // gets rid of prefix

            if (characters.getChosen() != null && characters.getChosen().length() > 0) {
                if (characters.getChosen().length() > 5) {
                    if (characters.getChosen().startsWith("Set")) {        // gets rid of prefix
                        System.out.println("Chosen " + characters.getChosen().substring(5) + " " + characters.getCharacter_id().substring(5) + " " + characters.getTier());
                    } else {
                        System.out.println("Chosen " + characters.getChosen() + " " + characters.getCharacter_id().substring(5) + " " + characters.getTier());
                    }
                } else {
                    System.out.println("Chosen " + characters.getChosen() + " " + characters.getCharacter_id().substring(5) + " " + characters.getTier());
                }
            } else {
                System.out.println(characters.getCharacter_id().substring(5) + " " + characters.getTier());
            }
        }
    }

    /**
     * Input: Hashmap to store all the current units in the game
     * @returns a hashmap that contains each champion as a key
     */
    public HashMap<String, Integer> createChampionMap(HashMap hm) { // NOT CURRENTLY BEING USED
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("StaticData.json")) {
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

    /**
     * Input: Json file containing list of units and the hashmap that contains all units
     * Function: sets the values of all the keys to 0
     */
    public static void parseChampionList(JSONObject champion, HashMap champCount) {
        champCount.put(champion.get("apiName"), 0);
    }

    /**
     * Input: Hashmap to sort
     * @returns a hashmap that is sorted by the values
     */
    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));
        Collections.reverse(list); // We want the list from high to low

        // put data from sorted list to hashmap
        HashMap<String, Integer> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sorted.put(entry.getKey(), entry.getValue());
        }
        return sorted;
    }

    /**
     * Input: A hashmap that contains all the games played with a unit and a second hashmap that calculates the avgRank with each unit
     * @returns an arraylist that has all the stats from both hashmaps
     */
    public List<ChampStat> setAnalysisMap(HashMap<String, Integer> map1, HashMap<String, Integer> map2) {
        List<ChampStat> champStatList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map1.entrySet()) {
            ChampStat champStat = new ChampStat();
            if (entry.getValue() > 0) {
                double avgRank = (double) map2.get(entry.getKey()) / (double) entry.getValue();
                champStat.setName(entry.getKey());
                champStat.setAvgRank(avgRank);
            } else {
                champStat.setName(entry.getKey());
                champStat.setAvgRank(0);
            }
            champStat.setNumGames(entry.getValue());
            champStatList.add(champStat);
        }
        return champStatList;
    }

    /**
     * Input: details of match each player
     * @returns An arraylist that stores each player and their specified stats
     */
    public List<Player> getPlayersList(MatchDetailsResponse matchDetails) {
        List<Player> playersList = new ArrayList<>();
        Info info = matchDetails.getInfo();
        int size = matchDetails.getInfo().getParticipants().size();
        for (int i = 0; i < size; i++) {
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

    /**
     * Input: List of LowEloPlayers
     * @returns the list of LowEloPlayers with their lp
     */
    public List<Summoner> getLowSummonersList(LowEloResponse[] response) {
        List<Summoner> summonerList = new ArrayList<>();
        for (int i = 0; i < response.length; i++) {
            Summoner summoner = new Summoner();
            summoner.setName(response[i].getSummonerName());
            summoner.setLeaguePoints(response[i].getLeaguePoints());
            summonerList.add(summoner);
        }
        return summonerList;
    }

    /**
     * Input: List of HighEloPlayers
     * @returns the list of HighEloPlayers with their lp
     */
    public List<Summoner> getHighSummonersList(HighEloResponse response) {
        List<Summoner> summonerList = new ArrayList<>();
        for (int i = 0; i < response.getEntries().size(); i++) {
            Summoner summoner = new Summoner();
            Entry entry = response.getEntries().get(i);
            summoner.setName(entry.getSummonerName());
            summoner.setLeaguePoints(entry.getLeaguePoints());
            summonerList.add(summoner);
        }
        return summonerList;
    }

    /**
     * Input: champCount hm, avgRank hm, info, and player index
     * Function: Updates the values in both hash maps given the same keys
     */
    public void updateMap(HashMap<String, Integer> countMap, HashMap<String, Integer> rankMap, Info info, int idx) {
        Participants player = info.getParticipants().get(idx);
        for (int k = 0; k < player.getUnits().size(); k++) {
            Units characters = player.getUnits().get(k);
            String key = characters.getCharacter_id().substring(5); // gets rid of the TFT_prefix
            if (countMap.containsKey(key)) {
                countMap.put(key, countMap.get(key) + 1);
                rankMap.put(key, rankMap.get(key) + player.getPlacement());
            }
            else{
                countMap.put(key, 1);
                rankMap.put(key, player.getPlacement());
            }
        }
    }

    /**
     * Input: summoner, matches, champCount, avgRank
     * Function: Creates the arraylist that contains the analysis of a players' match history
     */
    public void analyzeChampionStats(SummonerIDResponse summoner,
                                     String[] matches, HashMap champCount,
                                     HashMap avgRank) {
        int idx = 0;
        for (int i = 0; i < matches.length; i++) {
            MatchDetailsResponse matchDetails = getMatchDetails(matches[i]);
            Info info = matchDetails.getInfo();
            for (int j = 0; j < info.getParticipants().size(); j++) {
                if (info.getParticipants().get(j).getPuuid().equals(summoner.getPuuid())) {
                    idx = j;
                }
            }
            updateMap(champCount, avgRank, info, idx);
        }
        HashMap<String, Integer> sorted = sortByValue(champCount);
        setAnalysisMap(sorted, avgRank);
    }


}
