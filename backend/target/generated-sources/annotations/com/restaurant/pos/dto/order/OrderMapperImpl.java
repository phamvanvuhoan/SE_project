package com.restaurant.pos.dto.order;

import com.restaurant.pos.entity.Customer;
import com.restaurant.pos.entity.Employee;
import com.restaurant.pos.entity.MenuItem;
import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.OrderItem;
import com.restaurant.pos.entity.OrderStatus;
import com.restaurant.pos.entity.RestaurantTable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-09T14:43:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.11 (Ubuntu)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponse toResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        UUID tableId = null;
        String tableNumber = null;
        UUID employeeId = null;
        String employeeName = null;
        UUID customerId = null;
        String customerName = null;
        List<OrderItemResponse> items = null;
        UUID id = null;
        OrderStatus status = null;
        BigDecimal subtotal = null;
        BigDecimal promotionDiscount = null;
        BigDecimal pointDiscount = null;
        BigDecimal totalAmount = null;
        LocalDateTime orderTime = null;

        tableId = orderTableId( order );
        tableNumber = orderTableTableNumber( order );
        employeeId = orderEmployeeId( order );
        employeeName = orderEmployeeName( order );
        customerId = orderCustomerId( order );
        customerName = orderCustomerName( order );
        items = orderItemListToOrderItemResponseList( order.getOrderItems() );
        id = order.getId();
        status = order.getStatus();
        subtotal = order.getSubtotal();
        promotionDiscount = order.getPromotionDiscount();
        pointDiscount = order.getPointDiscount();
        totalAmount = order.getTotalAmount();
        orderTime = order.getOrderTime();

        OrderResponse orderResponse = new OrderResponse( id, tableId, tableNumber, employeeId, employeeName, customerId, customerName, status, subtotal, promotionDiscount, pointDiscount, totalAmount, orderTime, items );

        return orderResponse;
    }

    @Override
    public OrderItemResponse toResponse(OrderItem item) {
        if ( item == null ) {
            return null;
        }

        UUID menuItemId = null;
        String menuItemName = null;
        UUID id = null;
        int quantity = 0;
        BigDecimal unitPrice = null;
        BigDecimal subtotal = null;
        boolean promotionalItem = false;

        menuItemId = itemMenuItemId( item );
        menuItemName = itemMenuItemDishName( item );
        id = item.getId();
        quantity = item.getQuantity();
        unitPrice = item.getUnitPrice();
        subtotal = item.getSubtotal();
        promotionalItem = item.isPromotionalItem();

        String notes = null;

        OrderItemResponse orderItemResponse = new OrderItemResponse( id, menuItemId, menuItemName, quantity, unitPrice, subtotal, notes, promotionalItem );

        return orderItemResponse;
    }

    private UUID orderTableId(Order order) {
        RestaurantTable table = order.getTable();
        if ( table == null ) {
            return null;
        }
        return table.getId();
    }

    private String orderTableTableNumber(Order order) {
        RestaurantTable table = order.getTable();
        if ( table == null ) {
            return null;
        }
        return table.getTableNumber();
    }

    private UUID orderEmployeeId(Order order) {
        Employee employee = order.getEmployee();
        if ( employee == null ) {
            return null;
        }
        return employee.getId();
    }

    private String orderEmployeeName(Order order) {
        Employee employee = order.getEmployee();
        if ( employee == null ) {
            return null;
        }
        return employee.getName();
    }

    private UUID orderCustomerId(Order order) {
        Customer customer = order.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private String orderCustomerName(Order order) {
        Customer customer = order.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getName();
    }

    protected List<OrderItemResponse> orderItemListToOrderItemResponseList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponse> list1 = new ArrayList<OrderItemResponse>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( toResponse( orderItem ) );
        }

        return list1;
    }

    private UUID itemMenuItemId(OrderItem orderItem) {
        MenuItem menuItem = orderItem.getMenuItem();
        if ( menuItem == null ) {
            return null;
        }
        return menuItem.getId();
    }

    private String itemMenuItemDishName(OrderItem orderItem) {
        MenuItem menuItem = orderItem.getMenuItem();
        if ( menuItem == null ) {
            return null;
        }
        return menuItem.getDishName();
    }
}
