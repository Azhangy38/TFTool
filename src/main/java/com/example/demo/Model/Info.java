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
    private int tft_set_number;
}