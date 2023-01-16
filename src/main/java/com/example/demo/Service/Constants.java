package com.example.demo.Service;

/**
 * Contains web addresses of api calls
 */
public interface Constants {
    String MATCH_IDS_URI = "https://na1.api.riotgames.com/tft/summoner/v1/summoners/by-name/";
    String PUUID_URI = "https://na1.api.riotgames.com/tft/summoner/v1/summoners/by-puuid/";
    String MATCH_HISTORY = "https://americas.api.riotgames.com/tft/match/v1/matches/by-puuid/";
    String MATCH = "https://americas.api.riotgames.com/tft/match/v1/matches/";
    String HIGH_ELO = "https://na1.api.riotgames.com/tft/league/v1/";
    String LOW_ELO = "https://na1.api.riotgames.com/tft/league/v1/entries/";
    String apiKey_temp = "RGAPI-67b595ef-6662-4676-9668-bb8f95b72ead"; // REDACTED, replace with your own apikey if you want to try this

    enum Numeral {
        I(1), IV(4), V(5), IX(9), X(10), XL(40), L(50), XC(90), C(100), CD(400), D(500), CM(900), M(1000);
        int weight;

        Numeral(int weight) {
            this.weight = weight;
        }
    }


}
