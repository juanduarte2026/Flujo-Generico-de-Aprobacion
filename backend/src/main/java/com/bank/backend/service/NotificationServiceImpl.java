package com.bank.backend.service;

import com.bank.backend.entity.ApprovalRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void notifyNewRequest(ApprovalRequest request) {
        log.info(" [NOTIFICACIÓN] Nueva solicitud para aprobar");
        log.info("   → Destinatario: {}", request.getApprover());
        log.info("   → Solicitud ID: {}", request.getId());
        log.info("   → Título: {}", request.getTitle());
        log.info("   → Solicitante: {}", request.getRequester());
        
    }

    @Override
    public void notifyApproval(ApprovalRequest request, String comment) {
        log.info(" [NOTIFICACIÓN] Solicitud aprobada");
        log.info("   → Destinatario: {}", request.getRequester());
        log.info("   → Solicitud ID: {}", request.getId());
        log.info("   → Título: {}", request.getTitle());
        log.info("   → Comentario: {}", comment);
        
    }

    @Override
    public void notifyRejection(ApprovalRequest request, String comment) {
        log.info(" [NOTIFICACIÓN] Solicitud rechazada");
        log.info("   → Destinatario: {}", request.getRequester());
        log.info("   → Solicitud ID: {}", request.getId());
        log.info("   → Título: {}", request.getTitle());
        log.info("   → Comentario: {}", comment);
        
    }
}