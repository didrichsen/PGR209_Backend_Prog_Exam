package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final SubassemblyService subassemblyService;

    @Autowired
    public MachineService(MachineRepository machineRepository, SubassemblyService subassemblyService){
        this.machineRepository = machineRepository;
        this.subassemblyService = subassemblyService;

    }

    public Machine getMachineById(Long id){
        return machineRepository.findById(id).orElse(null);
    }

    public Machine createMachine(MachineRequest machineRequest){
        Machine machine = new Machine(machineRequest.getMachineName(), machineRequest.getPrice());

        List<Subassembly> subassemblies = new ArrayList<>();

        for(Long partId : machineRequest.getSubassemblyIds()){
            Subassembly subassembly = subassemblyService.getSubassemblyById(partId);
            subassemblies.add(subassembly);
        }

        machine.setSubassemblies(subassemblies);

        return machineRepository.save(machine);

    }

    

    public void deleteMachine(Long id){
        machineRepository.deleteById(id);
    }

    




}
