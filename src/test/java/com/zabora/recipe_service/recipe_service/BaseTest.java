package com.zabora.recipe_service.recipe_service;

import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;

public class BaseTest {
	 @BeforeAll
	    public static void setup() {

	        RestAssured.baseURI = "http://localhost";
	        RestAssured.port = 8001;
	        RestAssured.basePath = "/api";

	    }
}
