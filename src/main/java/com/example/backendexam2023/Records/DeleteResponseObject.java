package com.example.backendexam2023.Records;

import java.util.List;

public record DeleteResponseObject(boolean Success, String message, List<Object> deletedObjects) {
}
