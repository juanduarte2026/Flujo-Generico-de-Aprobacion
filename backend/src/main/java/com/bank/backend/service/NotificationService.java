package com.bank.backend.service;

import com.bank.backend.entity.ApprovalRequest;

public interface NotificationService {

    void notifyNewRequest(ApprovalRequest request);

    void notifyApproval(ApprovalRequest request, String comment);

    void notifyRejection(ApprovalRequest request, String comment);
}