package com.bank.backend.service;

import com.bank.backend.dto.request.ApprovalActionDTO;
import com.bank.backend.dto.request.CreateApprovalRequestDTO;
import com.bank.backend.dto.response.ApprovalRequestResponseDTO;
import com.bank.backend.entity.RequestType;
import com.bank.backend.enums.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface ApprovalService {

     // Crea una nueva solicitud de aprobación
    ApprovalRequestResponseDTO createRequest(CreateApprovalRequestDTO dto);

     // Obtiene una solicitud por su ID
    ApprovalRequestResponseDTO getRequestById(UUID id);

     // Obtiene todas las solicitudes
    List<ApprovalRequestResponseDTO> getAllRequests();

     // Obtiene solicitudes por solicitante
    List<ApprovalRequestResponseDTO> getRequestsByRequester(String requester);

     // Obtiene solicitudes pendientes de un aprobador
    List<ApprovalRequestResponseDTO> getPendingRequestsByApprover(String approver);

     // Obtiene solicitudes por estado
    List<ApprovalRequestResponseDTO> getRequestsByStatus(RequestStatus status);

     // Aprueba una solicitud
    ApprovalRequestResponseDTO approveRequest(UUID id, ApprovalActionDTO actionDTO);

     // Rechaza una solicitud
    ApprovalRequestResponseDTO rejectRequest(UUID id, ApprovalActionDTO actionDTO);

     // Obtiene todos los tipos de solicitud disponibles
    List<RequestType> getAllRequestTypes();

     // Cuenta solicitudes pendientes de un aprobador
    Long countPendingRequestsByApprover(String approver);
}
