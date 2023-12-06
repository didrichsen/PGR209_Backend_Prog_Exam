package com.example.backendexam2023;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Order.Order;
import com.example.backendexam2023.OrderBatch.OrderBatch;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.OrderBatch.OrderBatchRequest;
import com.example.backendexam2023.Repository.*;
import com.example.backendexam2023.Service.OrderBatchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
            OrderRepository orderRepository,
            OrderBatchRepository orderBatchRepository,
            OrderBatchService orderBatchService
    ) {

        return args -> {

            //Parts for human robot
            Part rightHand = partRepository.save(new Part("Right Hand"));
            Part leftHand = partRepository.save(new Part("Left Hand"));
            Part leftElbow = partRepository.save(new Part("Left Elbow"));
            Part rightElbow = partRepository.save(new Part("Right Elbow"));
            Part stomachPlate = partRepository.save(new Part("Stomach Plate"));
            Part backPlate = partRepository.save(new Part("Back Plate"));
            Part skull = partRepository.save(new Part("Skull"));
            Part eyes = partRepository.save(new Part("Eyes"));
            Part mouth = partRepository.save(new Part("Mouth"));
            Part leftFoot = partRepository.save(new Part("Left Foot"));
            Part rightFoot = partRepository.save(new Part("Right Foot"));
            Part legRight = partRepository.save(new Part("Leg Right"));
            Part legLeft = partRepository.save(new Part("Leg Left"));
            Part leftThigh = partRepository.save(new Part("Left Thigh"));
            Part rightThigh = partRepository.save(new Part("Right Thigh"));

            //parts for dog robot
            Part dogHead = partRepository.save(new Part("Dog Head"));
            Part dogBody = partRepository.save(new Part("Dog Body"));
            Part dogTail = partRepository.save(new Part("Dog Tail"));
            Part dogLeg = partRepository.save(new Part("Dog Leg"));


            //Filling up list for subassemblies for human robot
            List<Subassembly> subassemblies = new ArrayList<>();

            Subassembly leftArm = subassemblyRepository.save(new Subassembly("Left Arm"));
            leftArm.getParts().add(leftHand);
            leftArm.getParts().add(leftElbow);
            subassemblyRepository.save(leftArm);
            subassemblies.add(leftArm);

            Subassembly rightArm = subassemblyRepository.save(new Subassembly("Right Arm"));
            rightArm.getParts().add(rightHand);
            rightArm.getParts().add(rightElbow);
            subassemblyRepository.save(rightArm);
            subassemblies.add(rightArm);

            Subassembly torso = subassemblyRepository.save(new Subassembly("Torso"));
            torso.getParts().add(stomachPlate);
            torso.getParts().add(backPlate);
            torso.getParts().add(skull);
            torso.getParts().add(eyes);
            torso.getParts().add(mouth);
            subassemblyRepository.save(torso);
            subassemblies.add(torso);

            Subassembly leftLeg = subassemblyRepository.save(new Subassembly("Left Leg"));
            leftLeg.getParts().add(leftFoot);
            leftLeg.getParts().add(legLeft);
            leftLeg.getParts().add(leftThigh);
            subassemblyRepository.save(leftLeg);
            subassemblies.add(leftLeg);

            Subassembly rightLeg = subassemblyRepository.save(new Subassembly("Right Leg"));
            rightLeg.getParts().add(rightFoot);
            rightLeg.getParts().add(legRight);
            rightLeg.getParts().add(rightThigh);
            subassemblyRepository.save(rightLeg);
            subassemblies.add(rightLeg);

            //Filling up list for robot dog
            List<Subassembly> dogSubassemblies = new ArrayList<>();

            Subassembly dogHeadAssembly = subassemblyRepository.save(new Subassembly("Dog Head Assembly"));
            dogHeadAssembly.getParts().add(dogHead);
            subassemblyRepository.save(dogHeadAssembly);
            dogSubassemblies.add(dogHeadAssembly);

            Subassembly dogBodyAssembly = subassemblyRepository.save(new Subassembly("Dog Body Assembly"));
            dogBodyAssembly.getParts().add(dogBody);
            dogBodyAssembly.getParts().add(dogTail);
            subassemblyRepository.save(dogBodyAssembly);
            dogSubassemblies.add(dogBodyAssembly);

            Subassembly dogLegAssembly = subassemblyRepository.save(new Subassembly("Dog Leg Assembly"));
            dogLegAssembly.getParts().add(dogLeg);
            subassemblyRepository.save(dogLegAssembly);
            dogSubassemblies.add(dogLegAssembly);

            //Creating Human robot
            Machine HumanRobot = new Machine("CogniMate", 1799);
            HumanRobot.getSubassemblies().addAll(subassemblies);
            Machine savedMachine = machineRepository.save(HumanRobot);

            //Creating Dog robot
            Machine dogRobot = new Machine("DogniMate", 1399);
            dogRobot.getSubassemblies().addAll(dogSubassemblies);
            Machine savedMachine2 = machineRepository.save(dogRobot);


            //Creating customers
            Customer customer = customerRepository.save(new Customer("Simen Didrichsen", "simen@simen.no"));
            Customer customer2 = customerRepository.save(new Customer("Arian Mathai", "arian@arian.no"));


            //Creating orders
            Order order = new Order();
            Order order2 = new Order();
            Order order3 = new Order();

            order.setCustomer(customer);
            order.setMachine(savedMachine);
            Order newOrder = orderRepository.save(order);

            order2.setCustomer(customer2);
            order2.setMachine(savedMachine2);
            Order newOrder2 = orderRepository.save(order2);

            order3.setCustomer(customer2);
            order3.setMachine(dogRobot);
            Order newOrder3 = orderRepository.save(order3);

            OrderBatchRequest orderBatchRequest = new OrderBatchRequest();
            orderBatchRequest.getOrderIds().add(newOrder3.getOrderId());
            orderBatchRequest.getOrderIds().add(newOrder2.getOrderId());

            orderBatchService.createOrderBatch(orderBatchRequest);

            OrderBatchRequest orderBatchRequest2 = new OrderBatchRequest();
            orderBatchRequest2.getOrderIds().add(newOrder.getOrderId());
            OrderBatch orderBatch = orderBatchService.createOrderBatch(orderBatchRequest2);

            System.out.println(orderBatch.getOrderBatchId());


        };
    }

}
