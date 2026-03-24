package com.zabora.recipe_service.recipe_service.Recipes.incorrect;

import com.zabora.recipe_service.recipe_service.BaseTest;
import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Errors extends BaseTest {
    @Test
    public void crearRecetaSinRol() {
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
                .body(body)

                .when()
                .post("/api/recipes")

                .then()
                .log().all()
                .statusCode(403)
                .body("message", equalTo("No tienes permisos para realizar esta acción"));
    }

    @Test
    public void updateRecetaSinRol() {
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
                .body(body)

                .when()
                .put("/api/recipes/1")

                .then()
                .log().all()
                .statusCode(403)
                .body("message", equalTo("No tienes permisos para realizar esta acción"));
    }
}
