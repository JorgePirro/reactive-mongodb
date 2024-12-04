package com.challenge.reactivemongo.service;

import com.challenge.reactivemongo.model.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDto> listCustomers();
    Mono<CustomerDto> getCustomerById(String customerId);
    Mono<CustomerDto> saveNewCustomer(CustomerDto CustomerDto);
    Mono<CustomerDto> saveNewCustomer(Mono<CustomerDto> CustomerDto);
    Mono<CustomerDto> updateCustomer(String customerId, CustomerDto CustomerDto);
    Mono<CustomerDto> patchCustomer(String customerId, CustomerDto CustomerDto);
    Mono<Void> deleteCustomerById(String customerId);
}
