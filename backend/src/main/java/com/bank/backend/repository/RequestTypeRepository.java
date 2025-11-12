package com.bank.backend.repository;

import com.bank.backend.entity.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestTypeRepository extends JpaRepository<RequestType, Long> {

    Optional<RequestType> findByCode(String code);

    List<RequestType> findByIsActiveTrue();
}
