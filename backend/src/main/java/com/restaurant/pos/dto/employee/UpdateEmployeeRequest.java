package com.restaurant.pos.dto.employee;

import com.restaurant.pos.entity.EmployeeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateEmployeeRequest {

    @NotBlank(message = "Employee name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Employee role is required")
    private EmployeeRole role;

    @Pattern(regexp = "^$|[0-9]{10,11}", message = "Phone must be empty or contain 10-11 digits")
    private String phone;

    private boolean isActive;

    public UpdateEmployeeRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EmployeeRole getRole() { return role; }
    public void setRole(EmployeeRole role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
