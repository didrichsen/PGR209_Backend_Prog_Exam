package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    Optional<List<OrderLine>> findByMachine(Machine machine);
}
