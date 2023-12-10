package com.example.backendexam2023.Service;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Part.Part;
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

    public OperationResult<Object> createSubassembly(SubassemblyRequest subassemblyRequest){

        if (subassemblyRequest.getSubassemblyName() == null || subassemblyRequest.getSubassemblyName().trim().isEmpty())
            return new OperationResult<>(false, "subassembly needs name", null);

        Subassembly subassembly = new Subassembly(subassemblyRequest.getSubassemblyName());

        List<Part> parts = new ArrayList<>();

        for(Long partId : subassemblyRequest.getPartIds()){
            Part part = partService.getPartById(partId);
            parts.add(part);
        }
        subassembly.setParts(parts);
        if (subassembly.getParts().isEmpty()){
            return new OperationResult<>(false, "Subassembly has no parts", null);
        }

        return new OperationResult<>(true, null, subassemblyRepository.save(subassembly));
    }

    public DeleteResult deleteSubassemblyById(Long id){

        List<Machine> machinesToCheck = machineRepository.findAll();
        List<Long> machinesUsingSubassembly = new ArrayList<>();

        boolean isInUse = false;

        Subassembly subassemblyToDelete = getSubassemblyById(id);

        if(subassemblyToDelete == null){
            return new DeleteResult(false, Collections.emptyList(), "Couldn't find subassembly " + id);
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
            return new DeleteResult(false,machinesUsingSubassembly, "Subassembly is in use. Cant delete.");
        }

        subassemblyRepository.deleteById(subassemblyToDelete.getSubassemblyId());

        return new DeleteResult(true,Collections.emptyList(), null);
    }

    public OperationResult<Object> updateSubassembly(Long subassemblyId, Subassembly subassemblyData){

        Subassembly subassemblyToUpdate = getSubassemblyById(subassemblyId);

        if (subassemblyToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find any subassembly with id " + subassemblyId, null);
        }

        if (subassemblyData.getSubassemblyName() != null) subassemblyToUpdate.setSubassemblyName(subassemblyData.getSubassemblyName());
        if(subassemblyData.getParts() != null) subassemblyToUpdate.getParts().addAll(subassemblyData.getParts());

        return new OperationResult<>(true, null,subassemblyRepository.save(subassemblyToUpdate));

    }




}
