package com.example.backendexam2023.Address;

import com.example.backendexam2023.Records.DeleteResult;
import com.example.backendexam2023.Service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerServiceIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    public void should_Return_Status_No_Content_When_Deleting_Address() throws Exception {
        Long addressId = 1L;
        DeleteResult deleteResult = new DeleteResult(true, null,Collections.emptyList());

        when(addressService.deleteAddress(addressId)).thenReturn(deleteResult);

        mockMvc.perform(delete("/api/address/" + addressId))
                .andExpect(status().isNoContent());
    }

}
