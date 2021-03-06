package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.controllers.RestResponseEntityExceptionHandler;
import guru.springframework.services.CustomerService;
import guru.springframework.services.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {
    private static final String FIRST_NAME_ONE = "TestFN";
    private static final String LAST_NAME_ONE = "TestLN";
    private static final String ID_ONE = "1";
    private static final String FIRST_NAME_TWO = "TestTwoFN";
    private static final String LAST_NAME_TWO = "TestTwoLN";
    private static final String INVALID_ID = "666";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        // given
        CustomerDTO customerOne = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);

        CustomerDTO customerTwo = getCustomerDTO(FIRST_NAME_TWO, LAST_NAME_TWO);

        // when
        List<CustomerDTO> customers = Arrays.asList(customerOne, customerTwo);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // then
        mockMvc.perform(
                get(CustomerController.CUSTOMER_BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(customers.size())));
    }

    private CustomerDTO getCustomerDTO(String firstName, String lastName) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(firstName);
        customerDTO.setLastname(lastName);
        return customerDTO;
    }

    @Test
    public void testGetCustomerById() throws Exception {
        //given
        CustomerDTO customerDTO = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);

        // when
        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        // then
        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME_ONE)));
    }

    @Test
    public void createNewCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);

        CustomerDTO returnCustomerDTO = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);
        returnCustomerDTO.setCustomer_url(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE);

        // when
        when(customerService.createNewCustomer(customerDTO)).thenReturn(returnCustomerDTO);

        // then
        mockMvc.perform(post(CustomerController.CUSTOMER_BASE_URL + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME_ONE)))
                .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)));

        // reference to debug the body and its contents.
//        String response = mockMvc.perform(post(CUSTOMERS_API_URL)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(asJsonString(customerDTO)))
//                                .andReturn().getResponse().getContentAsString();
//        System.out.println(response);
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);

        CustomerDTO updatedCustomerDTO = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);
        updatedCustomerDTO.setCustomer_url(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE);

        // when
        when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(updatedCustomerDTO);

        // then
        mockMvc.perform(put(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME_ONE)))
                .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)));
    }

    @Test
    public void testPatchCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME_ONE);

        CustomerDTO updatedCustomerDTO = getCustomerDTO(FIRST_NAME_ONE, LAST_NAME_ONE);
        updatedCustomerDTO.setCustomer_url(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE);

        // when
        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(updatedCustomerDTO);

        // then
        mockMvc.perform(patch(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME_ONE)))
                .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_BASE_URL + "/" + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomerById(anyLong());
    }

    @Test
    public void testCustomerNotFound() throws Exception {
        when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CustomerController.CUSTOMER_BASE_URL + "/" + INVALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
