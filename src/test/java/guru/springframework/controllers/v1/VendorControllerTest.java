package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controllers.RestResponseEntityExceptionHandler;
import guru.springframework.services.ResourceNotFoundException;
import guru.springframework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static guru.springframework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest {

    private static final String VENDOR_NAME = "Coca Cola Solutions";
    private static final String VENDOR_ID_ONE = "1";
    private static final String VENDOR_TWO_NAME = "Coca Cola Solutions";
    private static final String VENDOR_ID_TWO = "2";

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    VendorDTO vendorDTO;
    VendorDTO vendorDTOTwo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        vendorDTO = getVendorDTO(VENDOR_NAME, VENDOR_ID_ONE);
        vendorDTOTwo = getVendorDTO(VENDOR_TWO_NAME, VENDOR_ID_TWO);
    }

    @Test
    public void testGetAllVendors() throws Exception {
        // when
        List<VendorDTO> vendors = Arrays.asList(vendorDTO, vendorDTOTwo);
//        when(vendorService.getAllVendors()).thenReturn(vendors);
        given(vendorService.getAllVendors()).willReturn(vendors);

        mockMvc.perform(get(VendorController.VENDOR_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(vendors.size())));
    }

    @Test
    public void getVendorById() throws Exception {
        given(vendorService.getVendorById(anyLong())).willReturn(vendorDTO);

        mockMvc.perform(get(VendorController.VENDOR_BASE_URL + "/" + VENDOR_ID_ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())));
    }

    @Test
    public void getVendorByNonExistingId() throws Exception {
        given(vendorService.getVendorById(anyLong())).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(VendorController.VENDOR_BASE_URL + "/" + VENDOR_ID_ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNewVendor() throws Exception {
        given(vendorService.createNewVendor(vendorDTO)).willReturn(vendorDTO);

        mockMvc.perform(post(VendorController.VENDOR_BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(vendorDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())))
            .andExpect(jsonPath("$.vendor_url", equalTo(vendorDTO.getVendorUrl())));
    }

    @Test
    public void updateVendor() throws Exception {
        given(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).willReturn(vendorDTO);

        mockMvc.perform(put(VendorController.VENDOR_BASE_URL + "/" + VENDOR_ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())));
    }

    @Test
    public void patchVendor() throws Exception {
        given(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).willReturn(vendorDTO);

        mockMvc.perform(patch(VendorController.VENDOR_BASE_URL + "/" + VENDOR_ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())));
    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(delete(VendorController.VENDOR_BASE_URL + "/" + VENDOR_ID_ONE))
            .andExpect(status().isOk());

        verify(vendorService).deleteVendorById(anyLong());
    }

    private VendorDTO getVendorDTO(String vendorName, String vendorId) {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(vendorName);
        vendorDTO.setVendorUrl(VendorController.VENDOR_BASE_URL + "/" + vendorId);
        return vendorDTO;
    }

}
