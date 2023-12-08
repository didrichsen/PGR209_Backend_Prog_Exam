package com.example.backendexam2023.Service;

import com.example.backendexam2023.DeleteResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SubassemblyService {

    private final SubassemblyRepository subassemblyRepository;
    private final PartService partService;

    private final MachineRepository machineRepository;

    @Autowired
    public SubassemblyService(SubassemblyRepository subassemblyRepository, PartService partService, MachineRepository machineRepository){
        this.subassemblyRepository = subassemblyRepository;
        this.partService = partService;
        this.machineRepository = machineRepository;
    }
    
    public Subassembly getSubassemblyById(Long id){
        return subassemblyRepository.findById(id).orElse(null);
    }

    public List<Subassembly> getSubassembliesPageable(int pageNumber) {
        return subassemblyRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public Subassembly createSubassembly(SubassemblyRequest subassemblyRequest){

        Subassembly subassembly = new Subassembly(subassemblyRequest.getSubassemblyName());

        List<Part> parts = new ArrayList<>();

        for(Long partId : subassemblyRequest.getPartIds()){
            Part part = partService.getPartById(partId);
            parts.add(part);
        }

        subassembly.setParts(parts);

        return subassemblyRepository.save(subassembly);
    }

    public DeleteResult deleteSubassemblyById(Long id){

        List<Machine> machinesToCheck = machineRepository.findAll();
        List<Long> machinesUsingSubassembly = new ArrayList<>();

        boolean isInUse = false;

        Subassembly subassemblyToDelete = getSubassemblyById(id);

        if(subassemblyToDelete == null){
            return new DeleteResult(false, Collections.emptyList());
        }

        for (Machine machine:machinesToCheck) {
            List<Subassembly> subassemblies = machine.getSubassemblies();
            for (Subassembly subassembly : subassemblies) {
                if(Objects.equals(subassembly, subassemblyToDelete)){
                    isInUse = true;
                    machinesUsingSubassembly.add(machine.getMachineId());
                }
            }
        }

        if(isInUse){
            return new DeleteResult(false,machinesUsingSubassembly);
        }

        subassemblyRepository.deleteById(subassemblyToDelete.getSubassemblyId());

        return new DeleteResult(true,Collections.emptyList());
    }

    protected List<Subassembly> getAllSubassemblies() {
        return subassemblyRepository.findAll();
    }




}
