package com.example.backendexam2023.orderLine;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Service.OrderLineService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
public class OrderLineUnitTests {

    @MockBean
    private MachineRepository machineRepository;

    @MockBean
    private OrderLineRepository orderLineRepository;

    @Autowired
    private OrderLineService orderLineService;


    @Test
    void should_not_create_order_line_because_machine_not_found() {
        Long machineId = 1L;
        when(machineRepository.findById(machineId)).thenReturn(Optional.empty());

        OperationResult<Object> operationResult = orderLineService.createOrderLine(machineId);

        assertFalse(operationResult.success());
        assertEquals("Couldn't find any machines with id of " + machineId, operationResult.errorMessage());
        assertNull(operationResult.createdObject());
    }

    @Test
    void should_create_order_line() {
        Long machineId = 1L;
        Machine machine = new Machine();
        machine.setMachineId(machineId);

        when(machineRepository.findById(machineId)).thenReturn(Optional.of(machine));
        when(orderLineRepository.save(ArgumentMatchers.any(OrderLine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> operationResult = orderLineService.createOrderLine(machineId);
        OrderLine createdOrderLine = (OrderLine) operationResult.createdObject();

        assertTrue(operationResult.success());
        assertNull(operationResult.errorMessage());
        assertNotNull(operationResult.createdObject());
        assertEquals(machine, createdOrderLine.getMachine());
    }

    @Test
    void should_not_delete_order_line_not_found() {
        Long orderLineId = 1L;

        when(orderLineRepository.findById(1L)).thenReturn(Optional.empty());

        DeleteResult result = orderLineService.deleteOrderLine(orderLineId);

        assertFalse(result.success());
        assertEquals("Couldn't find order lines with id " + orderLineId, result.error());
        assertNull(result.related_ids());
    }

    @Test
    void should_delete_order_line() {
        Long orderLineId = 1L;
        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(orderLineId);

        when(orderLineRepository.findById(1L)).thenReturn(Optional.of(orderLine));

        DeleteResult result = orderLineService.deleteOrderLine(orderLineId);

        assertTrue(result.success());
        assertNull(result.error());
    }

    @Test
    void should_not_update_order_line_not_found() {
        Long orderLineId = 1L;
        OrderLine newOrderLine = new OrderLine();

        when(orderLineRepository.findById(1L)).thenReturn(Optional.empty());

        OperationResult<Object> result = orderLineService.updateOrderLine(orderLineId, newOrderLine);

        assertFalse(result.success());
        assertEquals("Couldn't find any order lines with id " + orderLineId, result.errorMessage());
        assertNull(result.createdObject());
    }

    @Test
    void should_update_order_line() {
        Long orderLineId = 1L;
        OrderLine existingOrderLine = new OrderLine();
        existingOrderLine.setOrderLineId(orderLineId);
        existingOrderLine.setOrder(new Order());
        existingOrderLine.setMachine(new Machine());

        OrderLine newOrderLine = new OrderLine();
        newOrderLine.setOrder(new Order());
        newOrderLine.setMachine(new Machine());

        when(orderLineRepository.findById(1L)).thenReturn(Optional.of(existingOrderLine));
        when(orderLineRepository.save(ArgumentMatchers.any(OrderLine.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OperationResult<Object> result = orderLineService.updateOrderLine(orderLineId, newOrderLine);

        assertTrue(result.success());
        assertNull(result.errorMessage());
        assertEquals(newOrderLine.getOrder(), ((OrderLine) result.createdObject()).getOrder());
        assertEquals(newOrderLine.getMachine(), ((OrderLine) result.createdObject()).getMachine());

    }




}
