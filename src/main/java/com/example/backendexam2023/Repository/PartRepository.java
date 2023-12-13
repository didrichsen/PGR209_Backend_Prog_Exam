package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PartRepository extends JpaRepository<Part, Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM PART WHERE PART_ID = :partId AND SUBASSEMBLY_ID IS NOT NULL", nativeQuery = true)
    boolean isPartInUse(@Param("partId") Long partId);
}
