package com.restaurant.pos.repository;

import com.restaurant.pos.entity.PromotionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromotionRuleRepository extends JpaRepository<PromotionRule, UUID> {

    List<PromotionRule> findByEventPromotionId(UUID eventId);
}
