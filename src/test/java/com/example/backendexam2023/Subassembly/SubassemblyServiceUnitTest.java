package com.example.backendexam2023.Subassembly;


import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class SubassemblyServiceUnitTest {

    @MockBean
    SubassemblyRepository subassemblyRepository;

    @Test
    void shouldReturnSubWhenCreatingSub(){

        Subassembly part = new Subassembly("screw");

        when(subassemblyRepository.save(any(Subassembly.class))).thenReturn(part);

        Subassembly fork = subassemblyRepository.save(new Subassembly("fork"));

        assert fork.getSubassemblyName().equals(part.getSubassemblyName());

    }
}
