package com.ait.tests.restAssured;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;

public class TestBase {

    public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibmJvMDA3QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjk5MjU2OTExLCJpYXQiOjE2OTg2NTY5MTF9.d8lthHW9i2C1npvZPuaLfS3AJLLFfR9hBK7lVbpR6U8";

    @BeforeMethod
    public  void init(){   // precondition
        System.err.close();

        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

    }

}
