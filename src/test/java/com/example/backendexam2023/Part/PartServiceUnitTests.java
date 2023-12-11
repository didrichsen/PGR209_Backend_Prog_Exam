package com.example.backendexam2023.Part;

import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Service.PartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class PartServiceUnitTests {

    @MockBean
    private PartRepository partRepository;

    @Autowired
    private PartService partService;

    @Test
    void should_not_create_part_because_name_is_null() {
        Part part = new Part();
        part.setPartName(null);

        OperationResult<Object> operationResult = partService.createPart(part);

        assertFalse(operationResult.success());
        assertEquals("Invalid name", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_part_because_name_is_empty() {
        Part part = new Part();
        part.setPartName("");

        OperationResult<Object> operationResult = partService.createPart(part);

        assertFalse(operationResult.success());
        assertEquals("Invalid name", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_create_part() {
        Part part = new Part();
        part.setPartName("ValidName");

        when(partRepository.save(any(Part.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> operationResult = partService.createPart(part);
        Part createdPart = (Part) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertEquals("Part Created", operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals("ValidName", createdPart.getPartName());
    }
}
