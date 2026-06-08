package com.restaurant.pos.dto.order;

import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "tableId", source = "table.id")
    @Mapping(target = "tableNumber", source = "table.tableNumber")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", source = "employee.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "items", source = "orderItems")
    OrderResponse toResponse(Order order);

    @Mapping(target = "menuItemId", source = "menuItem.id")
    @Mapping(target = "menuItemName", source = "menuItem.dishName")
    @Mapping(target = "notes", ignore = true)
    OrderItemResponse toResponse(OrderItem item);
}
