package com.example.backendexam2023;

import com.example.backendexam2023.Model.*;
import com.example.backendexam2023.Repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackendExam2023Application {

    public static void main(String[] args) {
        SpringApplication.run(BackendExam2023Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            MachineRepository machineRepository,
            OrderRepository orderRepository,
            PartRepository partRepository,
            SubassemblyRepository subassemblyRepository
    ){
        return args -> {

            Machine machine = machineRepository.save(new Machine("Monster-Machine"));
            Subassembly subassembly = subassemblyRepository.save(new Subassembly("Monster-Tires"));
            Part part = partRepository.save(new Part("Monster-Rims"));

            machine.getSubassemblies().add(subassembly);
            machineRepository.save(machine);

            subassembly.getParts().add(part);
            subassemblyRepository.save(subassembly);


            Customer customer = customerRepository.save(new Customer("Simen Didrichsen", "simen@simen.no"));
            Address address = addressRepository.save(new Address("Hovseterveien 84D"));

            customer.getAddresses().add(address);
            customerRepository.save(customer);

            Order order = new Order();
            order.setCustomer(customer);
            order.getMachines().add(machine);

            orderRepository.save(order);

        };
    }

}
