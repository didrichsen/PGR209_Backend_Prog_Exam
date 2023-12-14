package com.example.backendexam2023.Records;

import java.util.List;

public record UpdateRequestMachine(String machineName, int price, List<Long> subassemblyIds) { }
