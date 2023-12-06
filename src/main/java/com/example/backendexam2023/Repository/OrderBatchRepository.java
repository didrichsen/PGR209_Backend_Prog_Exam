package com.example.backendexam2023.Repository;

import com.example.backendexam2023.OrderBatch.OrderBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBatchRepository extends JpaRepository<OrderBatch,Long> {
}
