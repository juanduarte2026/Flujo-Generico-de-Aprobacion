package com.bank.backend.controller;

import com.bank.backend.dto.request.ApprovalActionDTO;
import com.bank.backend.dto.request.CreateApprovalRequestDTO;
import com.bank.backend.dto.response.ApprovalRequestResponseDTO;
import com.bank.backend.entity.RequestType;
import com.bank.backend.enums.RequestStatus;
import com.bank.backend.service.ApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Tag(name = "Solicitudes de Aprobación", description = "API para gestionar solicitudes de aprobación")
@CrossOrigin(origins = "*") // Para desarrollo. En producción, configurar en CorsConfig
public class ApprovalRequestController {

    private final ApprovalService approvalService;

    @Operation(summary = "Crear nueva solicitud de aprobación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de solicitud no encontrado")
    })
    @PostMapping
    public ResponseEntity<ApprovalRequestResponseDTO> createRequest(
            @Valid @RequestBody CreateApprovalRequestDTO dto) {
        
        log.info("POST /requests - Crear solicitud: {}", dto.getTitle());
        ApprovalRequestResponseDTO response = approvalService.createRequest(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Obtener todas las solicitudes")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida")
    @GetMapping
    public ResponseEntity<List<ApprovalRequestResponseDTO>> getAllRequests() {
        log.info("GET /requests - Obtener todas las solicitudes");
        List<ApprovalRequestResponseDTO> requests = approvalService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Obtener solicitud por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApprovalRequestResponseDTO> getRequestById(
            @Parameter(description = "ID único de la solicitud")
            @PathVariable UUID id) {
        
        log.info("GET /requests/{} - Obtener solicitud", id);
        ApprovalRequestResponseDTO request = approvalService.getRequestById(id);
        return ResponseEntity.ok(request);
    }

    @Operation(summary = "Obtener solicitudes de un solicitante")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes del solicitante")
    @GetMapping("/requester/{username}")
    public ResponseEntity<List<ApprovalRequestResponseDTO>> getRequestsByRequester(
            @Parameter(description = "Usuario de red del solicitante")
            @PathVariable String username) {
        
        log.info("GET /requests/requester/{} - Obtener solicitudes del solicitante", username);
        List<ApprovalRequestResponseDTO> requests = approvalService.getRequestsByRequester(username);
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Obtener solicitudes pendientes de un aprobador")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes pendientes")
    @GetMapping("/approver/{username}/pending")
    public ResponseEntity<List<ApprovalRequestResponseDTO>> getPendingRequestsByApprover(
            @Parameter(description = "Usuario de red del aprobador")
            @PathVariable String username) {
        
        log.info("GET /requests/approver/{}/pending - Obtener solicitudes pendientes", username);
        List<ApprovalRequestResponseDTO> requests = 
                approvalService.getPendingRequestsByApprover(username);
        return ResponseEntity.ok(requests);
    }

    @Operation(summary = "Obtener solicitudes por estado")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes con el estado especificado")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApprovalRequestResponseDTO>> getRequestsByStatus(
            @Parameter(description = "Estado de la solicitud (PENDING, APPROVED, REJECTED)")
            @PathVariable RequestStatus status) {
        
        log.info("GET /requests/status/{} - Obtener solicitudes por estado", status);
        List<ApprovalRequestResponseDTO> requests = approvalService.getRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }


    @Operation(summary = "Aprobar una solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud aprobada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Operación inválida"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApprovalRequestResponseDTO> approveRequest(
            @Parameter(description = "ID único de la solicitud")
            @PathVariable UUID id,
            @Valid @RequestBody ApprovalActionDTO actionDTO) {
        
        log.info("PUT /requests/{}/approve - Aprobar solicitud", id);
        ApprovalRequestResponseDTO response = approvalService.approveRequest(id, actionDTO);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Rechazar una solicitud")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud rechazada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Operación inválida"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApprovalRequestResponseDTO> rejectRequest(
            @Parameter(description = "ID único de la solicitud")
            @PathVariable UUID id,
            @Valid @RequestBody ApprovalActionDTO actionDTO) {
        
        log.info("PUT /requests/{}/reject - Rechazar solicitud", id);
        ApprovalRequestResponseDTO response = approvalService.rejectRequest(id, actionDTO);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Obtener catálogo de tipos de solicitud")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de solicitud activos")
    @GetMapping("/types")
    public ResponseEntity<List<RequestType>> getAllRequestTypes() {
        log.info("GET /requests/types - Obtener tipos de solicitud");
        List<RequestType> types = approvalService.getAllRequestTypes();
        return ResponseEntity.ok(types);
    }


    @Operation(summary = "Contar solicitudes pendientes de un aprobador")
    @ApiResponse(responseCode = "200", description = "Cantidad de solicitudes pendientes")
    @GetMapping("/approver/{username}/pending/count")
    public ResponseEntity<Map<String, Long>> countPendingRequests(
            @Parameter(description = "Usuario de red del aprobador")
            @PathVariable String username) {
        
        log.info("GET /requests/approver/{}/pending/count - Contar pendientes", username);
        Long count = approvalService.countPendingRequestsByApprover(username);
        
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        response.put("approver", username.hashCode() & 0xFFFFFFFFL); // Solo para ejemplo
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Health check")
    @ApiResponse(responseCode = "200", description = "Servicio operativo")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "Approval System API");
        return ResponseEntity.ok(health);
    }
}