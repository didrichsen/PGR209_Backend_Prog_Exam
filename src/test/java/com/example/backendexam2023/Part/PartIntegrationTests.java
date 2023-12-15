package com.example.backendexam2023.Part;

import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest
public class PartIntegrationTests {


    @Autowired
    PartRepository partRepository;

    @Autowired
    SubassemblyRepository subassemblyRepository;

    @Test
    void should_confirm_that_part_is_not_use(){

        Part part = new Part();

        Part partDB = partRepository.save(part);

        boolean isInUse = partRepository.isPartInUse(partDB.getPartId());

        assertFalse(isInUse);

    }

    @Test
    void should_confirm_that_part_is_in_use(){

        Subassembly subassembly = new Subassembly();

        Part part = new Part();

        Part partDB = partRepository.save(part);
        subassembly.getParts().add(part);

        subassemblyRepository.save(subassembly);

        boolean isInUse = partRepository.isPartInUse(partDB.getPartId());

        assertTrue(isInUse);

    }


}
