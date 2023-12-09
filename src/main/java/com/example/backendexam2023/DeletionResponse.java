package com.example.backendexam2023;

import java.util.List;

public record DeletionResponse (boolean Success,String message, List<Object> deletedObjects) {
}
