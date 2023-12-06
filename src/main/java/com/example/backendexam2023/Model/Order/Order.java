package com.example.backendexam2023.Model.Order;

import com.example.backendexam2023.Model.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.OrderBatch.OrderBatch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "customer_order")
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne
    @JoinColumn(name = "order_batch_id")
    private OrderBatch orderBatch;


}
