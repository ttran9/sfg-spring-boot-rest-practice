package guru.springframework.controllers.v1;

import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {
    private static final String FIRST_NAME_ONE = "TestFN";
    private static final String LAST_NAME_ONE = "TestLN";
    private static final String ID_ONE = "1";
    private static final String FIRST_NAME_TWO = "TestTwoFN";
    private static final String LAST_NAME_TWO = "TestTwoLN";
    private static final String CUSTOMERS_API_URL = "/api/v1/customers/";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        // given
        CustomerDTO customerOne = new CustomerDTO();
        customerOne.setFirstname(FIRST_NAME_ONE);
        customerOne.setLastname(LAST_NAME_ONE);

        CustomerDTO customerTwo = new CustomerDTO();
        customerTwo.setFirstname(FIRST_NAME_TWO);
        customerTwo.setLastname(LAST_NAME_TWO);

        // when
        List<CustomerDTO> customers = Arrays.asList(customerOne, customerTwo);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // then
        mockMvc.perform(
                get(CUSTOMERS_API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(customers.size())));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME_ONE);
        customerDTO.setLastname(LAST_NAME_ONE);

        // when
        when(customerService.getCustomerById(anyLong())).thenReturn(customerDTO);

        // then
        mockMvc.perform(get(CUSTOMERS_API_URL + ID_ONE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME_ONE)))
                .andExpect(jsonPath("$.lastname", equalTo(LAST_NAME_ONE)));
    }
}