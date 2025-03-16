package org.cook.kafkaforcook.service.cook;

import org.cook.kafkaforcook.entity.CookEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CookService {
    CookEntity create(CookEntity cook);
    Optional<CookEntity> read(UUID id);
    List<CookEntity> readAll();
    Optional<CookEntity> update(UUID id, CookEntity cook);
    void delete(UUID id);

}
