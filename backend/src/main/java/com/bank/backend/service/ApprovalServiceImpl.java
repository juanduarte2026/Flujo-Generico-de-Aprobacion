package com.bank.backend.service;

import com.bank.backend.dto.request.ApprovalActionDTO;
import com.bank.backend.dto.request.CreateApprovalRequestDTO;
import com.bank.backend.dto.response.ApprovalRequestResponseDTO;
import com.bank.backend.entity.ApprovalHistory;
import com.bank.backend.entity.ApprovalRequest;
import com.bank.backend.entity.RequestType;
import com.bank.backend.enums.RequestStatus;
import com.bank.backend.exception.InvalidRequestException;
import com.bank.backend.exception.ResourceNotFoundException;
import com.bank.backend.mapper.ApprovalMapper;
import com.bank.backend.repository.ApprovalRequestRepository;
import com.bank.backend.repository.RequestTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


 @Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRequestRepository requestRepository;
    private final RequestTypeRepository requestTypeRepository;
    private final ApprovalMapper mapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ApprovalRequestResponseDTO createRequest(CreateApprovalRequestDTO dto) {
        log.info("Creando nueva solicitud de aprobación: {}", dto.getTitle());

        RequestType requestType = requestTypeRepository.findById(dto.getRequestTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de solicitud", "id", dto.getRequestTypeId()));

        if (!requestType.getIsActive()) {
            throw new InvalidRequestException(
                    "El tipo de solicitud '" + requestType.getName() + "' no está activo");
        }

        ApprovalRequest request = mapper.toEntity(dto, requestType);

        ApprovalHistory creationHistory = ApprovalHistory.builder()
                .action("CREATED")
                .comment("Solicitud creada")
                .performedBy(dto.getRequester())
                .build();

        request.addHistory(creationHistory);

        ApprovalRequest savedRequest = requestRepository.save(request);
        log.info("Solicitud creada exitosamente con ID: {}", savedRequest.getId());

        notificationService.notifyNewRequest(savedRequest);

        return mapper.toResponseDTO(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ApprovalRequestResponseDTO getRequestById(UUID id) {
        log.debug("Buscando solicitud con ID: {}", id);

        ApprovalRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud de aprobación", "id", id));

        return mapper.toResponseDTO(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalRequestResponseDTO> getAllRequests() {
        log.debug("Obteniendo todas las solicitudes");

        List<ApprovalRequest> requests = requestRepository.findAll();
        return mapper.toResponseDTOList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalRequestResponseDTO> getRequestsByRequester(String requester) {
        log.debug("Obteniendo solicitudes del solicitante: {}", requester);

        List<ApprovalRequest> requests = requestRepository
                .findByRequesterOrderByCreatedAtDesc(requester);

        return mapper.toResponseDTOList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalRequestResponseDTO> getPendingRequestsByApprover(String approver) {
        log.debug("Obteniendo solicitudes pendientes del aprobador: {}", approver);

        List<ApprovalRequest> requests = requestRepository
                .findByApproverAndStatusOrderByCreatedAtDesc(approver, RequestStatus.PENDING);

        return mapper.toResponseDTOList(requests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalRequestResponseDTO> getRequestsByStatus(RequestStatus status) {
        log.debug("Obteniendo solicitudes con estado: {}", status);

        List<ApprovalRequest> requests = requestRepository
                .findByStatusOrderByCreatedAtDesc(status);

        return mapper.toResponseDTOList(requests);
    }

    @Override
    @Transactional
    public ApprovalRequestResponseDTO approveRequest(UUID id, ApprovalActionDTO actionDTO) {
        log.info("Aprobando solicitud ID: {}", id);

        ApprovalRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud de aprobación", "id", id));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestException(
                    "Solo se pueden aprobar solicitudes en estado PENDIENTE. Estado actual: " 
                    + request.getStatus());
        }

        if (!request.getApprover().equals(actionDTO.getPerformedBy())) {
            throw new InvalidRequestException(
                    "Solo el aprobador asignado (" + request.getApprover() 
                    + ") puede aprobar esta solicitud");
        }

        request.setStatus(RequestStatus.APPROVED);

        ApprovalHistory approvalHistory = ApprovalHistory.builder()
                .action("APPROVED")
                .comment(actionDTO.getComment() != null ? actionDTO.getComment() : "Aprobado")
                .performedBy(actionDTO.getPerformedBy())
                .build();

        request.addHistory(approvalHistory);

        ApprovalRequest updatedRequest = requestRepository.save(request);
        log.info("Solicitud {} aprobada exitosamente por {}", id, actionDTO.getPerformedBy());

        notificationService.notifyApproval(updatedRequest, actionDTO.getComment());

        return mapper.toResponseDTO(updatedRequest);
    }

    @Override
    @Transactional
    public ApprovalRequestResponseDTO rejectRequest(UUID id, ApprovalActionDTO actionDTO) {
        log.info("Rechazando solicitud ID: {}", id);

        ApprovalRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud de aprobación", "id", id));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestException(
                    "Solo se pueden rechazar solicitudes en estado PENDIENTE. Estado actual: " 
                    + request.getStatus());
        }

        if (!request.getApprover().equals(actionDTO.getPerformedBy())) {
            throw new InvalidRequestException(
                    "Solo el aprobador asignado (" + request.getApprover() 
                    + ") puede rechazar esta solicitud");
        }

        request.setStatus(RequestStatus.REJECTED);

        ApprovalHistory rejectionHistory = ApprovalHistory.builder()
                .action("REJECTED")
                .comment(actionDTO.getComment() != null ? actionDTO.getComment() 
                        : "Rechazado sin comentarios")
                .performedBy(actionDTO.getPerformedBy())
                .build();

        request.addHistory(rejectionHistory);
        ApprovalRequest updatedRequest = requestRepository.save(request);
        log.info("Solicitud {} rechazada por {}", id, actionDTO.getPerformedBy());

        notificationService.notifyRejection(updatedRequest, actionDTO.getComment());

        return mapper.toResponseDTO(updatedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestType> getAllRequestTypes() {
        log.debug("Obteniendo todos los tipos de solicitud activos");
        return requestTypeRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPendingRequestsByApprover(String approver) {
        log.debug("Contando solicitudes pendientes del aprobador: {}", approver);
        return requestRepository.countByApproverAndStatus(approver, RequestStatus.PENDING);
    }
}