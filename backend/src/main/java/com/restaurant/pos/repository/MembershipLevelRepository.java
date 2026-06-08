package com.restaurant.pos.repository;

import com.restaurant.pos.entity.MembershipLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipLevelRepository extends JpaRepository<MembershipLevel, UUID> {

    Optional<MembershipLevel> findByLevelName(String levelName);

    /** Used for membership upgrade: find the highest tier the customer qualifies for. */
    List<MembershipLevel> findByMinSpendLessThanEqualOrderByMinSpendDesc(BigDecimal totalSpent);
}
