package com.zabora.recipe_service.recipe_service.service.RecipesServices;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeServiceDelete {
    private final RecipeRepository recipeRepository;
    public RecipeServiceDelete(RecipeRepository recipeRepository){
        this.recipeRepository=recipeRepository;
    }

    @Transactional
    public void deleteRecipe(Integer id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));

        recipeRepository.delete(recipe);
    }
}
