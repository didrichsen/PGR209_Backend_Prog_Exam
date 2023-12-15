package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Records.DeleteResultIds;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final MachineRepository machineRepository;

    private final OrderRepository orderRepository;


    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository, MachineRepository machineRepository, OrderRepository orderRepository){
        this.orderLineRepository = orderLineRepository;
        this.machineRepository = machineRepository;
        this.orderRepository = orderRepository;
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


    public DeleteResultIds deleteOrderLine(Long orderLineId){

        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElse(null);

        if(orderLine == null){
            return new DeleteResultIds(false, "Couldn't find order lines with id " + orderLineId,null);
        }

        return new DeleteResultIds(true,null,null);

    }

    public OperationResult<Object> updateOrderLine(Long orderLineId, OrderLine newOrderLine){

        OrderLine orderLineToUpdate = getOrderLineById(orderLineId);

        if (orderLineToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find any order lines with id " + orderLineId, null);
        }

        Order order = orderRepository.findById(newOrderLine.getOrder().getOrderId()).orElse(null);
        Machine machine = machineRepository.findById(newOrderLine.getMachine().getMachineId()).orElse(null);

        if (order != null) orderLineToUpdate.setOrder(order);
        if (machine != null) orderLineToUpdate.setMachine(machine);

        return new OperationResult<>(true, null,orderLineRepository.save(orderLineToUpdate));
    }


}


