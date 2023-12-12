package com.example.backendexam2023.Part;

import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import com.example.backendexam2023.Service.PartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class PartServiceUnitTests {

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private SubassemblyRepository subassemblyRepository;

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

    @Test
    public void should_not_delete_part_not_found() {
        Long partId = 1L;
        when(partRepository.findById(partId)).thenReturn(Optional.empty());

        DeleteResult result = partService.deletePartById(partId);

        assertFalse(result.success());
        assertEquals("Couldn't find subassembly with id " + partId, result.error());
        assertNull(result.related_ids());
    }

    @Test
    public void should_not_delete_part_in_use() {
        Long partId = 1L;
        Part part = new Part();
        part.setPartId(partId);

        Subassembly subassembly = new Subassembly();
        subassembly.setSubassemblyId(1L);
        subassembly.getParts().add(part);

        when(partRepository.findById(partId)).thenReturn(Optional.of(part));
        when(subassemblyRepository.findAll()).thenReturn(List.of(subassembly));

        DeleteResult result = partService.deletePartById(partId);

        assertFalse(result.success());
        assertEquals("Part in use. Can't delete part.", result.error());
        assertEquals(List.of(subassembly.getSubassemblyId()), result.related_ids());
    }

    @Test
    public void should_delete_part() {
        Long partId = 1L;
        Part part = new Part();
        part.setPartId(partId);

        when(partRepository.findById(partId)).thenReturn(Optional.of(part));
        when(subassemblyRepository.findAll()).thenReturn(Collections.emptyList());

        DeleteResult result = partService.deletePartById(partId);

        assertTrue(result.success());
        assertNull(result.error());
        assertNull(result.related_ids());
    }

}
