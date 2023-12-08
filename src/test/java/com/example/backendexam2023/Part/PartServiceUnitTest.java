package com.example.backendexam2023.Part;

import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import com.example.backendexam2023.Service.PartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ActiveProfiles("test")
@SpringBootTest
public class PartServiceUnitTest {

    @MockBean
    private PartRepository partRepository;

    @Autowired
    private PartService partService;

    @Test
    void shouldReturnPartWhenCreatingPart(){

        Part part = new Part("screw");

        when(partRepository.save(any(Part.class))).thenReturn(part);

        Part fork = partService.createPart(new Part("Fork"));

        assert fork.getPartName().equals(part.getPartName());

    }

    @Test
    void shouldReturnErrorIfPartNameIsNull() {

        Part partWithNullName = new Part();

        assertThrows(RuntimeException.class, () -> partService.createPart(partWithNullName));

    }









}
