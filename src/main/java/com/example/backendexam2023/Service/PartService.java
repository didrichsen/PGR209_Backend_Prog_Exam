package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;

    @Autowired
    public PartService(PartRepository partRepository){
        this.partRepository = partRepository;
    }

    public Part createPart(Part part){
        return partRepository.save(part);
    }

    public List<Part> getAllParts(){
        return partRepository.findAll();
    }
    public Part getPartById(Long id){
        return partRepository.findById(id).orElse(null);
    }
    public void deletePart(Long id){
        partRepository.deleteById(id);
    }
    public Part updatePart(Part part){
        return partRepository.save(part);
    }

}
