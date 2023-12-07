package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.OrderBatch.OrderBatch;
import com.example.backendexam2023.Service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machine")
public class MachineController {

    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService){
        this.machineService = machineService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> getMachineById(@PathVariable Long id){
        Machine machine = machineService.getMachineById(id);

        if (machine == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(machine, HttpStatus.OK);

    }

    @GetMapping("/page/{pageNumber}")
    public List<Machine> getMachinesByPage(@PathVariable int pageNumber) {
        return machineService.getMachinesPageable(pageNumber);
    }

    @PostMapping
    public Machine createMachine(@RequestBody MachineRequest machineRequest){
        return machineService.createMachine(machineRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteMachine(@PathVariable Long id){
        machineService.deleteMachine(id);
    }








}
