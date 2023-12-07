package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    public List<Machine> getMachinesPageable(int pageNumber) {
        return machineRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public Machine createMachine(MachineRequest machineRequest){
        Machine machine = new Machine(machineRequest.getMachineName(), machineRequest.getPrice());

        List<Subassembly> subassemblies = new ArrayList<>();

        if(machineRequest.getSubassemblyIds().isEmpty()){
            throw new RuntimeException();
        }

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
    public Machine updateMachine(Long machineId, Machine newMachine){
        Machine machineToUpdate = getMachineById(machineId);

        if (machineToUpdate == null) throw new RuntimeException("Could not find machine with id " + machineId);

        if (newMachine.getMachineName() != null) machineToUpdate.setMachineName(newMachine.getMachineName());
        if (newMachine.getPrice() != null) machineToUpdate.setPrice(newMachine.getPrice());
        return machineRepository.save(machineToUpdate);
    }

    




}
