package com.example.backendexam2023.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Part {


    @Id
    @GeneratedValue(generator = "part_generator")
    @SequenceGenerator(name = "part_generator", sequenceName = "part_seq", initialValue = 1, allocationSize = 1)
    @Column(name="part_id")
    private Long partId = 0L;

    @Column(name = "part_name")
    private String partName;

}
