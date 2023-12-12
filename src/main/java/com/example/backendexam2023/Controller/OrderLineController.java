package com.example.backendexam2023.Controller;

import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/order-line")
public class OrderLineController {

    private final OrderLineService orderLineService;

    @Autowired
    public OrderLineController(OrderLineService orderLineService){
        this.orderLineService = orderLineService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> createOrderLine(@PathVariable Long id) {

        OperationResult<Object> operationResult = orderLineService.createOrderLine(id);

        if (operationResult.success()) {
            return new ResponseEntity<>(operationResult.createdObject(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Map.of("error",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrderLine(@PathVariable Long id){
        DeleteResult deleteResult = orderLineService.deleteOrderLine(id);

        if(deleteResult.success()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(deleteResult.error(),HttpStatus.BAD_REQUEST);
        }

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

    @PutMapping("/update/{orderLineId}")
    public ResponseEntity<Object> updateOrderLine(@PathVariable Long orderLineId, @RequestBody OrderLine orderLineData){

        OperationResult<Object> operationResult = orderLineService.updateOrderLine(orderLineId,orderLineData);

        if(operationResult.success()){
            return new ResponseEntity<>(operationResult.createdObject(),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error:",operationResult.errorMessage()), HttpStatus.BAD_REQUEST);
        }

    }




}
