package com.zabora.recipe_service.recipe_service;

import com.zabora.recipe_service.recipe_service.repository.AuthClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseTest {
	@MockitoBean
	protected AuthClient authClient;

	 @BeforeAll
	    public static void setup() {

	        RestAssured.baseURI = "http://localhost";
	        RestAssured.port = 8001;
//	        RestAssured.basePath = "/api";

	    }
	@BeforeEach
	public void mockAuthDefaults() {
		Mockito.when(authClient.validateRole(Mockito.anyString()))
				.thenReturn(false);
	}
}
