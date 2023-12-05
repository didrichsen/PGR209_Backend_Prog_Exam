package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/part")
public class PartController {

    private final PartService partService;

    @Autowired
    public PartController(PartService partService){
        this.partService = partService;
    }

    @GetMapping
    public List<Part> getAllParts(){
        return partService.getAllParts();
    }

    @GetMapping("/{id}")
    public Part getPartById(@PathVariable Long id){
        return partService.getPartById(id);
    }

    @PostMapping
    public Part createPart(@RequestBody Part part){
        return partService.createPart(part);
    }
    @DeleteMapping("/{id}")
    public void deletePart(@PathVariable Long id){
        partService.deletePartById(id);
    }
    @PutMapping
    public Part updatePart(@RequestBody Part part){
        return partService.updatePart(part);
    }




}
