package org.cook.kafkaforcook.repository;

import org.cook.kafkaforcook.entity.PizzaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<PizzaEntity, String> {
    Optional<PizzaEntity> findByName(String name);
}
