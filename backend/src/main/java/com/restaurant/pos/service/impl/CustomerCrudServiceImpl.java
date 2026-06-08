package com.restaurant.pos.service.impl;

import com.restaurant.pos.dto.common.PagedResponse;
import com.restaurant.pos.dto.customer.CreateCustomerRequest;
import com.restaurant.pos.dto.customer.CustomerResponse;
import com.restaurant.pos.dto.customer.UpdateCustomerRequest;
import com.restaurant.pos.entity.Customer;
import com.restaurant.pos.entity.MembershipLevel;
import com.restaurant.pos.exception.BusinessRuleViolationException;
import com.restaurant.pos.exception.ResourceNotFoundException;
import com.restaurant.pos.repository.CustomerRepository;
import com.restaurant.pos.repository.MembershipLevelRepository;
import com.restaurant.pos.service.CustomerCrudService;
import com.restaurant.pos.service.PageResponseFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.UUID;

@Service
public class CustomerCrudServiceImpl implements CustomerCrudService {

    private final CustomerRepository customerRepository;
    private final MembershipLevelRepository membershipLevelRepository;

    public CustomerCrudServiceImpl(CustomerRepository customerRepository,
                                   MembershipLevelRepository membershipLevelRepository) {
        this.customerRepository = customerRepository;
        this.membershipLevelRepository = membershipLevelRepository;
    }

    @Override
    public PagedResponse<CustomerResponse> findAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return PageResponseFactory.fromPage(customerRepository.findAll(pageable), this::toResponse);
        }
        String q = keyword.trim().toLowerCase();
        return PageResponseFactory.fromList(customerRepository.findAll().stream()
                .filter(c -> contains(c.getName(), q) || contains(c.getPhone(), q) || contains(c.getEmail(), q))
                .map(this::toResponse)
                .toList(), pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public CustomerResponse findById(UUID id) {
        return toResponse(findCustomer(id));
    }

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        validateUniqueContact(null, request.getPhone(), request.getEmail());
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(request.getName());
        customer.setPhone(blankToNull(request.getPhone()));
        customer.setEmail(blankToNull(request.getEmail()));
        customer.setMembershipLevel(lowestLevel());
        customer.setTotalPoints(0);
        customer.setTotalSpent(BigDecimal.ZERO);
        return toResponse(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CustomerResponse update(UUID id, UpdateCustomerRequest request) {
        Customer customer = findCustomer(id);
        validateUniqueContact(id, request.getPhone(), request.getEmail());
        customer.setName(request.getName());
        customer.setPhone(blankToNull(request.getPhone()));
        customer.setEmail(blankToNull(request.getEmail()));
        return toResponse(customerRepository.save(customer));
    }

    private Customer findCustomer(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    private void validateUniqueContact(UUID currentId, String phone, String email) {
        if (phone != null && !phone.isBlank()) {
            customerRepository.findByPhone(phone)
                    .filter(existing -> !existing.getId().equals(currentId))
                    .ifPresent(existing -> {
                        throw new BusinessRuleViolationException("Phone already exists.");
                    });
        }
        if (email != null && !email.isBlank()) {
            customerRepository.findByEmail(email)
                    .filter(existing -> !existing.getId().equals(currentId))
                    .ifPresent(existing -> {
                        throw new BusinessRuleViolationException("Email already exists.");
                    });
        }
    }

    private MembershipLevel lowestLevel() {
        return membershipLevelRepository.findAll().stream()
                .min(Comparator.comparing(MembershipLevel::getMinSpend))
                .orElse(null);
    }

    private CustomerResponse toResponse(Customer customer) {
        String levelName = customer.getMembershipLevel() == null ? null : customer.getMembershipLevel().getLevelName();
        return new CustomerResponse(customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(),
                customer.getTotalPoints(), customer.getTotalSpent(), levelName, customer.getCreatedAt());
    }

    private boolean contains(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
