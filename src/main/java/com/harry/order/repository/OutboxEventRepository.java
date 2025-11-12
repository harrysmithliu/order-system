package com.harry.order.repository;

import com.harry.order.model.po.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository  extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop100ByProcessedFalseOrderByCreatedAtAsc();
}
