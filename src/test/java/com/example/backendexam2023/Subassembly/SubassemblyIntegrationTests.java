package com.example.backendexam2023.Subassembly;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.Mac;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class SubassemblyIntegrationTests {


    @Autowired
    SubassemblyRepository subassemblyRepository;

    @Autowired
    MachineRepository machineRepository;

    @Test
    void should_confirm_that_subassembly_is_not_use(){

        Subassembly subassembly = new Subassembly();

        Subassembly subassemblyDB = subassemblyRepository.save(subassembly);

        boolean isInUse = subassemblyRepository.isSubassemblyInUse(subassemblyDB.getSubassemblyId());

        assertFalse(isInUse);

    }

    @Test
    void should_confirm_that_part_is_in_use(){

        Machine machine = new Machine();

        Subassembly subassembly = new Subassembly();

        Subassembly subDB = subassemblyRepository.save(subassembly);
        machine.getSubassemblies().add(subDB);

        machineRepository.save(machine);

        boolean isInUse = subassemblyRepository.isSubassemblyInUse(subDB.getSubassemblyId());

        assertTrue(isInUse);

    }



}
