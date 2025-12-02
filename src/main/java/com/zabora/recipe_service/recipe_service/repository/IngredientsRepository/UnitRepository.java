package com.zabora.recipe_service.recipe_service.repository.IngredientsRepository;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
}
