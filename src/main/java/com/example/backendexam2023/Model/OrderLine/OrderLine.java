package com.example.backendexam2023.Model.OrderLine;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "order_line")
public class OrderLine {

    @Id
    @GeneratedValue(generator = "order_line_generator")
    @SequenceGenerator(name = "order_line_generator", sequenceName = "order_line_seq", initialValue = 1, allocationSize = 1)
    @Column(name="order_line_id")
    private Long orderLineId = 0L;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderLine(Machine machine){
        this.machine = machine;
    }


}
