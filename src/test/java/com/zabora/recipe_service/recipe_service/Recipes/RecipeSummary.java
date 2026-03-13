package com.zabora.recipe_service.recipe_service.Recipes;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

import com.zabora.recipe_service.recipe_service.BaseTest;

public class RecipeSummary extends BaseTest {
	
	 @Test
	    public void obtenerResumenRecetas() {

	        given()

	        .when()
	            .get("/recipeSummary")

	        .then()
	            .log().all()
	            .statusCode(200)
	            .body("$", not(empty()))
	            .body("[0].title", notNullValue())
	            .body("[0].totalTimeMin", greaterThan(0));
	    }

}
