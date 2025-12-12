package com.zabora.recipe_service.recipe_service.service.IngredientsServices;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Measurement;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Unit;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnitService {

    private final UnitRepository unitRepository; // El repositorio de tu entidad Unit

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<UnitResponse> findAll() {
        List <Unit> units = unitRepository.findAll();
        return units.stream()
                .map(u -> new UnitResponse(
                        u.getId(),
                        u.getName(),
                        // Tienes que mapear el objeto Measurement a MeasurementResponse
                        new MeasurementResponse(
                                u.getMeasurement().getId(),
                                u.getMeasurement().getName() // Asumiendo estos campos
                        )
                )).toList();
    }

}