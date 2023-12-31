package com.example.backendexam2023.Machine;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Records.DeleteResultIds;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import com.example.backendexam2023.Service.MachineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@SpringBootTest
public class MachineServiceUnitTests {

    @MockBean
    private MachineRepository machineRepository;

    @MockBean
    private SubassemblyRepository subassemblyRepository;

    @MockBean
    private OrderLineRepository orderLineRepository;

    @Autowired
    private MachineService machineService;

    @Test
    void should_not_create_machine_because_name_is_null() {
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setPrice(1000);

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);

        assertFalse(operationResult.success());
        assertEquals("Machine needs a name", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_machine_because_name_is_empty() {
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("");
        machineRequest.setPrice(1000);

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);

        assertFalse(operationResult.success());
        assertEquals("Machine needs a name", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_machine_because_price_is_invalid() {
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("ValidName");
        machineRequest.setPrice(0);

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);

        assertFalse(operationResult.success());
        assertEquals("Machine needs a price", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_machine_because_subassemblies_are_empty() {
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("ValidName");
        machineRequest.setPrice(1000);

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);

        assertFalse(operationResult.success());
        assertEquals("A machine needs to at least have one subassembly", operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_not_create_machine_because_subassembly_is_not_found() {
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("ValidName");
        machineRequest.setPrice(1000);
        machineRequest.getSubassemblyIds().add(1L);

        when(subassemblyRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);

        assertFalse(operationResult.success());
        assertEquals("Couldn't find subassembly with id " + 1L, operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_create_machine() {
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("ValidName");
        machineRequest.setPrice(1000);
        List<Long> subassemblyIds = List.of(1L, 2L);
        machineRequest.getSubassemblyIds().addAll(subassemblyIds);

        Subassembly subassembly1 = new Subassembly();
        Subassembly subassembly2 = new Subassembly();

        when(subassemblyRepository.findById(1L)).thenReturn(Optional.of(subassembly1));
        when(subassemblyRepository.findById(2L)).thenReturn(Optional.of(subassembly2));
        when(machineRepository.save(any(Machine.class))).thenAnswer(argument -> argument.getArgument(0));

        OperationResult<Object> operationResult = machineService.createMachine(machineRequest);
        Machine machine = (Machine) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals("ValidName", machine.getMachineName());
        assertEquals(1000, machine.getPrice());
        assertTrue(machine.getSubassemblies().contains(subassembly1));
        assertTrue(machine.getSubassemblies().contains(subassembly2));
    }

    @Test
    public void should_not_delete_machine_in_use() {
        Long machineId = 1L;
        Machine machine = new Machine();
        machine.setMachineId(machineId);

        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(1L);

        when(machineRepository.findById(machineId)).thenReturn(Optional.of(machine));
        when(orderLineRepository.findByMachine(machine)).thenReturn(Optional.of(List.of(orderLine)));

        DeleteResultIds result = machineService.deleteMachineById(machineId);

        assertFalse(result.success());
        assertEquals("Can't delete machine. Machine placed in order lines.", result.error());
        assertEquals(List.of(1L), result.related_ids());
    }

    @Test
    public void should_delete_machine() {
        Long machineId = 1L;
        Machine machine = new Machine();
        machine.setMachineId(machineId);

        when(machineRepository.findById(machineId)).thenReturn(Optional.of(machine));
        when(orderLineRepository.findByMachine(machine)).thenReturn(Optional.empty());

        DeleteResultIds result = machineService.deleteMachineById(machineId);

        assertTrue(result.success());
        assertNull(result.error());
        assertNull(result.related_ids());

    }

    @Test
    public void should_not_update_machine_not_found() {
        Long machineId = 1L;
        Machine newMachine = new Machine();
        when(machineRepository.findById(machineId)).thenReturn(Optional.empty());

        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("testmac");
        machineRequest.setPrice(100);
        machineRequest.getSubassemblyIds().add(1L);

        OperationResult<Object> result = machineService.updateMachine(machineId, machineRequest);

        assertFalse(result.success());
        assertEquals("Couldn't find machine with id " + machineId, result.errorMessage());
        assertNull(result.createdObject());
    }

    @Test
    public void should_update_machine_with_price_subassemblies_and_name() {
        Long machineId = 1L;
        Machine existingMachine = new Machine();
        existingMachine.setMachineId(machineId);
        existingMachine.setMachineName("Old Machine");


        MachineRequest newMachine = new MachineRequest();
        newMachine.setPrice(10000);
        newMachine.setMachineName("New Machine");
        newMachine.setSubassemblyIds(List.of(1L, 2L));

        Subassembly subassembly = new Subassembly();
        subassembly.setSubassemblyId(1L);

        Subassembly subassembly1 = new Subassembly();
        subassembly1.setSubassemblyId(2L);


        when(machineRepository.findById(machineId)).thenReturn(Optional.of(existingMachine));
        when(machineRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(subassemblyRepository.findById(1L)).thenReturn(Optional.of(subassembly));
        when(subassemblyRepository.findById(2L)).thenReturn(Optional.of(subassembly1));

        OperationResult<Object> result = machineService.updateMachine(machineId, newMachine);

        List<Long> subIds = new ArrayList<>();

        Machine machine = (Machine) result.createdObject();

        for (Subassembly s : machine.getSubassemblies()) {
            subIds.add(s.getSubassemblyId());
        }

        assertTrue(result.success());
        assertNull(result.errorMessage());
        assertEquals(newMachine.getPrice(), ((Machine) result.createdObject()).getPrice());
        assertEquals(newMachine.getSubassemblyIds(), subIds);
        assertEquals(newMachine.getMachineName(), ((Machine) result.createdObject()).getMachineName());

    }



}
