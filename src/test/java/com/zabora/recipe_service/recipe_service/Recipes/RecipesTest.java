package com.zabora.recipe_service.recipe_service.Recipes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.zabora.recipe_service.recipe_service.BaseTest;

import io.restassured.http.ContentType;
import org.mockito.Mockito;

public class RecipesTest extends BaseTest {

	@Test
	public void crearRecetaOK() {
		Mockito.when(authClient.validateRole("ROLE_ADMIN")).thenReturn(true);

		String body = """
				{
				  "title": "Pasta Carbonara",
				  "shortDescription": "Pasta italiana",
				  "servings": 4,
				  "difficultyId": 2,
				  "licenseName": "Creative Commons BY-SA 4.0",
				  "licenseUrl": "https://creativecommons.org/licenses/by-sa/4.0/",
				  "categoryIds": [2],
				  "flavorIds": [1],
				  "ingredients": [
				    {
				      "ingredientId": 2,
				      "quantity": 400,
				      "unitId": 9
				    }
				  ],
				  "images": [],
				  "steps": [
				    {
				      "stepOrder": 1,
				      "description": "Hervir agua",
				      "timeSeconds": 300,
				      "imageUrl": null
				    }
				  ]
				}
				""";

		 given()
	        .contentType(ContentType.JSON)
	        .header("X-User-Role", "ROLE_ADMIN")
	        .body(body)

	    .when()
	        .post("/api/recipes")

	    .then()
	        .log().all()
	        .statusCode(200)
	        .body("title", equalTo("Pasta Carbonara"));
	}

	@Test
	public void updateRecetaOK() {
		Mockito.when(authClient.validateRole("ROLE_ADMIN")).thenReturn(true);

		String body = """
				{
				  "title": "probando",
				  "shortDescription": "probando",
				  "servings": 4,
				  "difficultyId": 2,
				  "licenseName": "Creative Commons BY-SA 4.0",
				  "licenseUrl": "https://creativecommons.org/licenses/by-sa/4.0/",
				  "categoryIds": [2],
				  "flavorIds": [1],
				  "ingredients": [
				    {
				      "ingredientId": 3,
				      "quantity": 400,
				      "unitId": 9
				    }
				  ],
				  "images": [],
				  "steps": [
				    {
				      "stepOrder": 1,
				      "description": "probando",
				      "timeSeconds": 300,
				      "imageUrl": null
				    }
				  ]
				}
				""";

		given()
				.contentType(ContentType.JSON)
				.header("X-User-Role", "ROLE_ADMIN")
				.body(body)

				.when()
				.put("/api/recipes/1")

				.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	public void deleteRecetaOK() {
		Mockito.when(authClient.validateRole("ROLE_ADMIN")).thenReturn(true);

		given()
				.contentType(ContentType.JSON)
				.header("X-User-Role", "ROLE_ADMIN")


				.when()
				.delete("/api/recipes/2")

				.then()
				.log().all()
				.statusCode(204);
	}

	@Test
	public void obtenerTodasLasRecetas() {

		given()

				.when().get("/api/recipes")

				.then().log().all().statusCode(200).body("$", not(empty()));
	}

	@Test
	public void obtenerRecetaPorId() {

		given()

				.when().get("/api/recipes/1")

				.then().log().all().statusCode(200).body("id", equalTo(1)).body("title", notNullValue());
	}

	@Test
	public void obtenerMultiplesRecetas() {

		given().queryParam("ids", "1,2")

				.when().get("/api/recipes/multiple")

				.then().log().all().statusCode(200).body("$", not(empty())).body("size()", greaterThanOrEqualTo(1));
	}
}
