package com.restaurant.pos.repository;

import com.restaurant.pos.entity.EventPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventPromotionRepository extends JpaRepository<EventPromotion, UUID> {

    List<EventPromotion> findByIsActiveTrue();

    List<EventPromotion> findByCreatedById(UUID employeeId);

    /** Returns all active promotions valid at a given point in time (BR-05). */
    @Query("SELECT DISTINCT e FROM EventPromotion e LEFT JOIN FETCH e.rules " +
           "WHERE e.isActive = true " +
           "AND e.startDate <= :targetDate " +
           "AND e.endDate >= :targetDate")
    List<EventPromotion> findCurrentlyActive(@Param("targetDate") LocalDateTime targetDate);
}
