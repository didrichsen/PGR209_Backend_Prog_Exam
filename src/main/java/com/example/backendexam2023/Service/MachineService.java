package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Machine;
import com.example.backendexam2023.Repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository){
        this.machineRepository = machineRepository;
    }

    public Machine createMachine(Machine machine){
        return machineRepository.save(machine);
    }

    public Machine getMachineById(Long id){
        return machineRepository.findById(id).orElse(null);
    }

    public void deleteMachine(Long id){
        machineRepository.deleteById(id);
    }

    




}
