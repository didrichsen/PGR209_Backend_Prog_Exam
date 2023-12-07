package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Service.SubassemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subassembly")
public class SubassemblyController {

    private final SubassemblyService subassemblyService;

    @Autowired
    public SubassemblyController(SubassemblyService subassemblyService){
        this.subassemblyService = subassemblyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subassembly> getSubassemblyById(@PathVariable Long id){
        Subassembly subassembly = subassemblyService.getSubassemblyById(id);

        if(subassembly == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(subassembly,HttpStatus.OK);

    }
    @GetMapping("/page/{pageNumber}")
    public List<Subassembly> getSubassembliesByPage(@PathVariable int pageNumber) {
        return subassemblyService.getSubassembliesPageable(pageNumber);
    }

    @PostMapping
    public Subassembly createSubassembly(@RequestBody SubassemblyRequest subassemblyRequest){
        return subassemblyService.createSubassembly(subassemblyRequest);
    }



    @DeleteMapping("/{id}")
    public void deleteSubassembly(@PathVariable Long id){
        subassemblyService.deleteSubassembly(id);
    }



}
