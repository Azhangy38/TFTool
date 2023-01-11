package com.example.demo.Response;
import com.example.demo.Model.Summoner;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class LowTierResponse {
    private String tier;
    private int division;
    private List<Summoner> summoner;
}
