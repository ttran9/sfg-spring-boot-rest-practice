package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {
    public static final String FIRST_NAME = "Jim";
    public static final String LAST_NAME = "Koernig";
    public static final Long ID = 1L;
    public static final String CUSTOMER_API_URL = "/api/v1/customers/";

    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(customerMapper, customerRepository);
    }

    @Test
    public void getAllCustomers() throws Exception {
        // given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer());

        // when
        when(customerRepository.findAll()).thenReturn(customers);
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        // then
        assertEquals(customers.size(), customerDTOS.size());

    }

    @Test
    public void getCustomerById() throws Exception {

        // given
        Customer customer = new Customer();
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);
        customer.setId(ID);

        // when
        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));

        // then
        CustomerDTO customerDTO = customerService.getCustomerById(ID);
        assertEquals(FIRST_NAME, customerDTO.getFirstname());
        assertEquals(LAST_NAME, customerDTO.getLastname());
    }

    @Test
    public void createNewCustomer() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME);
        customerDTO.setLastname(LAST_NAME);
        customerDTO.setCustomer_url(CUSTOMER_API_URL + ID);

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstname(FIRST_NAME);
        savedCustomer.setLastname(LAST_NAME);
        savedCustomer.setId(ID);

        // when
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        CustomerDTO savedCustomerDTO = customerService.createNewCustomer(customerDTO);

        // then
        assertEquals(customerDTO.getFirstname(), savedCustomerDTO.getFirstname());
        assertEquals(customerDTO.getLastname(), savedCustomerDTO.getLastname());
        assertEquals(customerDTO.getCustomer_url(), savedCustomerDTO.getCustomer_url());
    }

    @Test
    public void saveCustomerByDTO() throws Exception {
        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname(FIRST_NAME);
        customerDTO.setLastname(LAST_NAME);
        customerDTO.setCustomer_url(CUSTOMER_API_URL + ID);

        Customer savedCustomer = new Customer();
        savedCustomer.setLastname(LAST_NAME);
        savedCustomer.setFirstname(FIRST_NAME);
        savedCustomer.setId(ID);

        // when
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        CustomerDTO savedCustomerDTO = customerService.saveCustomerByDTO(ID, customerDTO);

        // then
        assertEquals(customerDTO.getFirstname(), savedCustomerDTO.getFirstname());
        assertEquals(customerDTO.getLastname(), savedCustomerDTO.getLastname());
        assertEquals(customerDTO.getCustomer_url(), savedCustomerDTO.getCustomer_url());
    }

    @Test
    public void deleteCustomerById() throws Exception {
        // given
        Customer savedCustomer = new Customer();
        savedCustomer.setLastname(LAST_NAME);
        savedCustomer.setFirstname(FIRST_NAME);

        // when
        customerRepository.save(savedCustomer);
        long numberOfCustomers = customerRepository.count();
        customerRepository.deleteById(ID);
        int expectedNumberOfCustomers = 1;

        // then
        assertNotEquals(expectedNumberOfCustomers, numberOfCustomers);
        verify(customerRepository, times(1)).deleteById(anyLong());

        /**
         * jt's solution below
         */
//        customerRepository.deleteById(ID);
//        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}
