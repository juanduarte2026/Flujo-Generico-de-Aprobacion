package com.bank.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApprovalRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 5000, message = "La descripción no puede exceder 5000 caracteres")
    private String description;

    @NotBlank(message = "El solicitante es obligatorio")
    @Size(max = 100, message = "El solicitante no puede exceder 100 caracteres")
    private String requester;

    @NotBlank(message = "El aprobador es obligatorio")
    @Size(max = 100, message = "El aprobador no puede exceder 100 caracteres")
    private String approver;

    @NotNull(message = "El ID del tipo de solicitud es obligatorio")
    private Long requestTypeId;
}
