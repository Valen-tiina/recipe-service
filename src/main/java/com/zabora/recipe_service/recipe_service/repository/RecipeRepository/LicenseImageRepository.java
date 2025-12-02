package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.LicenseImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseImageRepository extends JpaRepository<LicenseImage, Integer> {
}
