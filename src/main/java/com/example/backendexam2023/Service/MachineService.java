package com.example.backendexam2023.Service;

import com.example.backendexam2023.Model.OrderLine.OrderLine;
import com.example.backendexam2023.Model.Part.Part;
import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Records.OperationResultDeletion;
import com.example.backendexam2023.Records.OperationResult;
import com.example.backendexam2023.Model.Machine.Machine;
import com.example.backendexam2023.Model.Machine.MachineRequest;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.MachineRepository;
import com.example.backendexam2023.Repository.OrderLineRepository;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MachineService {

    private final MachineRepository machineRepository;
    private final SubassemblyRepository subassemblyRepository;
    private final OrderLineRepository orderLineRepository;
    private final PartRepository partRepository;

    @Autowired
    public MachineService(MachineRepository machineRepository, SubassemblyRepository subassemblyRepository, OrderLineRepository orderLineRepository, PartRepository partRepository){
        this.machineRepository = machineRepository;
        this.subassemblyRepository = subassemblyRepository;
        this.orderLineRepository = orderLineRepository;
        this.partRepository = partRepository;

    }

    public Machine getMachineById(Long id){
        return machineRepository.findById(id).orElse(null);
    }

    public List<Machine> getMachinesPageable(int pageNumber) {
        return machineRepository.findAll(PageRequest.of(pageNumber, 5)).stream().toList();
    }

    public OperationResult<Object> createMachine(MachineRequest machineRequest){

        if (machineRequest.getMachineName() == null || machineRequest.getMachineName().trim().isEmpty()){
            return new OperationResult<>(false,"Machine needs a name", null);
        }

        if (machineRequest.getPrice() == null || machineRequest.getPrice() <= 0) {
            return new OperationResult<>(false,"Machine needs a price", null);
        }

        Machine machine = new Machine(machineRequest.getMachineName(), machineRequest.getPrice());

        List<Subassembly> subassemblies = new ArrayList<>();

        if(machineRequest.getSubassemblyIds().isEmpty()){
            return new OperationResult<>(false, "A machine needs to at least have one subassembly", null);
        }

        for(Long subassemblyId : machineRequest.getSubassemblyIds()){
            Subassembly subassembly = subassemblyRepository.findById(subassemblyId).orElse(null);
            if(subassembly == null){
                return new OperationResult<>(false, "Couldn't find subassembly with id " + subassemblyId, null);
            }
            subassemblies.add(subassembly);
        }

        machine.setSubassemblies(subassemblies);

        return new OperationResult<>(true,null, machineRepository.save(machine));

    }

    public DeleteResult deleteMachineById(Long id){

        Machine machineToDelete = getMachineById(id);

        if(machineToDelete == null){
            return new DeleteResult(false ,"Couldn't find machine with id " + id,null);
        }

        Optional<List<OrderLine>> orderLinesOptional = orderLineRepository.findByMachine(machineToDelete);

        if(orderLinesOptional.isPresent()){
            List<OrderLine> orderLinesRegisteredWithMachine = orderLinesOptional.get();
            List<Long> orderLineIds = orderLinesRegisteredWithMachine.stream()
                    .map(OrderLine::getOrderLineId)
                    .collect(Collectors.toList());
            return new DeleteResult(false, "Can't delete machine. Machine placed in order lines.",orderLineIds);
        }

        machineRepository.deleteById(machineToDelete.getMachineId());

        List<Subassembly> subassemblies = machineToDelete.getSubassemblies();

        for (Subassembly subassembly : subassemblies){
            List<Part> parts = subassembly.getParts();
            for(Part part : parts){
                partRepository.deleteById(part.getPartId());
            }
            subassemblyRepository.deleteById(subassembly.getSubassemblyId());
        }

        return new DeleteResult(true,null,null);
    }

    public OperationResult<Object> updateMachine(Long machineId, Machine newMachine){
        Machine machineToUpdate = getMachineById(machineId);

        if (machineToUpdate == null) {
            return new OperationResult<>(false,"Couldn't find machine with id " + machineId, null);
        }

        if (newMachine.getMachineName() != null) machineToUpdate.setMachineName(newMachine.getMachineName());
        if (newMachine.getPrice() != null) machineToUpdate.setPrice(newMachine.getPrice());
        if (!newMachine.getSubassemblies().isEmpty()) machineToUpdate.getSubassemblies().addAll(newMachine.getSubassemblies());

        return new OperationResult<>(true, null,machineRepository.save(machineToUpdate));
    }

}
