package com.restaurant.pos.dto.payment;

import com.restaurant.pos.entity.Order;
import com.restaurant.pos.entity.Payment;
import com.restaurant.pos.entity.PaymentMethod;
import com.restaurant.pos.entity.PaymentStatus;
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
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponse toResponse(Payment payment) {
        if ( payment == null ) {
            return null;
        }

        UUID orderId = null;
        UUID id = null;
        PaymentMethod paymentMethod = null;
        BigDecimal amount = null;
        PaymentStatus status = null;
        LocalDateTime paidAt = null;

        orderId = paymentOrderId( payment );
        id = payment.getId();
        paymentMethod = payment.getPaymentMethod();
        amount = payment.getAmount();
        status = payment.getStatus();
        paidAt = payment.getPaidAt();

        PaymentResponse paymentResponse = new PaymentResponse( id, orderId, paymentMethod, amount, status, paidAt );

        return paymentResponse;
    }

    private UUID paymentOrderId(Payment payment) {
        Order order = payment.getOrder();
        if ( order == null ) {
            return null;
        }
        return order.getId();
    }
}
