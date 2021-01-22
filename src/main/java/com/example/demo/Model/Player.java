package com.example.demo.Model;

import lombok.Setter;
import lombok.Getter;
import java.util.List;

@Getter
@Setter
public class Player {
    private String summonerName;
    private int rank;
    private List<Traits> traits;
    private List<Units> units;
}
