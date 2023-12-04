package com.example.backendexam2023.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Order {

    @Id
    @GeneratedValue(generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_seq", initialValue = 1, allocationSize = 1)
    @Column(name="order_id")
    private Long orderId = 0L;

    @ManyToOne
    @JoinColumn(name="customer_id")
    @JsonIgnoreProperties("orders")
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Machine> machines = new ArrayList<>();


}
