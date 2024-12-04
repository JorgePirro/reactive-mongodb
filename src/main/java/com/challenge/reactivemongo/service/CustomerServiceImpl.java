package com.challenge.reactivemongo.service;

import com.challenge.reactivemongo.mapper.CustomerMapper;
import com.challenge.reactivemongo.model.CustomerDto;
import com.challenge.reactivemongo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {



    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDto> listCustomers() {
        return customerRepository.findAll()
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> saveNewCustomer(CustomerDto CustomerDto) {
        return customerRepository.save(customerMapper.customerDtoToCustomer(CustomerDto))
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> saveNewCustomer(Mono<CustomerDto> CustomerDto) {
        return CustomerDto.map(customerMapper::customerDtoToCustomer)
                .flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> updateCustomer(String customerId, CustomerDto CustomerDto) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    customer.setCustomerName(CustomerDto.getCustomerName());
                    return customer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<CustomerDto> patchCustomer(String customerId, CustomerDto CustomerDto) {
        return customerRepository.findById(customerId)
                .map(customer -> {
                    if (StringUtils.hasText(CustomerDto.getCustomerName())){
                        customer.setCustomerName(CustomerDto.getCustomerName());
                    }
                    return customer;
                }).flatMap(customerRepository::save)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public Mono<Void> deleteCustomerById(String customerId) {
        return customerRepository.deleteById(customerId);
    }
}
