package com.restaurant.pos.dto.customer;

import com.restaurant.pos.entity.Customer;
import com.restaurant.pos.entity.MembershipLevel;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerResponse toResponse(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        String membershipLevelName = null;
        UUID id = null;
        String name = null;
        String phone = null;
        String email = null;
        int totalPoints = 0;
        BigDecimal totalSpent = null;
        LocalDateTime createdAt = null;

        membershipLevelName = customerMembershipLevelLevelName( customer );
        id = customer.getId();
        name = customer.getName();
        phone = customer.getPhone();
        email = customer.getEmail();
        totalPoints = customer.getTotalPoints();
        totalSpent = customer.getTotalSpent();
        createdAt = customer.getCreatedAt();

        CustomerResponse customerResponse = new CustomerResponse( id, name, phone, email, totalPoints, totalSpent, membershipLevelName, createdAt );

        return customerResponse;
    }

    @Override
    public Customer toEntity(CreateCustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setName( request.getName() );
        customer.setPhone( request.getPhone() );
        customer.setEmail( request.getEmail() );

        return customer;
    }

    @Override
    public Customer toEntity(UpdateCustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setName( request.getName() );
        customer.setPhone( request.getPhone() );
        customer.setEmail( request.getEmail() );

        return customer;
    }

    private String customerMembershipLevelLevelName(Customer customer) {
        MembershipLevel membershipLevel = customer.getMembershipLevel();
        if ( membershipLevel == null ) {
            return null;
        }
        return membershipLevel.getLevelName();
    }
}
