package com.example.backendexam2023.Records;

import java.util.List;

public record UpdateRequestSubassembly(String subassemblyName, List<Long> partIds) {
}
