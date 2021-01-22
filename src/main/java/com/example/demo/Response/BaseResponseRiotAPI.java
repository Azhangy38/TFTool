package com.example.demo.Response;
import com.example.demo.Model.Status;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class BaseResponseRiotAPI {
    private Status status;
}