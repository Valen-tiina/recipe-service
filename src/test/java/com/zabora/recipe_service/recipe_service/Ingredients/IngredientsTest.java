package com.zabora.recipe_service.recipe_service.Ingredients;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;

import com.zabora.recipe_service.recipe_service.BaseTest;

import io.restassured.http.ContentType;
import org.mockito.Mockito;

public class IngredientsTest extends BaseTest {

    @Test
    public void crearIngrediente() {
        Mockito.when(authClient.validateRole("ROLE_ADMIN")).thenReturn(true);

        String body = """
        {
          "name": "Tomate",
          "imageUrl": "https://ejemplo.com/images/tomate.jpg"
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .header("X-User-Role", "ROLE_ADMIN")
            .body(body)

        .when()
            .post("/ingredient")

        .then()
            .log().all()
            .statusCode(200)
            .body("name", equalTo("Tomate"));
    }

    @Test
    public void updateIngrediente() {
        Mockito.when(authClient.validateRole("ROLE_ADMIN")).thenReturn(true);

        String body = """
        {
          "name": "Tomate",
          "imageUrl": "https://ejemplo.com/images/tomate.jpg"
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .header("X-User-Role", "ROLE_ADMIN")
                .body(body)

                .when()
                .put("/ingredient/2")

                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void deleteIngrediente() {
        Mockito.when(authClient.validateRole("ROLE_ADMIN")).thenReturn(true);

        given()
                .contentType(ContentType.JSON)
                .header("X-User-Role", "ROLE_ADMIN")

                .when()
                .delete("/ingredient/2")

                .then()
                .log().all()
                .statusCode(204);
    }

    @Test
    public void obtenerIngredientes() {

        given()

        .when()
            .get("/ingredients")

        .then()
            .log().all()
            .statusCode(200)
            .body("$", not(empty()));
    }


    @Test
    public void obtenerIngredientePorId() {

        given()

        .when()
            .get("/ingredients/1")

        .then()
            .log().all()
            .statusCode(200)
            .body("id", equalTo(1))
            .body("name", notNullValue());
    }


    @Test
    public void actualizarIngrediente() {

        String body = """
        {
          "name": "Tomate Cherry",
          "imageUrl": "https://ejemplo.com/images/tomatecherry.jpg"
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(body)

        .when()
            .put("/ingredients/1")

        .then()
            .log().all()
            .statusCode(200)
            .body("name", equalTo("Tomate Cherry"));
    }


    @Test
    public void eliminarIngrediente() {

        given()

        .when()
            .delete("/ingredients/1")

        .then()
            .log().all()
            .statusCode(204);
    }

}
