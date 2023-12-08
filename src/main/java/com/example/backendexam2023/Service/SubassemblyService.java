package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubassemblyService {

    private final SubassemblyRepository subassemblyRepository;
    private final PartService partService;

    @Autowired
    public SubassemblyService(SubassemblyRepository subassemblyRepository, PartService partService){
        this.subassemblyRepository = subassemblyRepository;
        this.partService = partService;
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

    public void deleteSubassembly(Long id){
        subassemblyRepository.deleteById(id);
    }

    protected List<Subassembly> getAllSubassemblies() {
        return subassemblyRepository.findAll();
    }




}
