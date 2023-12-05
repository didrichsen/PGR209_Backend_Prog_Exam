package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Subassembly;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubassemblyService {

    private final SubassemblyRepository subassemblyRepository;

    @Autowired
    public SubassemblyService(SubassemblyRepository subassemblyRepository){
        this.subassemblyRepository = subassemblyRepository;
    }

    public Subassembly createSubassembly(Subassembly subassembly){
        return subassemblyRepository.save(subassembly);
    }

    public Subassembly getSubassembly(Long id){
        return subassemblyRepository.findById(id).orElse(null);
    }

    public void deleteSubassembly(Long id){
        subassemblyRepository.deleteById(id);
    }




}
