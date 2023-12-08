package com.example.backendexam2023.Records;

import java.util.List;

public record ErrorMessageWithIds(String errorMessage, List<Long> id) {
}
