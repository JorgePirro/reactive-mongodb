package com.challenge.reactivemongo.mapper;

import com.challenge.reactivemongo.domain.Customer;
import com.challenge.reactivemongo.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    CustomerDto customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
