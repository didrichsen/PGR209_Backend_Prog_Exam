package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order-line")
public class OrderLineController {

    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService){
        this.orderLineService = orderLineService;
    }

    @PostMapping()
    public ResponseEntity<Object> createOrderLine(Long MachineId) {

        OperationResult<Object> operationResult = orderLineService.createOrderLine(MachineId);

        if (operationResult.success()) {
            return new ResponseEntity<>(operationResult.createdObject(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(operationResult.errorMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrderLine(@PathVariable Long id){
        orderLineService.deleteOrderLine(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderLine> getOrderLineById(@PathVariable Long id){
        OrderLine orderLine = orderLineService.getOrderLineById(id);

        if (orderLine == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(orderLine, HttpStatus.OK);
    }
    @GetMapping("/page/{pageNumber}")
    public List<OrderLine> getOrderLinesByPage(@PathVariable int pageNumber) {
        return orderLineService.getOrderLinesPageable(pageNumber);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrderLine(@PathVariable Long id, @RequestBody OrderLine newOrderLine){
        try{
            return new ResponseEntity<>(orderLineService.updateOrderLine(id, newOrderLine), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }








}
