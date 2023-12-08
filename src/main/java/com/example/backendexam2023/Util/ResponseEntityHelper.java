package com.example.backendexam2023.Util;

import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.ErrorMessageWithIds;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityHelper {

    public static ResponseEntity<Object> getResponseForDelete(DeleteResult deleteResult) {
        if(deleteResult.ids() == null){
            return new ResponseEntity<>(deleteResult.errorMessage(), HttpStatus.BAD_REQUEST);
        }

        if(!deleteResult.ids().isEmpty()){
            ErrorMessageWithIds errorMessageWithIds = new ErrorMessageWithIds(deleteResult.errorMessage(), deleteResult.ids());
            return new ResponseEntity<>(errorMessageWithIds, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

