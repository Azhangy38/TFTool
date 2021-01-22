package com.example.demo.Response;
import com.example.demo.Model.Summoner;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter
@Setter
public class HighTierResponse {
    private String tier;
    private List<Summoner> summoner;
}