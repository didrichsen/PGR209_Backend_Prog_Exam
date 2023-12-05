package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Machine;
import com.example.backendexam2023.Model.Subassembly;
import com.example.backendexam2023.Service.MachineService;
import com.example.backendexam2023.Service.SubassemblyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public class SubassemblyController {

    private final SubassemblyService subassemblyService;

    @Autowired
    public SubassemblyController(SubassemblyService subassemblyService){
        this.subassemblyService = subassemblyService;
    }

    @PostMapping
    public Subassembly createSubassembly(@RequestBody Subassembly subassembly){
        return subassemblyService.createSubassembly(subassembly);
    }

    @GetMapping("/{id}")
    public Subassembly getSubassemblyById(@PathVariable Long id){
        return subassemblyService.getSubassembly(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSubassembly(@PathVariable Long id){
        subassemblyService.deleteSubassembly(id);
    }



}
