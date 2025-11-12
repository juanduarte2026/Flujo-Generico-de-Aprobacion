package com.bank.backend.repository;

import com.bank.backend.entity.ApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {

    List<ApprovalHistory> findByRequestIdOrderByPerformedAtAsc(UUID requestId);

    List<ApprovalHistory> findByPerformedByOrderByPerformedAtDesc(String performedBy);
}