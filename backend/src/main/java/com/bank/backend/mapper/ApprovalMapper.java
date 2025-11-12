package com.bank.backend.mapper;

import com.bank.backend.dto.request.CreateApprovalRequestDTO;
import com.bank.backend.dto.response.ApprovalHistoryResponseDTO;
import com.bank.backend.dto.response.ApprovalRequestResponseDTO;
import com.bank.backend.entity.ApprovalHistory;
import com.bank.backend.entity.ApprovalRequest;
import com.bank.backend.entity.RequestType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ApprovalMapper {


    public ApprovalRequest toEntity(CreateApprovalRequestDTO dto, RequestType requestType) {
        return ApprovalRequest.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .requester(dto.getRequester())
                .approver(dto.getApprover())
                .requestType(requestType)
                .build();
    }


    public ApprovalRequestResponseDTO toResponseDTO(ApprovalRequest entity) {
        return ApprovalRequestResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .requester(entity.getRequester())
                .approver(entity.getApprover())
                .requestType(toRequestTypeDTO(entity.getRequestType()))
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .history(toHistoryDTOList(entity.getHistory()))
                .build();
    }


    public List<ApprovalRequestResponseDTO> toResponseDTOList(List<ApprovalRequest> entities) {
        return entities.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private ApprovalRequestResponseDTO.RequestTypeDTO toRequestTypeDTO(RequestType requestType) {
        return ApprovalRequestResponseDTO.RequestTypeDTO.builder()
                .id(requestType.getId())
                .code(requestType.getCode())
                .name(requestType.getName())
                .description(requestType.getDescription())
                .build();
    }


    public ApprovalHistoryResponseDTO toHistoryDTO(ApprovalHistory history) {
        return ApprovalHistoryResponseDTO.builder()
                .id(history.getId())
                .action(history.getAction())
                .comment(history.getComment())
                .performedBy(history.getPerformedBy())
                .performedAt(history.getPerformedAt())
                .build();
    }

    private List<ApprovalHistoryResponseDTO> toHistoryDTOList(List<ApprovalHistory> historyList) {
        return historyList.stream()
                .map(this::toHistoryDTO)
                .collect(Collectors.toList());
    }
}