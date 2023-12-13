package com.example.backendexam2023.Order;

import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.OrderRepository;
import com.example.backendexam2023.Service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan("com.example.backendexam2023.Service")
public class OrderIntegrationTest {

    @Autowired
    OrderService orderService;

    @Autowired
    PartService partService;
    @Autowired
    SubassemblyService subassemblyService;
    @Autowired
    MachineService machineService;
    @Autowired
    OrderLineService orderLineService;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerService customerService;

    @Test
    void should_create_order_from_scratch_and_fetch_from_DB(){

        //testing that an Object of type order includes everything It's supposed to.

        // Creating customer
        Customer cust = new Customer("name", "email");
        cust.getAddresses().add(new Address());
        OperationResult<Object> customerResult =  customerService.addCustomer(cust);
        Customer customer = (Customer) customerResult.createdObject();


        //Creating part
        Part part = new Part("fork");
        OperationResult<Object> resultPart = partService.createPart(part);
        Part newlyCreatedPart = (Part) resultPart.createdObject();

        //Creating subassembly
        SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
        subassemblyRequest.setSubassemblyName("Sub");
        subassemblyRequest.getPartIds().add(newlyCreatedPart.getPartId());
        OperationResult<Object> resultSubassembly = subassemblyService.createSubassembly(subassemblyRequest);
        Subassembly newlyCreatedSubassembly = (Subassembly) resultSubassembly.createdObject();

        //Creating Machine
        MachineRequest machineRequest = new MachineRequest();
        machineRequest.setMachineName("machine");
        machineRequest.setPrice(1000);
        machineRequest.getSubassemblyIds().add(newlyCreatedSubassembly.getSubassemblyId());
        OperationResult<Object> resultMachine = machineService.createMachine(machineRequest);

        Machine machine = (Machine) resultMachine.createdObject();

        OperationResult<Object> orderLinerResult = orderLineService.createOrderLine(machine.getMachineId());
        OrderLine orderLine = (OrderLine) orderLinerResult.createdObject();

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(customer.getCustomerId());
        orderRequest.setOrderLineIds(List.of(orderLine.getOrderLineId()));

        OperationResult<Object> orderResult = orderService.createOrder(orderRequest);
        System.out.println(orderResult.success());
        Order orderFromDB = (Order) orderResult.createdObject();
        System.out.println(orderFromDB.getOrderId());

        Order order = orderService.getOrderById(1L);

        // Assertions
        assertNotNull(order);
        assertFalse(order.getOrderLines().isEmpty());
        assertEquals(orderFromDB, order);
        assertNotNull(order.getOrderLines());
        assertNotNull(order.getOrderDate());
        assertNotNull(order.getCustomer());
        assertEquals(1, order.getCustomer().getAddresses().size());

    }

}
