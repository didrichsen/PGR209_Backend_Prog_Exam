package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.*;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MachineService machineService;

    private final CustomerService customerService;

    @Autowired
    public OrderService(OrderRepository orderRepository, MachineService machineService, CustomerService customerService){
        this.orderRepository = orderRepository;
        this.machineService = machineService;
        this.customerService = customerService;
    }

    public Order createOrder(OrderRequest orderRequest){
        Order order = new Order();
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());

        List<Machine> machines = new ArrayList<>();

        for(Long machineId : orderRequest.getMachineIds()){
            Machine machine = machineService.getMachineById(machineId);
            machines.add(machine);
        }

        order.setMachines(machines);
        order.setCustomer(customer);

        return orderRepository.save(order);
    }

    //Insert pagination later
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    /*
    public void deleteOrder(Long id){

        Order order = getOrderById(id);

        List<Machine> machines = order.getMachines();

        machines.forEach(machine ->{

            List<Subassembly> subassemblies = machine.getSubassemblies();
            subassemblies.forEach(subassembly -> {

                List<Part> parts = subassembly.getParts();
                parts.forEach(part -> partService.deletePartById(part.getPartId()));
                subassemblyService.deleteSubassembly(subassembly.getSubassemblyId());
            });

            machineService.deleteMachine(machine.getMachineId());

            });

        orderRepository.deleteById(order.getOrderId());

    }

     */

    public void deleteOrder(Long id){

        orderRepository.deleteById(id);

    }

    public Order updateOrder(Order order){
        return orderRepository.save(order);
    }

}
