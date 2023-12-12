package com.example.backendexam2023.Service;

import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final MachineRepository machineRepository;


    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository, MachineRepository machineRepository){
        this.orderLineRepository = orderLineRepository;
        this.machineRepository = machineRepository;
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

        return new OperationResult(true, null, orderLineReturned);
    }

    public List<OrderLine> getOrderLinesPageable(int pageNumber) {
        return orderLineRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }


    public DeleteResult deleteOrderLine(Long orderLineId){

        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElse(null);

        if(orderLine == null){
            return new DeleteResult(false, "Couldn't find order lines with id " + orderLineId,null);
        }

        return new DeleteResult(true,null,null);

    }

    public OperationResult<Object> updateOrderLine(Long orderLineId, OrderLine newOrderLine){

        OrderLine orderLineToUpdate = getOrderLineById(orderLineId);

        if (orderLineToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find any order lines with id " + orderLineId, null);
        }

        if (newOrderLine.getOrder() != null) orderLineToUpdate.setOrder(newOrderLine.getOrder());
        if (newOrderLine.getMachine() != null) orderLineToUpdate.setMachine(newOrderLine.getMachine());

        return new OperationResult<>(true, null,orderLineRepository.save(orderLineToUpdate));
    }


}


