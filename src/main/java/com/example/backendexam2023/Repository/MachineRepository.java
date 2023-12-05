package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Machine.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineRepository extends JpaRepository<Machine,Long> {
}
