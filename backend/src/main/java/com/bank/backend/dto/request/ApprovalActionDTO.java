package com.bank.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalActionDTO {

    @Size(max = 2000, message = "El comentario no puede exceder 2000 caracteres")
    private String comment;

    @NotBlank(message = "El usuario que realiza la acción es obligatorio")
    @Size(max = 100, message = "El usuario no puede exceder 100 caracteres")
    private String performedBy;
}