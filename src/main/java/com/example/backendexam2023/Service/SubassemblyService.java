package com.example.backendexam2023.Service;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Records.DeleteResultIds;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubassemblyService {

    private final SubassemblyRepository subassemblyRepository;
    private final PartRepository partRepository;

    private final MachineRepository machineRepository;

    @Autowired
    public SubassemblyService(SubassemblyRepository subassemblyRepository, PartRepository partRepository, MachineRepository machineRepository){
        this.subassemblyRepository = subassemblyRepository;
        this.partRepository = partRepository;
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
            Part part = partRepository.findById(partId).orElse(null);
            if(part == null){
                return new OperationResult<>(false, "Couldn't find part with id " + partId, null);
            }

            if(partRepository.isPartInUse(partId)){
                return new OperationResult<>(false, "Part with id of  " +partId + " is already in use.", null);
            }

            parts.add(part);
        }
        subassembly.setParts(parts);
        if (subassembly.getParts().isEmpty()){
            return new OperationResult<>(false, "Subassembly has no parts", null);
        }

        return new OperationResult<>(true, null, subassemblyRepository.save(subassembly));
    }

    public DeleteResultIds deleteSubassemblyById(Long id){

        List<Machine> machinesToCheck = machineRepository.findAll();
        List<Long> machinesUsingSubassembly = new ArrayList<>();

        boolean isInUse = false;

        Subassembly subassemblyToDelete = getSubassemblyById(id);

        if(subassemblyToDelete == null){
            return new DeleteResultIds(false,  "Couldn't find subassembly " + id, null);
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
            return new DeleteResultIds(false, "Subassembly is in use. Can't delete.",machinesUsingSubassembly);
        }

        subassemblyRepository.deleteById(subassemblyToDelete.getSubassemblyId());

        return new DeleteResultIds(true, null,null);
    }

    public OperationResult<Object> updateSubassembly(Long subassemblyId, SubassemblyRequest subassemblyData){

        Subassembly subassemblyToUpdate = getSubassemblyById(subassemblyId);

        if (subassemblyToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find any subassembly with id " + subassemblyId, null);
        }

        if (subassemblyData.getSubassemblyName() != null && !subassemblyData.getSubassemblyName().trim().isEmpty()) subassemblyToUpdate.setSubassemblyName(subassemblyData.getSubassemblyName());
        if (subassemblyData.getPartIds() != null && !subassemblyData.getPartIds().isEmpty()){

            boolean isInUse = subassemblyData.getPartIds().stream()
                    .anyMatch(partRepository::isPartInUse);


            if(isInUse){
                return new OperationResult<>(false,"Part is in use.", null);
            }

            List<Part> parts = subassemblyData.getPartIds()
                    .stream()
                    .map(partRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toCollection(ArrayList::new));

            subassemblyToUpdate.setParts(parts);

        }

        Subassembly updatedSubassembly = subassemblyRepository.save(subassemblyToUpdate);

        return new OperationResult<>(true, null,updatedSubassembly);

    }




}
