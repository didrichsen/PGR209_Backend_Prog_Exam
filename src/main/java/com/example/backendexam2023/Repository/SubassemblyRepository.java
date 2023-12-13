package com.example.backendexam2023.Repository;

import com.example.backendexam2023.Model.Subassembly.Subassembly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubassemblyRepository extends JpaRepository<Subassembly,Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM SUBASSEMBLY WHERE SUBASSEMBLY_ID = :subassemblyId AND MACHINE_ID IS NOT NULL", nativeQuery = true)
    boolean isSubassemblyInUse(@Param("subassemblyId") Long subassemblyId);

}
