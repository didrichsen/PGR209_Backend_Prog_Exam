package com.example.backendexam2023;
import com.example.backendexam2023.Model.Address.Address;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Order.OrderRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Model.Subassembly.SubassemblyRequest;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Repository.*;
import com.example.backendexam2023.Service.*;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

            partRepository.save(new Part("Cotton"));

        };


    }
    private void CreateOrderLines(Machine machine, List<Long> orderLines, OrderLineRepository orderLineRepository){
        OrderLine orderLine = orderLineRepository.save(new OrderLine());
        orderLine.setMachine(machine);
        orderLineRepository.save(orderLine);
        orderLines.add(orderLine.getOrderLineId());
    }

     /*

    CommandLineRunner commandLineRunner(
            CustomerService customerService,
            AddressService addressService,
            PartService partService,
            SubassemblyService subassemblyService,
            MachineService machineService,
            OrderLineService orderLineService,
            OrderService orderService
    ) {
        Faker faker = new Faker();
        return args -> {

            Random random = new Random();

            //Creating 50 random customers.
            for (int i = 0; i < 50; i++) {

                Customer customer = new Customer(faker.name().fullName(), faker.internet().emailAddress());
                customerService.addCustomer(customer);

            }

            //Creates 50 addresses
            for (int i = 0; i < 50; i++) {

                Address address = new Address(faker.address().streetAddress(), 1200 + i);
                addressService.createAddress(address);

            }

            //Adds address to customer. Making use of all ids starting at 1.
            for (int i = 0; i < 50; i++) {

                Long customerId = i + 1L;
                Long addressId = 50L - i;
                customerService.addAddressToCustomer(customerId, addressId);

            }

            //Creating 32 parts and 8 subassemblies

            for (int i = 0; i < 8; i++) {

                List<Long> partIds = new ArrayList<>();

                for (int j = 0; j < 4; j++) {

                    OperationResult<Object> result = partService.createPart(new Part(faker.commerce().material()));
                    Part part = (Part) result.createdObject();
                    partIds.add(part.getPartId());

                }

                SubassemblyRequest subassemblyRequest = new SubassemblyRequest();
                subassemblyRequest.getPartIds().addAll(partIds);
                subassemblyRequest.setSubassemblyName("Subassembly Product " + random.nextInt(1000));
                subassemblyService.createSubassembly(subassemblyRequest);

                partIds.clear();

            }

            //Creating 4 machines, which 2 subassemblies each

            List<Long> subassemblyIds = new ArrayList<>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));

            for (int i = 0; i < 4; i++) {

                MachineRequest machineRequest = new MachineRequest();
                machineRequest.getSubassemblyIds().add(subassemblyIds.remove(0));
                machineRequest.getSubassemblyIds().add(subassemblyIds.remove(0));
                machineRequest.setMachineName(faker.commerce().productName());
                machineRequest.setPrice(random.nextInt(10000));

                machineService.createMachine(machineRequest);

            }

            //Creating 40 order lines

            List<Long> machineIds = List.of(1L, 2L, 3L, 4L);

            for (int i = 0; i < 40; i++) {

                long randomMachineId = machineIds.get(random.nextInt(machineIds.size()));
                orderLineService.createOrderLine(randomMachineId);

            }

            List<Long> orderLineIds = LongStream.rangeClosed(1, 40)
                    .boxed()
                    .collect(Collectors.toList());

            List<Long> customerIds = LongStream.rangeClosed(1, 10)
                    .boxed()
                    .collect(Collectors.toList());

            for (int i = 0; i < 10; i++) {

                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setCustomerId(customerIds.remove(0));
                orderRequest.getOrderLineIds().add(orderLineIds.remove(0));
                orderRequest.getOrderLineIds().add(orderLineIds.remove(0));
                orderRequest.getOrderLineIds().add(orderLineIds.remove(0));
                orderRequest.getOrderLineIds().add(orderLineIds.remove(0));

                orderService.createOrder(orderRequest);

            }
        };
        */

};
