package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Util.ResponseHelperDeletionIdArray;
import com.example.backendexam2023.Service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/part")
public class PartController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService){
        this.partService = partService;
    }

    @PostMapping
    public ResponseEntity<Object> createPart(@RequestBody Part part){

        OperationResult<Object> operationResult = partService.createPart(part);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/page/{pageNumber}")
    public List<Part> getPartsByPage(@PathVariable int pageNumber) {
        return partService.getPartsPageable(pageNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Part> getPartById(@PathVariable Long id){
        Part part = partService.getPartById(id);

        if(part == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(part,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePart(@PathVariable Long id){

        DeleteResult deleteResult = partService.deletePartById(id);

        return ResponseHelperDeletionIdArray.getResponseForDelete(deleteResult);

    }

    @PutMapping("/update/{partId}")
    public ResponseEntity<Object> updatePart(@PathVariable Long partId, @RequestBody Part partData){

        OperationResult<Object> operationResult = partService.updatePart(partId,partData);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error:",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

    }


}
