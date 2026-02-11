package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.CategoryResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.categoriesdtos.FlavorResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.ResponseRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipeImagesDTO.ResponseRecipeImages;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.LicensesRecipeDTO.ResponseLicenseRecipe;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.ResponseRecipes;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.StepDTO.ResponseSteps;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Category;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Difficulty;
import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.*;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.CategoryRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.DifficultyRepository;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.FlavorRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final CategoryRepository categoryRepository;
    private final FlavorRepository flavorRepository;
    private final DifficultyRepository difficultyRepository;

    public RecipeService(
            CategoryRepository categoryRepository,
            FlavorRepository flavorRepository,
            DifficultyRepository difficultyRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.flavorRepository = flavorRepository;
        this.difficultyRepository = difficultyRepository;
    }

    // validación separada por metodos
    public Difficulty validateDifficulty(Integer difficultyId) {
        return difficultyRepository.findById(difficultyId)
                .orElseThrow(() -> new RuntimeException("Dificultad no encontrada"));
    }

    public Set<Category> validateCategories(Set<Integer> categoryIds) {
        var categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        if (categories.size() != categoryIds.size()) {
            throw new RuntimeException("Una o más categorías no existen");
        }
        return categories;
    }
    public Set<Flavor> validateFlavors(Set<Integer> flavorIds) {
        var flavors = new HashSet<>(flavorRepository.findAllById(flavorIds));
        if (flavors.size() != flavorIds.size()) {
            throw new RuntimeException("Uno o más sabores no existen");
        }
        return flavors;
    }


// mapea rta que se mostrara en front
    public ResponseRecipes mapToResponse(Recipe recipe) {
        return new ResponseRecipes(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getShortDescription(),
                recipe.getServings(),
                recipe.getTotalTimeMin(),
                recipe.getDifficulty().getName(),

                new ResponseLicenseRecipe(
                        recipe.getLicense().getId(),
                        recipe.getLicense().getName(),
                        recipe.getLicense().getUrlImage()
                ),

                recipe.getCategories()
                        .stream()
                        .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                        .collect(Collectors.toSet()),

                recipe.getFlavors()
                        .stream()
                        .map(f -> new FlavorResponse(f.getId(), f.getName()))
                        .collect(Collectors.toSet()),

                recipe.getImages()
                        .stream()
                        .map(img -> new ResponseRecipeImages(
                                img.getId(),
                                img.getImageUrl(),
                                img.getAltText(),
                                img.getPosition(),
                                img.getLicense().getId(),
                                img.getLicense().getName(),
                                img.getLicense().getUrlRecipe()
                        ))
                        .toList(),

                recipe.getIngredients()
                        .stream()
                        .map(ri -> new ResponseRecipeIngredient(
                                ri.getId(),
                                ri.getIngredient().getName(),
                                ri.getIngredient().getImageUrl(),
                                ri.getQuantity(),
                                new UnitResponse(
                                        ri.getUnit().getId(),
                                        ri.getUnit().getName(),
                                        new MeasurementResponse(
                                                ri.getUnit().getMeasurement().getId(),
                                                ri.getUnit().getMeasurement().getName()
                                        )
                                )
                        ))
                        .toList(),

                recipe.getSteps()
                        .stream()
                        .map(s -> new ResponseSteps(
                                s.getId(),
                                s.getStepOrder(),
                                s.getDescription(),
                                s.getTimeSeconds(),
                                s.getImageUrl()
                        ))
                        .toList()
        );
    }
}