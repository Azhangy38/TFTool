package com.example.demo.Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tactician {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String summonerName;
    private int summonerLevel;
    private String tier;
    private int division;

    @Override
    public String toString() {
        return "Summoner{" +
                "id=" + id +
                ", summonerName='" + summonerName + '\'' +
                ", summonerLevel=" + summonerLevel +
                ", tier=" + tier +
                ", division='" + division + '\'' +
                '}';
    }
}
