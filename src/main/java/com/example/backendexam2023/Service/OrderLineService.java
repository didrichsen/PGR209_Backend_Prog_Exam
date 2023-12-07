package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final MachineService machineService;

    private final CustomerService customerService;

    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository, MachineService machineService, CustomerService customerService){
        this.orderLineRepository = orderLineRepository;
        this.machineService = machineService;
        this.customerService = customerService;
    }

    public OrderLine getOrderLineById(Long id){
        return orderLineRepository.findById(id).orElse(null);
    }
/*
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

 */

    //Insert pagination later
    public List<OrderLine> getAllOrderLines(){
        return orderLineRepository.findAll();
    }

    public List<OrderLine> getOrderLinesPageable(int pageNumber) {
        return orderLineRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }


    /*
    public void deleteOrderLine(Long id){

        Order order = getOrderLineById(id);

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

    public void deleteOrderLine(Long id){

        orderLineRepository.deleteById(id);

    }

    public OrderLine updateOrderLine(OrderLine orderLine){
        return orderLineRepository.save(orderLine);
    }

}
