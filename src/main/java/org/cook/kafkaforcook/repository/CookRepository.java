package org.cook.kafkaforcook.repository;

import org.cook.kafkaforcook.entity.CookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CookRepository extends JpaRepository<CookEntity, UUID> {
    Optional<CookEntity> findById(UUID id);
}
