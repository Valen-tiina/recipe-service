package com.zabora.recipe_service.recipe_service.Recipes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.zabora.recipe_service.recipe_service.BaseTest;

public class RecipesSearchTest extends BaseTest {

	@Test
	public void buscarRecetaPorTitulo() {

		given().queryParam("title", "Pollo")

				.when().get("/recipes/search")

				.then().log().all().statusCode(200).body("$", not(empty()))
				.body("[0].title", containsStringIgnoringCase("Pollo"));
	}

	// enpoint: GET /api/recipes/search/ingredient?ingredient=pollo
	@Test
	public void buscarRecetasPorIngrediente() {

		given().queryParam("ingredient", "tomate")

				.when().get("/recipes/search/ingredient")

				.then().log().all().statusCode(200).body("$", not(empty())).body("[0].ingredients", notNullValue());
	}

	@Test
	public void obtenerRecetasDelDia() {

		given()

				.when().get("/recipes/todayMeal")

				.then().log().all().statusCode(200).body("lunch", notNullValue()).body("breakfast", notNullValue())
				.body("dinner", notNullValue());
	}

	@Test
	public void obtenerDesayunos() {

		given()

				.when().get("/recipes/breakfast")

				.then().log().all().statusCode(200).body("$", not(empty())).body("[0].title", notNullValue());
	}

	@Test
	public void obtenerAlmuerzos() {

		given()

				.when().get("/recipes/lunch")

				.then().log().all().statusCode(200).body("$", not(empty()));
	}

	@Test
	public void obtenerCenas() {

		given()

				.when().get("/recipes/dinner")

				.then().log().all().statusCode(200).body("$", not(empty()));
	}

//	@Test
//	public void obtenerMultiplesRecetasPorIds() {
//
//		given().queryParam("ids", "1,5,10")
//
//				.when().get("/recipes/multiple")
//
//				.then().log().all().statusCode(200).body("$", not(empty()));
//	}
}
