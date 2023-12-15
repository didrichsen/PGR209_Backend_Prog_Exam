package com.example.backendexam2023.orderLine;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class OrderLineIntegrationTests {

    @Autowired
    OrderLineRepository orderLineRepository;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void should_find_order_lines_by_machine(){

        Machine machine = new Machine();

        OrderLine orderLine = new OrderLine(machine);
        OrderLine orderLine1 = new OrderLine(machine);

        orderLineRepository.saveAll(List.of(orderLine1,orderLine));

        Optional<List<OrderLine>> orderLinesFound = orderLineRepository.findByMachine(machine);

        List<OrderLine> orderLines = orderLinesFound.get();

        assertEquals(2,orderLines.size());

    }

    @Test
    void should_assert_to_true_order_line_has_order(){

        OrderLine orderLine = new OrderLine();

        OrderLine orderLineFromDB = orderLineRepository.save(orderLine);

        Order order = new Order();
        order.getOrderLines().add(orderLineFromDB);
        orderRepository.save(order);

        Boolean isRegisteredWithOrder = orderLineRepository.isOrderLineRegisteredWithOrder(orderLineFromDB.getOrderLineId());

        assertTrue(isRegisteredWithOrder);


    }

    @Test
    void should_assert_to_false_order_line_has_no_other_order(){

        OrderLine orderLine = new OrderLine();

        OrderLine orderLineFromDB = orderLineRepository.save(orderLine);

        Boolean isRegisteredWithOrder = orderLineRepository.isOrderLineRegisteredWithOrder(orderLineFromDB.getOrderLineId());

        assertFalse(isRegisteredWithOrder);

    }

}
