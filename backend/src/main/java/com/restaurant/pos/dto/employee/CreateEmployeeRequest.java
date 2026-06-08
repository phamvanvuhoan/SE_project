package com.restaurant.pos.dto.employee;

import com.restaurant.pos.entity.EmployeeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateEmployeeRequest {

    @NotBlank(message = "Employee name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Employee role is required")
    private EmployeeRole role;

    @Pattern(regexp = "^$|[0-9]{10,11}", message = "Phone must be empty or contain 10-11 digits")
    private String phone;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private boolean isActive = true;

    public CreateEmployeeRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public EmployeeRole getRole() { return role; }
    public void setRole(EmployeeRole role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
