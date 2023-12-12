package com.example.backendexam2023.Subassembly;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import com.example.backendexam2023.Service.PartService;
import com.example.backendexam2023.Service.SubassemblyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class SubassemblyServiceUnitTests {

    @MockBean
    private SubassemblyRepository subassemblyRepository;

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private MachineRepository machineRepository;

    @Autowired
    private SubassemblyService subassemblyService;



    @Test
    void should_not_create_subassembly_because_name_is_null() {
        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName(null);

        OperationResult<Object> operationResult = subassemblyService.createSubassembly(subassemblyRequest);

        assertFalse(operationResult.success());
        assertEquals("subassembly needs name", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_subassembly_because_name_is_empty() {
        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName("");

        OperationResult<Object> operationResult = subassemblyService.createSubassembly(subassemblyRequest);

        assertFalse(operationResult.success());
        assertEquals("subassembly needs name", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_subassembly_because_of_no_parts() {
        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName("ValidName");

        OperationResult<Object> operationResult = subassemblyService.createSubassembly(subassemblyRequest);

        assertFalse(operationResult.success());
        assertEquals("Subassembly has no parts", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_subassembly_because_of_couldnt_find_part() {
        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName("ValidName");
        subassemblyRequest.getPartIds().add(1L);

        when(partRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OperationResult<Object> operationResult = subassemblyService.createSubassembly(subassemblyRequest);

        assertFalse(operationResult.success());
        assertEquals("Couldn't find part with id " + 1L, operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_create_subassembly() {
        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName("ValidName");
        List<Long> partIds = List.of(1L, 2L);
        subassemblyRequest.setPartIds(partIds);

        Part part1 = new Part();
        Part part2 = new Part();

        when(partRepository.findById(1L)).thenReturn(Optional.of(part1));
        when(partRepository.findById(2L)).thenReturn(Optional.of(part2));
        when(subassemblyRepository.save(any(Subassembly.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> operationResult = subassemblyService.createSubassembly(subassemblyRequest);
        Subassembly subassembly = (Subassembly) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals("ValidName", subassembly.getSubassemblyName());
        assertTrue(subassembly.getParts().contains(part1));
        assertTrue(subassembly.getParts().contains(part2));
    }

    @Test
    public void should_not_delete_subassembly_not_found() {
        Long subassemblyId = 1L;
        when(subassemblyRepository.findById(subassemblyId)).thenReturn(Optional.empty());

        DeleteResult result = subassemblyService.deleteSubassemblyById(subassemblyId);

        assertFalse(result.success());
        assertEquals("Couldn't find subassembly " + subassemblyId, result.error());
        assertNull(result.related_ids());
    }

    @Test
    public void should_not_delete_subassembly_in_use() {
        Long subassemblyId = 1L;
        Subassembly subassembly = new Subassembly();
        subassembly.setSubassemblyId(subassemblyId);

        Machine machine = new Machine();
        machine.setMachineId(1L);
        machine.getSubassemblies().add(subassembly);

        when(subassemblyRepository.findById(subassemblyId)).thenReturn(Optional.of(subassembly));
        when(machineRepository.findAll()).thenReturn(List.of(machine));

        DeleteResult result = subassemblyService.deleteSubassemblyById(subassemblyId);

        assertFalse(result.success());
        assertEquals("Subassembly is in use. Can't delete.", result.error());
        assertEquals(List.of(machine.getMachineId()), result.related_ids());
    }

    @Test
    public void should_delete_subassembly() {
        Long subassemblyId = 1L;
        Subassembly subassembly = new Subassembly();
        subassembly.setSubassemblyId(subassemblyId);

        when(subassemblyRepository.findById(subassemblyId)).thenReturn(Optional.of(subassembly));
        when(machineRepository.findAll()).thenReturn(Collections.emptyList());

        DeleteResult result = subassemblyService.deleteSubassemblyById(subassemblyId);

        assertTrue(result.success());
        assertNull(result.error());
        assertNull(result.related_ids());

    }
}
