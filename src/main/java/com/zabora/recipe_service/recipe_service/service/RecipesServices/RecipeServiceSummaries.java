package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.AlergiaDTO;
import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.CondicionMedicaDTO;
import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.MedicalInfoResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.PreferenciaDTO;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.ResponseRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary;
import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.RecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.RecipeImage;
import com.zabora.recipe_service.recipe_service.repository.AuthClient;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.RecipeIngredientRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeImageRepository;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;

@Service
public class RecipeServiceSummaries {

	private final RecipeRepository recipeRepository;
	private final RecipeImageRepository recipeImageRepository;
	  private final RecipeIngredientRepository recipeIngredientRepository;
	private final AuthClient authClient;


    public RecipeServiceSummaries(
            RecipeRepository recipeRepository,
            RecipeImageRepository recipeImageRepository,
            RecipeIngredientRepository recipeIngredientRepository,
            AuthClient authClient) {

        this.recipeRepository = recipeRepository;
        this.recipeImageRepository = recipeImageRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.authClient = authClient;
    }

	public Page<RecipeResponseSummary> getRecipeSummary(int page, int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<Recipe> recipePage = recipeRepository.findAllSummaries(pageable);

		List<Integer> recipeIds = recipePage.getContent().stream().map(Recipe::getId).toList();

		List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeIds(recipeIds);
		Map<Integer, List<ResponseRecipeIngredient>> groupedIngredients = mapIngredients(ingredients);

		List<RecipeImage> images = recipeImageRepository.findByRecipeIds(recipeIds);
		Map<Integer, String> imageMap = mapImages(images);

		return recipePage.map(
				recipe -> mapToSummary(recipe, groupedIngredients.get(recipe.getId()), imageMap.get(recipe.getId())));
	}

	public Page<RecipeResponseSummary> getRecipeSummaryByUser(Long userId, int page, int size) {

		MedicalInfoResponse medicalInfo = authClient.getUserMedicalInfo(userId);
		List<String> forbidden = buildForbiddenIngredients(medicalInfo);

		Pageable pageable = PageRequest.of(page, size);

		Page<Recipe> recipePage;

		if (forbidden.isEmpty()) {
			recipePage = recipeRepository.findAllSummaries(pageable);
		} else {
			recipePage = recipeRepository.findRecipesWithoutIngredients(forbidden, pageable);
		}

		List<Integer> recipeIds = recipePage.getContent().stream().map(Recipe::getId).toList();

		List<RecipeIngredient> ingredients = recipeIngredientRepository.findByRecipeIds(recipeIds);
		Map<Integer, List<ResponseRecipeIngredient>> groupedIngredients = mapIngredients(ingredients);

		List<RecipeImage> images = recipeImageRepository.findByRecipeIds(recipeIds);
		Map<Integer, String> imageMap = mapImages(images);

		return recipePage.map(
				recipe -> mapToSummary(recipe, groupedIngredients.get(recipe.getId()), imageMap.get(recipe.getId())));
	}

	private Map<Integer, List<ResponseRecipeIngredient>> mapIngredients(List<RecipeIngredient> ingredients) {
		return ingredients.stream().collect(Collectors.groupingBy(ri -> ri.getRecipe().getId(),
				Collectors.mapping(this::toIngredientDto, Collectors.toList())));
	}

	private Map<Integer, String> mapImages(List<RecipeImage> images) {
		return images.stream().collect(Collectors.toMap(img -> img.getRecipe().getId(), RecipeImage::getImageUrl,
				(existing, replacement) -> existing));
	}

	private ResponseRecipeIngredient toIngredientDto(RecipeIngredient ri) {

		MeasurementResponse measurement = null;

		if (ri.getUnit() != null && ri.getUnit().getMeasurement() != null) {
			measurement = new MeasurementResponse(ri.getUnit().getMeasurement().getId(),
					ri.getUnit().getMeasurement().getName());
		}

		return new ResponseRecipeIngredient(ri.getId(), ri.getIngredient().getName(), ri.getIngredient().getImageUrl(),
				ri.getQuantity(), new UnitResponse(ri.getUnit().getId(), ri.getUnit().getName(), measurement));
	}

	private List<String> buildForbiddenIngredients(MedicalInfoResponse medicalInfo) {
		List<String> forbidden = new ArrayList<>();

		for (CondicionMedicaDTO condicion : medicalInfo.getCondicionesMedicas()) {
			if (condicion.getId() == 2) {
				forbidden.addAll(List.of("azucar", "miel", "jarabe"));
			}
			if (condicion.getId() == 3) {
				forbidden.add("sal");
			}
		}

		for (AlergiaDTO alergia : medicalInfo.getAlergias()) {
			if (alergia.getId() == 4) {
				forbidden.addAll(List.of("leche", "queso", "mantequilla", "yogurt"));
			}
			if (alergia.getId() == 2) {
				forbidden.add("mani");
			}
		}

		PreferenciaDTO pref = medicalInfo.getPreferenciaAlimenticia();
		if (pref != null) {
			if (pref.getId() == 2) {
				forbidden.addAll(List.of("carne", "pollo", "huevo", "leche", "queso"));
			}
			if (pref.getId() == 4) {
				forbidden.addAll(List.of("trigo", "cebada", "centeno"));
			}
		}

		return forbidden;
	}

	private RecipeResponseSummary mapToSummary(Recipe recipe, List<ResponseRecipeIngredient> ingredients,
			String imageUrl) {

		return new RecipeResponseSummary(recipe.getId(), recipe.getTitle(), recipe.getShortDescription(),
				recipe.getTotalTimeMin(), imageUrl, ingredients != null ? ingredients : List.of());
	}

	public List<RecipeResponseSummary> mapToSummary(List<Recipe> recipes) {
		return recipes
				.stream().map(
						r -> new RecipeResponseSummary(r.getId(), r.getTitle(), r.getShortDescription(),
								r.getTotalTimeMin(),
								r.getImages().stream().findFirst().map(RecipeImage::getImageUrl).orElse(null),
								r.getIngredients().stream()
										.map(ri -> new ResponseRecipeIngredient(ri.getId(),
												ri.getIngredient().getName(), ri.getIngredient().getImageUrl(),
												ri.getQuantity(),
												new UnitResponse(ri.getUnit().getId(), ri.getUnit().getName(),
														new MeasurementResponse(ri.getUnit().getMeasurement().getId(),
																ri.getUnit().getMeasurement().getName()))))
										.toList()))
				.toList();
	}

}