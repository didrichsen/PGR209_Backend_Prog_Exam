package com.example.backendexam2023.Service;

import com.example.backendexam2023.DeleteResult;
import com.example.backendexam2023.Model.Customer.Customer;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final SubassemblyRepository subassemblyRepository;
    private final OrderLineRepository orderLineRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, SubassemblyRepository subassemblyRepository, OrderLineRepository orderLineRepository){
        this.machineRepository = machineRepository;
        this.subassemblyRepository = subassemblyRepository;
        this.orderLineRepository = orderLineRepository;

    }

    public Machine getMachineById(Long id){
        return machineRepository.findById(id).orElse(null);
    }

    public List<Machine> getMachinesPageable(int pageNumber) {
        return machineRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public Machine createMachine(MachineRequest machineRequest){
        Machine machine = new Machine(machineRequest.getMachineName(), machineRequest.getPrice());

        List<Subassembly> subassemblies = new ArrayList<>();

        if(machineRequest.getSubassemblyIds().isEmpty()){
            throw new RuntimeException();
        }

        for(Long partId : machineRequest.getSubassemblyIds()){
            Subassembly subassembly = subassemblyRepository.findById(partId).orElse(null);
            subassemblies.add(subassembly);
        }

        machine.setSubassemblies(subassemblies);

        return machineRepository.save(machine);

    }

    public DeleteResult deleteMachineById(Long id){

        List<OrderLine> orderLinesToCheck = orderLineRepository.findAll();
        List<Long> orderLinesRegisteredWithMachine = new ArrayList<>();

        boolean isInUse = false;

        Machine machineToDelete = getMachineById(id);

        if(machineToDelete == null){
            return new DeleteResult(false, Collections.emptyList());
        }

        for (OrderLine orderLine : orderLinesToCheck) {
            Machine machine = orderLine.getMachine();
                if(Objects.equals(machine, machineToDelete)){
                    isInUse = true;
                    orderLinesRegisteredWithMachine.add(orderLine.getOrderLineId());
                }
            }

        if(isInUse){
            return new DeleteResult(false,orderLinesRegisteredWithMachine);
        }

        machineRepository.deleteById(machineToDelete.getMachineId());

        return new DeleteResult(true,Collections.emptyList());
    }

    public Machine updateMachine(Long machineId, Machine newMachine){
        Machine machineToUpdate = getMachineById(machineId);

        if (machineToUpdate == null) throw new RuntimeException("Could not find machine with id " + machineId);

        if (newMachine.getMachineName() != null) machineToUpdate.setMachineName(newMachine.getMachineName());
        if (newMachine.getPrice() != null) machineToUpdate.setPrice(newMachine.getPrice());
        return machineRepository.save(machineToUpdate);
    }

    




}
