package com.example.backendexam2023.Model.Subassembly;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubassemblyRequest {

    private String subassemblyName;
    private List<Long> partIds = new ArrayList<>();

}
