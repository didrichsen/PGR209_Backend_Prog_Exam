package com.example.backendexam2023.Model.Subassembly;

import com.example.backendexam2023.Model.Part;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Subassembly {

    @Id
    @GeneratedValue(generator = "subassembly_generator")
    @SequenceGenerator(name = "subassembly_generator", sequenceName = "subassembly_seq", initialValue = 1, allocationSize = 1)
    @Column(name="subassembly_id")
    private Long subassemblyId = 0L;

    @Column(name = "subassembly_name")
    private String subassemblyName;

    @OneToMany
    @JoinColumn(name = "subassembly_id")
    private List<Part> parts = new ArrayList<>();

    public Subassembly(String subassemblyName){
        this.subassemblyName = subassemblyName;
    }




}
