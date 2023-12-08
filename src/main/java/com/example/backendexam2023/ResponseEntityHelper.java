package com.example.backendexam2023;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityHelper {

    public static ResponseEntity<List<Long>> createResponseEntity(DeleteResult deleteResult) {
        if (!deleteResult.getIdsInUse().isEmpty()) {
            List<Long> listOfIdsUsingPart = deleteResult.getIdsInUse();
            return new ResponseEntity<>(listOfIdsUsingPart, HttpStatus.CONFLICT);
        }

        if (!deleteResult.isDeletable()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

