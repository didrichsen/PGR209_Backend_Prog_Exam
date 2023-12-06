package com.example.backendexam2023.Model.Machine;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class MachineRequest {

        private String machineName;
        private Integer price;
        private List<Long> subassemblyIds;

}
