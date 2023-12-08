package com.example.backendexam2023.Service;

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

        if(part.getPartName() == null){
            return new OperationResult<>(false,"Invalid name", null);
        }

        Part createdPart = partRepository.save(part);
        return new OperationResult<>(true,"Part Created", createdPart);

    }



    public List<Part> getAllParts(){
        return partRepository.findAll();
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
            return new DeleteResult(false, Collections.emptyList(), "Couldn't find subassembly with id " + id);
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
            return new DeleteResult(false,subassembliesUsingPart, "Part in use. Cant delete part.");
        }

        partRepository.deleteById(partToDelete.getPartId());

        return new DeleteResult(true,Collections.emptyList(), null);
    }

    public Part updatePart(Part part){
        return partRepository.save(part);
    }


}
