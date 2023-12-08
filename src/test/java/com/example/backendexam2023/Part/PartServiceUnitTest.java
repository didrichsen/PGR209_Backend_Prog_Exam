package com.example.backendexam2023.Part;

import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Model.Part;
import com.example.backendexam2023.Model.Subassembly.Subassembly;
import com.example.backendexam2023.Repository.PartRepository;
import com.example.backendexam2023.Repository.SubassemblyRepository;
import com.example.backendexam2023.Service.PartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest
public class PartServiceUnitTest {

    @MockBean
    private PartRepository partRepository;

    @MockBean
    private SubassemblyRepository subassemblyRepository;

    @Autowired
    private PartService partService;

    @Test
    void shouldReturnPartWhenCreatingPart(){

        Part part = new Part("screw");

        when(partRepository.save(any(Part.class))).thenReturn(part);

        Part fork = partService.createPart(new Part("Fork"));

        assert fork.getPartName().equals(part.getPartName());

    }

    @Test
    void shouldReturnErrorIfPartNameIsNull() {

        Part partWithNullName = new Part();

        assertThrows(RuntimeException.class, () -> partService.createPart(partWithNullName));

    }

    @Test
    void Part_Shall_Not_Be_Deleted_Because_Its_In_Use() {

        Part part = new Part();
        part.setPartId(1L);

        Subassembly subassembly = new Subassembly();
        subassembly.getParts().add(part);

        List<Subassembly> subassembliesToCheck = List.of(subassembly);

        when(subassemblyRepository.findAll()).thenReturn(subassembliesToCheck);
        when(partRepository.findById(part.getPartId())).thenReturn(Optional.of(part));


        DeleteResult deleteResult = partService.deletePartById(1L);

        assertFalse(deleteResult.isSuccess());
        assertFalse(deleteResult.getIds().isEmpty());

    }

    @Test
    void Part_Shall_Not_Be_Deleted_Because_It_Does_Not_Exist() {

        Part part = new Part();
        part.setPartId(1L);

        Subassembly subassembly = new Subassembly();
        subassembly.getParts().add(part);

        List<Subassembly> subassembliesToCheck = List.of(subassembly);

        when(subassemblyRepository.findAll()).thenReturn(subassembliesToCheck);
        when(partRepository.findById(part.getPartId())).thenReturn(Optional.of(part));


        DeleteResult deleteResult = partService.deletePartById(2L);

        assertFalse(deleteResult.isSuccess());

    }

    @Test
    void Part_Shall_Not_Be_Deleted() {

        Part part = new Part();
        part.setPartId(1L);

        Part partToDelete = new Part();
        part.setPartId(2L);

        Subassembly subassembly = new Subassembly();
        subassembly.getParts().add(part);

        List<Subassembly> subassembliesToCheck = List.of(subassembly);

        when(subassemblyRepository.findAll()).thenReturn(subassembliesToCheck);
        when(partRepository.findById(partToDelete.getPartId())).thenReturn(Optional.of(partToDelete));


        DeleteResult deleteResult = partService.deletePartById(2L);

        assert (deleteResult.isSuccess());
        assert (deleteResult.getIds().isEmpty());

    }











}
