package com.bank.backend.dto.response;

import com.bank.backend.enums.RequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestResponseDTO {

    private UUID id;
    private String title;
    private String description;
    private String requester;
    private String approver;
    private RequestTypeDTO requestType;
    private RequestStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    private List<ApprovalHistoryResponseDTO> history;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestTypeDTO {
        private Long id;
        private String code;
        private String name;
        private String description;
    }
}