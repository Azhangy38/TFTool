package com.example.demo.Model;

import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class Participants {
    private List<String> augments;
    private Companion companion;
    private int gold_left;
    private int last_round;
    private int level;
    private int partner_group_id;
    private int placement;
    private int players_eliminated;
    private String puuid;
    private double time_eliminated;
    private int total_damage_to_players;
    private List<Traits> traits;
    private List<Units> units;


}