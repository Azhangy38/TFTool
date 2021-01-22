package com.example.demo.Response;

import com.example.demo.Model.Player;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter

public class GameDetailsResponse {
    private String matchID;
    private List<Player> players;
}
