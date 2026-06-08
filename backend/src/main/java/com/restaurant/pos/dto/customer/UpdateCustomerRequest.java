package com.restaurant.pos.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateCustomerRequest {

    @NotBlank(message = "Customer name cannot be blank")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "[0-9]{10,11}", message = "Phone must contain 10-11 digits")
    private String phone;

    @Email(message = "Email must be a valid email address")
    private String email;

    public UpdateCustomerRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
