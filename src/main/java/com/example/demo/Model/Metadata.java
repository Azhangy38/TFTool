package com.example.demo.Model;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class Metadata {
    private int data_version;
    private String match_id;
    private String[] participants;

}