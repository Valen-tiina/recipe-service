package com.zabora.recipe_service.recipe_service.repository.CategoriesRepository;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlavorRepository extends JpaRepository<Flavor, Integer> {
}
