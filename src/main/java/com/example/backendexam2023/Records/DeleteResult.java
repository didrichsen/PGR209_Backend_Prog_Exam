package com.example.backendexam2023.Records;

import java.util.List;
public record DeleteResult(boolean success, List<Long> ids, String errorMessage) {
}
