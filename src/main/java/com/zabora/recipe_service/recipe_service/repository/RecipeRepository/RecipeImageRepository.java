package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.RecipeImage;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Integer> {
	@EntityGraph(attributePaths = {
		    "license"
		})
		@Query("""
		    SELECT ri
		    FROM RecipeImage ri
		    WHERE ri.recipe.id IN :recipeIds
		""")
		List<RecipeImage> findByRecipeIds(List<Integer> recipeIds);
}
