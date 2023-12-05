package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Machine;
import com.example.backendexam2023.Service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/machine")
public class MachineController {

    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService){
        this.machineService = machineService;
    }

    @PostMapping
    public Machine createMachine(@RequestBody Machine machine){
        return machineService.createMachine(machine);
    }

    @GetMapping("/{id}")
    public Machine getMachineById(@PathVariable Long id){
        return machineService.getMachineById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteVet(@PathVariable Long id){
        machineService.deleteMachine(id);
    }








}
