package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;

    @Autowired
    public PartService(PartRepository partRepository){
        this.partRepository = partRepository;
    }

    public Part getPartById(Long id){
        return partRepository.findById(id).orElse(null);
    }

    public Part createPart(Part part){
        if (part.getPartName() == null) throw new RuntimeException();
        return partRepository.save(part);
    }

    public List<Part> getAllParts(){
        return partRepository.findAll();
    }
    
    public List<Part> getPartsPageable(int pageNumber) {
        return partRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }
    
    public void deletePartById(Long id){
        partRepository.deleteById(id);
    }
    public Part updatePart(Part part){
        return partRepository.save(part);
    }

}
