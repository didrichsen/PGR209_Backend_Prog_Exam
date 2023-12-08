package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Part.Part;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part,Long> {
}
