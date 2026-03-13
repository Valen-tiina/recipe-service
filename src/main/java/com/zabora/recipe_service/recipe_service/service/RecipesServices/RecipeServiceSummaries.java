package com.zabora.recipe_service.recipe_service.service.RecipesServices;

import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.AlergiaDTO;
import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.CondicionMedicaDTO;
import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.MedicalInfoResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.PreferenciaDTO;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.RecipeIngredientDTO.ResponseRecipeIngredient;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.UnitResponse;
import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.RecipeImage;
import com.zabora.recipe_service.recipe_service.repository.AuthClient;
import com.zabora.recipe_service.recipe_service.repository.RecipeRepository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceSummaries {

    private final RecipeRepository recipeRepository;
    private final AuthClient authClient;

    public RecipeServiceSummaries(RecipeRepository recipeRepository, AuthClient authClient) {
        this.recipeRepository = recipeRepository;
        this.authClient = authClient;
    }


    public List<RecipeResponseSummary> getRecipeSummary() {
        return mapToSummary(recipeRepository.findAllSummaries());
    }

    public List<RecipeResponseSummary> getRecipeSummaryByUser(Long userId) {
        MedicalInfoResponse medicalInfo = authClient.getUserMedicalInfo(userId);
        List<String> forbidden = buildForbiddenIngredients(medicalInfo);

        if (forbidden.isEmpty()) {
            return mapToSummary(recipeRepository.findAllSummaries());
        }

        return mapToSummary(recipeRepository.findRecipesWithoutIngredients(forbidden));
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


    public List<RecipeResponseSummary> mapToSummary(List<Recipe> recipes) {
        return recipes.stream()
                .map(r -> new RecipeResponseSummary(
                        r.getId(),
                        r.getTitle(),
                        r.getShortDescription(),
                        r.getTotalTimeMin(),
                        r.getImages().stream()
                                .findFirst()
                                .map(RecipeImage::getImageUrl)
                                .orElse(null),
                        r.getIngredients().stream()
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
                                .toList()
                ))
                .toList();
    }
}