package com.example.backendexam2023;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.*;
import com.example.backendexam2023.Service.MachineService;
import com.example.backendexam2023.Service.OrderService;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

//TODOS
//Make sure that customer has address before connected to order. Or something. Check when making order that customer has valid address.
//Create faker names and addresses, and more orders.
//Create update for all
//Delete for all
//Get with pagination
//Tests



@SpringBootApplication
public class BackendExam2023Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendExam2023Application.class, args);
    }

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

                for (int j = 0; j < 1; j++){
                    OrderLine orderLine = orderLineRepo.save(new OrderLine());
                    Machine machine = machineRepository.save(new Machine( faker.commerce().productName(), 100 + i));
                    for (int l = 0; l < 2; l++){
                        Subassembly subassembly = subassemblyRepository.save(new Subassembly("subassembly" + l + j + i));
                        for(int k = 0; k < 3; k++){
                            Part part = partRepository.save(new Part(faker.commerce().material()));
                            subassembly.getParts().add(part);
                        }
                        subassemblyRepository.save(subassembly);
                        machine.getSubassemblies().add(subassembly);
                    }

                    machineRepository.save(machine);
                    orderLine.setMachine(machine);
                    OrderLine orderLine1 = orderLineRepo.save(orderLine);
                    List<OrderLine> orderLines = new ArrayList<>();
                    orderLines.add(orderLine1);
                    customer.getAddresses().add(address);
                    customerRepository.save(customer);
                    orderService.createOrder(orderLines, customer);
                }
            }
        };
    }
}
