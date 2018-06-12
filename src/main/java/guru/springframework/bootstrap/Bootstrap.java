package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createCategories();
        createCustomers();
        createVendors();
    }

    private void createCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);

        System.out.println("Categories loaded = " + categoryRepository.count());
    }

    private void createCustomers() {
        Customer customer = new Customer();
        customer.setFirstname("Joe");
        customer.setLastname("Smith");
//        customer.setId(1L);

        customerRepository.save(customer);

        Customer customerTwo = new Customer();
        customerTwo.setFirstname("Bob");
        customerTwo.setLastname("Franco");
//        customerTwo.setId(2L);

        customerRepository.save(customerTwo);

        System.out.println("Customers loaded = " + customerRepository.count());
    }

    private void createVendors() {
        Vendor vendor = new Vendor();
        vendor.setName("Coca Cola Solutions");
        vendorRepository.save(vendor);

        Vendor vendorTwo = new Vendor();
        vendor.setName("Dell Solutions");
        vendorRepository.save(vendorTwo);

        System.out.println("Vendors loaded = " + vendorRepository.count());
    }
}
