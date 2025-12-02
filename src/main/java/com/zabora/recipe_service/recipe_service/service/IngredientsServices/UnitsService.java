package com.zabora.recipe_service.recipe_service.service.IngredientsServices;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Measurement;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Unit;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.MeasurementRepository;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitsService {
    private final UnitRepository  unitRepo;
    private final MeasurementRepository measureRepo;
    public UnitsService(UnitRepository unitRepo, MeasurementRepository measureRepo) {
        this.unitRepo = unitRepo;
        this.measureRepo = measureRepo;
    }

    public List<UnitResponse> findAll() {
        return unitRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UnitResponse findById(Integer id) {
        Unit entity = unitRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("La unidad con ID " + id + " no existe"));
        return toResponse(entity);
    }

    // ------------------------ PRIVATE MAPPERS ------------------------

    private UnitResponse toResponse(Unit entity) {
        return new UnitResponse(
                entity.getId(),
                entity.getName(),
                entity.getMeasurement().getName()
        );
    }
;

    private MeasurementResponse toMeasurementResponse(Measurement m) {
        if (m == null) return null;

        return new MeasurementResponse(
                m.getId(),
                m.getName()
        );
    }
}
