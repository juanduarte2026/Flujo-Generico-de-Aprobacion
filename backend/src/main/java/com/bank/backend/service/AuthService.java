package com.bank.backend.service;

import com.bank.backend.dto.request.LoginRequestDTO;
import com.bank.backend.dto.response.UserResponseDTO;
import com.bank.backend.entity.User;
import com.bank.backend.exception.InvalidRequestException;
import com.bank.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDTO login(LoginRequestDTO loginDTO) {
        log.info("Intento de login para usuario: {}", loginDTO.getUsername());

        User user = userRepository.findByUsernameAndActiveTrue(loginDTO.getUsername())
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado o inactivo: {}", loginDTO.getUsername());
                    return new InvalidRequestException("Credenciales inválidas");
                });

        if (!user.getPassword().equals(loginDTO.getPassword())) {
            log.warn("Contraseña incorrecta para usuario: {}", loginDTO.getUsername());
            throw new InvalidRequestException("Credenciales inválidas");
        }

        log.info("Login exitoso para usuario: {} ({})", user.getUsername(), user.getFullName());

        return toUserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllActiveUsers() {
        log.debug("Obteniendo todos los usuarios activos");
        
        return userRepository.findAll().stream()
                .filter(User::getActive)
                .map(this::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByUsername(String username) {
        log.debug("Obteniendo información del usuario: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidRequestException(
                        "Usuario no encontrado: " + username));

        return toUserResponseDTO(user);
    }


    private UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}