package com.zabora.recipe_service.recipe_service.Recipes.incorrect;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.zabora.recipe_service.recipe_service.BaseTest;

public class RecipeTestInExisting extends BaseTest{
	@Test
	public void buscarIngredienteSinResultados() {

		given().queryParam("ingredient", "ingredienteQueNoExiste123")

				.when().get("/recipes/search/ingredient")

				.then().log().all().statusCode(200).body("$", empty());
	}

	@Test
	public void buscarTituloSinResultados() {

		given().queryParam("title", "recetaQueNoExisteXYZ")

				.when().get("/recipes/search")

				.then().log().all().statusCode(200).body("$", empty());
	}

	@Test
	public void buscarRecetasPorIdsInexistentes() {

		given().queryParam("ids", "99999,88888")

				.when().get("/recipes/multiple")

				.then().log().all().statusCode(200).body("$", empty());
	}

	@Test
	public void todayMealNoDebeSerNull() {

		given()

				.when().get("/recipes/todayMeal")

				.then().statusCode(200).body("breakfast", notNullValue()).body("lunch", notNullValue())
				.body("dinner", notNullValue());
	}
}
