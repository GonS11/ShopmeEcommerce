package com.shopme.admin.customer;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CustomerService
{
    public static final int CUSTOMER_PER_PAGE = 10;

    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private CountryRepository countryRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void listByPage(int pageNum, PagingAndSortingHelper helper)
    {
        helper.listEntities(pageNum, CUSTOMER_PER_PAGE, customerRepo);
    }

    public void updateCustomerEnabledStatus(Integer id, boolean enabled)
    {
        customerRepo.updateEnabledStatus(id, enabled);
    }

    public Customer get(Integer id) throws CustomerNotFoundException
    {
        return customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Could not find any customer with ID " + id));
    }

    public List<Country> listAllCountries()
    {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(Integer id, String email)
    {
        Customer existCustomer = customerRepo.findByEmail(email);
        return existCustomer == null || Objects.equals(existCustomer.getId(), id);
    }

    public void save(Customer customerInForm)
    {
        boolean isUpdating = (customerInForm.getId() != null);

        if (isUpdating)
        {
            Customer customerInDB = customerRepo.findById(customerInForm.getId())
                    .orElseThrow(() -> new CustomerNotFoundException(
                            "Could not find any customer with ID " + customerInForm.getId()));

            if (!customerInForm.getPassword().isEmpty())
            {
                String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
                customerInForm.setPassword(encodedPassword);
            }
            else
            {
                customerInForm.setPassword(customerInDB.getPassword());
            }

            customerInForm.setEnabled(customerInDB.isEnabled());
            customerInForm.setCreatedTime(customerInDB.getCreatedTime());
            customerInForm.setVerificationCode(customerInDB.getVerificationCode());
            customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
            customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());
        }
        else
        {
            encodePassword(customerInForm);
        }

        customerRepo.save(customerInForm);
    }

    private void encodePassword(Customer customer)
    {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public void delete(Integer id) throws CustomerNotFoundException
    {
        Long count = customerRepo.countById(id);
        if (count == null || count == 0)
        {
            throw new CustomerNotFoundException("Could not find any customer with ID " + id);
        }

        customerRepo.deleteById(id);
    }
}