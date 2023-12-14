package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Records.DeleteResultIds;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/machine")
public class MachineController {

    private final MachineService machineService;

    @Autowired
    public MachineController(MachineService machineService){
        this.machineService = machineService;
    }

    @PostMapping
    public ResponseEntity<Object> createMachine(@RequestBody MachineRequest machineRequest){

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMachineById(@PathVariable Long id){

        DeleteResultIds deleteResult = machineService.deleteMachineById(id);

        if(deleteResult.success()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(deleteResult,HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update/{machineId}")
    public ResponseEntity<Object> updateMachine(@PathVariable Long machineId, @RequestBody MachineRequest machineData){

        OperationResult<Object> operationResult = machineService.updateMachine(machineId,machineData);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error:",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

    }

}







