package com.bank.backend.repository;

import com.bank.backend.entity.ApprovalRequest;
import com.bank.backend.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, UUID> {

    List<ApprovalRequest> findByRequesterOrderByCreatedAtDesc(String requester);

    List<ApprovalRequest> findByApproverAndStatusOrderByCreatedAtDesc(
            String approver, 
            RequestStatus status
    );

    List<ApprovalRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    Long countByApproverAndStatus(String approver, RequestStatus status);

    @Query("SELECT ar FROM ApprovalRequest ar WHERE ar.requestType.id = :typeId ORDER BY ar.createdAt DESC")
    List<ApprovalRequest> findByRequestTypeId(@Param("typeId") Long typeId);
}