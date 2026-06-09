package com.restaurant.pos.dto.employee;

import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.EmployeeRole;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeResponse toResponse(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        UUID id = null;
        String name = null;
        EmployeeRole role = null;
        String phone = null;
        String username = null;

        id = employee.getId();
        name = employee.getName();
        role = employee.getRole();
        phone = employee.getPhone();
        username = employee.getUsername();

        boolean isActive = false;

        EmployeeResponse employeeResponse = new EmployeeResponse( id, name, role, phone, username, isActive );

        return employeeResponse;
    }

    @Override
    public Employee toEntity(CreateEmployeeRequest request) {
        if ( request == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setName( request.getName() );
        employee.setRole( request.getRole() );
        employee.setPhone( request.getPhone() );
        employee.setUsername( request.getUsername() );
        employee.setActive( request.isActive() );

        return employee;
    }

    @Override
    public Employee toEntity(UpdateEmployeeRequest request) {
        if ( request == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setName( request.getName() );
        employee.setRole( request.getRole() );
        employee.setPhone( request.getPhone() );
        employee.setActive( request.isActive() );

        return employee;
    }
}
