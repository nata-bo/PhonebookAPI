package com.ait.tests.restAssured;

import com.ait.dto.ContactDto;
import com.ait.dto.ErrorDto;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddContactsTestsRA extends TestBase{

    @Test
    public  void addNewContactSuccessTest(){
        int i = new Random().nextInt(1000)+1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Liza")
                .lastName("Cro")
                .address("Bonn")
                .email("liz"+i+"@gmail.com")
                .phone("345654312"+i)
                .description("lllkkkkkjjj")
                .build();

        String message = given()
                .header("Authorization",TOKEN)
                .contentType(ContentType.JSON)
                .body(contactDto)
                .post("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract().path("message");
        System.out.println(message);


    }
    @Test
    public  void addNewContactWithoutNameTest(){
        ContactDto contactDto = ContactDto.builder()
                .lastName("Cro")
                .address("Bonn")
                .email("liz@gmail.com")
                .phone("34565431232")
                .description("lllkkkkkjjj")
                .build();

        ErrorDto errorDto = given()
                .header("Authorization",TOKEN)
                .contentType(ContentType.JSON)
                .body(contactDto)
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .extract().response().as(ErrorDto.class);
        System.out.println(errorDto.getMessage());
        Assert.assertTrue(errorDto.getMessage().toString().contains("name=must not be blank"));


    }
@Test
    public  void addNewContactInvalidPhoneTest(){
        ContactDto contactDto = ContactDto.builder()
                .name("Liza")
                .lastName("Cro")
                .address("Bonn")
                .email("liz@gmail.com")
                .phone("345654312")
                .description("lllkkkkkjjj")
                .build();

              given()
                .header("Authorization",TOKEN)
                .contentType(ContentType.JSON)
                .body(contactDto)
                .post("contacts")
                .then()
                .assertThat().statusCode(400)
                .assertThat().body("message.phone",
                        containsString("Phone number must contain only digits! And length min 10, max 15!"));




    }

}
