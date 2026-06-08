package com.restaurant.pos.repository;

import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByUsername(String username);

    Optional<Employee> findByPhone(String phone);

    List<Employee> findByRole(EmployeeRole role);

    List<Employee> findByIsActiveTrue();
}
