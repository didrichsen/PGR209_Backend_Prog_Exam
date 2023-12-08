package com.example.backendexam2023.Service;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.CustomerRepository;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final MachineRepository machineRepository;

    private final CustomerRepository customerRepository;

    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository, MachineRepository machineRepository, CustomerRepository customerRepository){
        this.orderLineRepository = orderLineRepository;
        this.machineRepository = machineRepository;
        this.customerRepository = customerRepository;
    }

    public OrderLine getOrderLineById(Long id){
        return orderLineRepository.findById(id).orElse(null);
    }

    public OperationResult<Object> createOrderLine(Long machineId){

        Machine machine = machineRepository.findById(machineId).orElse(null);

        if(machine == null){
            return new OperationResult<>(false, "Couldn't find any machines with id of " + machineId, null);
        }

        OrderLine orderLine = new OrderLine();
        orderLine.setMachine(machine);
        OrderLine orderLineReturned = orderLineRepository.save(orderLine);

        return new OperationResult(true, "Order Created", orderLineReturned);
    }



    //Insert pagination later
    public List<OrderLine> getAllOrderLines(){
        return orderLineRepository.findAll();
    }

    public List<OrderLine> getOrderLinesPageable(int pageNumber) {
        return orderLineRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }


    public void deleteOrderLine(Long id){

        orderLineRepository.deleteById(id);

    }

    public OrderLine updateOrderLine(Long orderLineId, OrderLine newOrderLine){
        OrderLine orderLineToUpdate = getOrderLineById(orderLineId);

        if (orderLineToUpdate == null) throw new RuntimeException("Could not find orderLine with id " + orderLineId);
        if (newOrderLine.getOrder() != null) orderLineToUpdate.setOrder(newOrderLine.getOrder());
        if (newOrderLine.getMachine() != null) orderLineToUpdate.setMachine(newOrderLine.getMachine());

        return orderLineRepository.save(orderLineToUpdate);
    }

}
