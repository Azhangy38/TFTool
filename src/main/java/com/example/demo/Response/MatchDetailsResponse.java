package com.example.demo.Response;

import com.example.demo.Model.Info;
import com.example.demo.Model.Metadata;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class MatchDetailsResponse {

    private Metadata metadata;
    private Info info;

}