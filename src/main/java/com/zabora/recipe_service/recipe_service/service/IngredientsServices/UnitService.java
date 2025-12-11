package com.zabora.recipe_service.recipe_service.service.IngredientsServices;

import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.UnitRepository;
import org.springframework.stereotype.Service;

@Service
public class UnitService {

    private final UnitRepository unitRepository; // El repositorio de tu entidad Unit

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    /**
     * Recupera todas las unidades de medida y las mapea al DTO de respuesta.
     * @return Lista de UnitResponse.
     */
    public List<UnitResponse> getAllUnits() {
        return unitRepository.findAll() // 👈 Llama a tu repositorio
                .stream()
                .map(UnitMapper::toResponse) // 👈 Usa el Mapper para convertir
                .collect(Collectors.toList());
    }
}