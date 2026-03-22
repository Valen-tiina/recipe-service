package com.zabora.recipe_service.recipe_service.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeName;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    List<Recipe> findByTitleContainingIgnoreCase(String title);

    @Query("""
        SELECT DISTINCT r FROM Recipe r
        LEFT JOIN FETCH r.ingredients ri
        LEFT JOIN FETCH ri.ingredient i
        WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredientName, '%'))
    """)
    List<Recipe> findByIngredientNameContaining(@Param("ingredientName") String ingredientName);

    @Query("""
        SELECT DISTINCT r FROM Recipe r
        LEFT JOIN FETCH r.ingredients ri
        LEFT JOIN FETCH ri.ingredient
        LEFT JOIN FETCH ri.unit u
        LEFT JOIN FETCH u.measurement
        LEFT JOIN FETCH r.steps
        LEFT JOIN FETCH r.images img
        LEFT JOIN FETCH img.license
        LEFT JOIN FETCH r.categories
        LEFT JOIN FETCH r.flavors
        LEFT JOIN FETCH r.difficulty
        LEFT JOIN FETCH r.license
        WHERE r.id = :id
    """)
    Optional<Recipe> findByIdWithAllRelations(@Param("id") Integer id);

    List<Recipe> findByTotalTimeMinLessThanEqual(Integer maxTime);

    Page<Recipe> findByCategoriesIdIn(Set<Integer> categoryIds, Pageable pageable);

    Page<Recipe> findByDifficultyId(Integer difficultyId, Pageable pageable);


    @Query("SELECT r FROM Recipe r JOIN r.categories c WHERE c.id = :categoryId")
    List<Recipe> findByCategoryId(@Param("categoryId") Integer categoryId);

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri JOIN ri.ingredient i WHERE LOWER(i.name) IN :ingredientNames")
    List<Recipe> findByIngredientsNameIn(@Param("ingredientNames") List<String> ingredientNames);

    @Query("""
    SELECT DISTINCT r FROM Recipe r
    LEFT JOIN FETCH r.ingredients ri
    LEFT JOIN FETCH ri.ingredient
    LEFT JOIN FETCH ri.unit u
    LEFT JOIN FETCH u.measurement
    LEFT JOIN FETCH r.steps
    LEFT JOIN FETCH r.images img
    LEFT JOIN FETCH img.license
    LEFT JOIN FETCH r.categories
    LEFT JOIN FETCH r.flavors
    LEFT JOIN FETCH r.difficulty
    LEFT JOIN FETCH r.license
    WHERE r.id IN :ids
""")
    List<Recipe> findAllByIdWithRelations(@Param("ids") List<Integer> ids);

    @Query("""
    SELECT DISTINCT r, COUNT(i.id) as matchCount
    FROM Recipe r
    JOIN r.ingredients ri
    JOIN ri.ingredient i
    WHERE LOWER(i.name) IN :ingredientNames
    GROUP BY r
    ORDER BY matchCount DESC
""")
    List<Recipe> findByIngredientsMatchingMultiple(@Param("ingredientNames") List<String> ingredientNames);

    @Query("""
    SELECT new com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeName(r.id, r.title)
    FROM Recipe r
    WHERE r.id IN :ids
""")
    List<RecipeName> findRecipeNamesByIds(@Param("ids") List<Integer> ids);

/*    @Query("""
    Select new com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary(
            r.id, r.title, r.shortDescription, r.totalTimeMin ,ri.imageUrl
    )
    FROM Recipe r
    LEFT JOIN r.images ri
""")
    List<RecipeResponseSummary> findAllSummaries();

    @Query("""
SELECT DISTINCT new com.zabora.recipe_service.recipe_service.model.dtos.recipesdtos.RecipesDTO.RecipeResponseSummary(
        r.id, r.title, r.shortDescription, r.totalTimeMin, ri.imageUrl
)
FROM Recipe r
LEFT JOIN r.images ri
WHERE r.id NOT IN (

    SELECT r2.id
    FROM Recipe r2
    JOIN r2.ingredients ri2
    JOIN ri2.ingredient i
    WHERE LOWER(i.name) IN :ingredientes

)
""")
    List<RecipeResponseSummary> findRecipesWithoutIngredients(
            @Param("ingredientes") List<String> ingredientes
    );*/

//    @Query("""
//    SELECT DISTINCT r
//    FROM Recipe r
//    LEFT JOIN FETCH r.images ri
//    LEFT JOIN FETCH r.ingredients ri2
//    JOIN FETCH ri2.ingredient i
//""")
//    List<Recipe> findAllSummaries();
    
    @EntityGraph(attributePaths = {
    	    "difficulty",
    	    "license"
    	})
    	@Query("""
    	    SELECT r FROM Recipe r
    	""")
    	Page<Recipe> findAllSummaries(Pageable pageable);

//    @Query("""
//    SELECT DISTINCT r
//    FROM Recipe r
//    LEFT JOIN FETCH r.images
//    LEFT JOIN FETCH r.ingredients ri
//    LEFT JOIN FETCH ri.ingredient i
//    LEFT JOIN FETCH ri.unit u
//    LEFT JOIN FETCH u.measurement
//    WHERE r.id NOT IN (
//        SELECT r2.id
//        FROM Recipe r2
//        JOIN r2.ingredients ri2
//        JOIN ri2.ingredient i2
//        WHERE LOWER(i2.name) IN :ingredientes
//    )
//""")
//    List<Recipe> findRecipesWithoutIngredients(@Param("ingredientes") List<String> ingredientes);
    
    @Query("""
    	    SELECT r FROM Recipe r
    	    WHERE r.id NOT IN (
    	        SELECT ri.recipe.id
    	        FROM RecipeIngredient ri
    	        WHERE ri.ingredient.name IN :forbidden
    	    )
    	""")
    	Page<Recipe> findRecipesWithoutIngredients(List<String> forbidden, Pageable pageable);
}


