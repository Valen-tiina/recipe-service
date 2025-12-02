package com.zabora.recipe_service.recipe_service.repository.IngredientsRepository;

import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
}
