package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PartService {

    private final PartRepository partRepository;
    private SubassemblyRepository subassemblyRepository;

    @Autowired
    public PartService(PartRepository partRepository, SubassemblyRepository subassemblyRepository){
        this.partRepository = partRepository;
        this.subassemblyRepository = subassemblyRepository;
    }

    public Part getPartById(Long id){
        return partRepository.findById(id).orElse(null);
    }

    public OperationResult<Object> createPart(Part part){

        if(part.getPartName() == null || part.getPartName().trim().isEmpty()){
            return new OperationResult<>(false,"Invalid name", null);
        }

        Part createdPart = partRepository.save(part);
        return new OperationResult<>(true,"Part Created", createdPart);

    }
    
    public List<Part> getPartsPageable(int pageNumber) {
        return partRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }
    
    public DeleteResult deletePartById(Long id){

        List<Subassembly> subassembliesToCheck = subassemblyRepository.findAll();
        List<Long> subassembliesUsingPart = new ArrayList<>();

        boolean isInUse = false;

        Part partToDelete = partRepository.findById(id).orElse(null);

        if(partToDelete == null){
            return new DeleteResult(false,"Couldn't find subassembly with id " + id,Collections.emptyList());
        }

        for (Subassembly subassembly:subassembliesToCheck) {
            List<Part> parts = subassembly.getParts();
            for (Part part : parts) {
                if(Objects.equals(part, partToDelete)){
                    isInUse = true;
                    subassembliesUsingPart.add(subassembly.getSubassemblyId());
                }
            }
        }

        if(isInUse){
            return new DeleteResult(false,"Part in use. Cant delete part.",subassembliesUsingPart);
        }

        partRepository.deleteById(partToDelete.getPartId());

        return new DeleteResult(true, null,Collections.emptyList());
    }

    public OperationResult<Object> updatePart(Long partId, Part partData){

        Part partToUpdate = getPartById(partId);

        if (partToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find any part with id " + partId, null);
        }

        if (partData.getPartName() != null) partToUpdate.setPartName(partData.getPartName());

        return new OperationResult<>(true, null,partRepository.save(partToUpdate));

    }


}
