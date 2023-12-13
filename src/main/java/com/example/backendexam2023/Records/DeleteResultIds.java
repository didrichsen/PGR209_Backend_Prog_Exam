package com.example.backendexam2023.Records;

import java.util.List;
public record DeleteResultIds(boolean success, String error, List<Long> related_ids) {
}
