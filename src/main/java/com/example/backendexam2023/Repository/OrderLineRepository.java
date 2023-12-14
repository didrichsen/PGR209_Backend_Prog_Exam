package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM ORDER_LINE WHERE ORDER_LINE_ID = :orderLineId AND ORDER_ID IS NOT NULL", nativeQuery = true)
    boolean isOrderLineRegisteredWithOrder(@Param("orderLineId") Long orderLineId);


    Optional<List<OrderLine>> findByMachine(Machine machine);
}
