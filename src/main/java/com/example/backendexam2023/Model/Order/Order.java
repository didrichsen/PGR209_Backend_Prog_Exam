package com.example.backendexam2023.Model.Order;

import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(generator = "order_batch_generator")
    @SequenceGenerator(name = "order_batch_generator", sequenceName = "order_batch_seq", initialValue = 1, allocationSize = 1)
    @Column(name="order_batch_id")
    private Long orderBatchId = 0L;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_price")
    private Integer totalPrice;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderLine> orderLines = new ArrayList<>();

    public Order(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

}
