package com.example.demo.Response;
import com.example.demo.Response.BaseResponseRiotAPI;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class PuuidResponse extends BaseResponseRiotAPI {

    private String puuid;
    private String gameName;
    private String tagLine;

}