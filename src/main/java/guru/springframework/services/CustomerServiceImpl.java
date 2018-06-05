package guru.springframework.services;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.controllers.v1.CustomerController;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomer_url(CustomerController.CUSTOMER_BASE_URL + "/" + customer.getId());
                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        /*
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        Customer customer = optionalCustomer.get();
        return customerMapper.customerToCustomerDTO(customer);
        */
        return customerRepository
                .findById(id)
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomer_url(CustomerController.CUSTOMER_BASE_URL + "/" + customer.getId());
                    return customerDTO;
                }).orElseThrow(ResourceNotFoundException::new);

    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        return saveAndReturnCustomerDTO(customerMapper.customerDTOToCustomer(customerDTO));
    }

    private CustomerDTO saveAndReturnCustomerDTO(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);

        CustomerDTO returnedCustomer = customerMapper.customerToCustomerDTO(savedCustomer);
        returnedCustomer.setCustomer_url(CustomerController.CUSTOMER_BASE_URL + "/" + savedCustomer.getId());

        return returnedCustomer;
    }

    @Override
    public CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        customer.setId(id);

        return saveAndReturnCustomerDTO(customer);
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {
            if(customerDTO.getFirstname() != null) {
                customer.setFirstname(customerDTO.getFirstname());
            }
            if(customerDTO.getLastname() != null) {
                customer.setLastname(customerDTO.getLastname());
            }
            return saveAndReturnCustomerDTO(customer);
        }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
