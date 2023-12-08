package com.example.backendexam2023;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.*;
import com.example.backendexam2023.Service.OrderService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//TODOS
//Make sure that customer has address before connected to order. Or something. Check when making order that customer has valid address.
//Create update for all
//Delete for all
//Tests

@SpringBootApplication
public class BackendExam2023Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendExam2023Application.class, args);
    }

    @Profile("!test")
    @Bean
    CommandLineRunner commandLineRunner(
            PartRepository partRepository,
            SubassemblyRepository subassemblyRepository,
            MachineRepository machineRepository,
            CustomerRepository customerRepository,
            OrderLineRepository orderLineRepo,
            OrderService orderService,
            AddressRepository addressRepository
    ) {
        Faker faker = new Faker();
        return args -> {

            for(int i = 0; i < 50; i ++){

               Customer customer = customerRepository.save(new Customer(faker.name().fullName(), faker.internet().emailAddress()));
               Address address = addressRepository.save(new Address(faker.address().streetAddress(), 1200 + i));
               customer.getAddresses().add(address);
               customerRepository.save(customer);

                for (int j = 0; j < 1; j++){
                    int randomPrice = new Random().nextInt(5001) + 5000;
                    Machine machine = machineRepository.save(new Machine( faker.commerce().productName(), randomPrice));
                    for (int l = 0; l < 2; l++){
                        Subassembly subassembly = subassemblyRepository.save(new Subassembly("Subassembly Number:" + (i + j)));
                        for(int k = 0; k < 3; k++){
                            Part part = partRepository.save(new Part(faker.commerce().material()));
                            subassembly.getParts().add(part);
                        }
                        subassemblyRepository.save(subassembly);
                        machine.getSubassemblies().add(subassembly);
                    }
                    Machine machine2 = machineRepository.save(machine);

                    List<Long> orderLines = new ArrayList<>();

                    if(i % 2 == 0){

                        for(int m = 0; m < 2; m++){
                            CreateOrderLines(machine2,orderLines,orderLineRepo);
                        }
                    }
                    else if (i % 3 == 0){
                        for(int m = 0; m < 4; m++){
                            CreateOrderLines(machine2,orderLines,orderLineRepo);
                        }
                    }
                    else {
                        CreateOrderLines(machine2,orderLines,orderLineRepo);
                    }
                    if (i % 2 == 0){
                        List<Long> orderLines2 = new ArrayList<>();
                        CreateOrderLines(machine2,orderLines2,orderLineRepo);
                        OrderRequest orderRequest = new OrderRequest();
                        orderRequest.getOrderLineIds().addAll(orderLines2);
                        orderRequest.setCustomerId(customer.getCustomerId());
                        orderService.createOrder(orderRequest);
                    }

                    OrderRequest orderRequest2 = new OrderRequest();
                    orderRequest2.getOrderLineIds().addAll(orderLines);
                    orderRequest2.setCustomerId(customer.getCustomerId());
                    orderService.createOrder(orderRequest2);

                }
            }
        };
    }
    private void CreateOrderLines(Machine machine, List<Long> orderLines, OrderLineRepository orderLineRepository){
        OrderLine orderLine = orderLineRepository.save(new OrderLine());
        orderLine.setMachine(machine);
        orderLineRepository.save(orderLine);
        orderLines.add(orderLine.getOrderLineId());
    }
}
