package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
