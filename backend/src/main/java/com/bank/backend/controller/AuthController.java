package com.bank.backend.controller;

import com.bank.backend.dto.request.LoginRequestDTO;
import com.bank.backend.dto.response.UserResponseDTO;
import com.bank.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para login y gestión de usuarios")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión", 
               description = "Autentica un usuario con sus credenciales")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginDTO) {
        
        log.info("POST /auth/login - Intento de login: {}", loginDTO.getUsername());
        UserResponseDTO user = authService.login(loginDTO);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Obtener lista de usuarios activos",
               description = "Retorna todos los usuarios activos del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("GET /auth/users - Obtener lista de usuarios activos");
        List<UserResponseDTO> users = authService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtener información de un usuario",
               description = "Retorna la información completa de un usuario por su username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "400", description = "Usuario no encontrado")
    })
    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(
            @Parameter(description = "Nombre de usuario (username)")
            @PathVariable String username) {
        
        log.info("GET /auth/users/{} - Obtener información del usuario", username);
        UserResponseDTO user = authService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}