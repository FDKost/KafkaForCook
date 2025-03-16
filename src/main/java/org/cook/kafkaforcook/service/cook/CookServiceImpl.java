package org.cook.kafkaforcook.service.cook;

import lombok.RequiredArgsConstructor;
import org.cook.kafkaforcook.entity.CookEntity;
import org.cook.kafkaforcook.repository.CookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CookServiceImpl implements CookService {
    private final CookRepository cookRepository;

    @Override
    public CookEntity create(CookEntity cook) {
        return cookRepository.save(cook);
    }

    @Override
    public Optional<CookEntity> read(UUID id) {
        return cookRepository.findById(id);
    }

    @Override
    public List<CookEntity> readAll() {
        return cookRepository.findAll();
    }

    @Override
    public Optional<CookEntity> update(UUID id, CookEntity cook) {
        return cookRepository.findById(id)
                .map(cookRepository::saveAndFlush);
    }

    @Override
    public void delete(UUID id) {
        cookRepository.findById(id)
                .ifPresent(cookRepository::delete);
    }
}
