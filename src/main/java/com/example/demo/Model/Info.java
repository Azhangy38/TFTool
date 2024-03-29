package com.example.demo.Model;

import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class Info {
    private long game_datetime;
    private double game_length;
    private String game_version;
    private List<Participants> participants;
    private int queue_id;
    private String tft_mode;
    private String tft_game_type;
    private String tft_set_core_name;
    private int tft_set_number;
}