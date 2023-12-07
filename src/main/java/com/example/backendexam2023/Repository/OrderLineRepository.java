package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.OrderLine.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}
