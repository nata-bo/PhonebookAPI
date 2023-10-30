package com.ait.tests.okhttp;

import com.ait.dto.ContactDto;
import com.ait.dto.ErrorDto;
import com.ait.dto.MessageDto;
import com.google.gson.Gson;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteContactOkhttpTests {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibmJvMDA3QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjk5MDAxMjU4LCJpYXQiOjE2OTg0MDEyNTh9.C7KX46b3yIM0pePkBh5UgsU1qizXeaAHY6g4QkSHIoc";
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    String id;

    @BeforeMethod
    public void precondition() throws IOException {
        // create contact
       ContactDto contactDto = ContactDto.builder()
                .name("Liza")
                .lastName("Cro")
                .address("Bonn")
                .email("lizcro@gmail.com")
                .phone("3456543212")
                .description("lllkkkkkjjj")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(contactDto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(requestBody)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();

        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        String message = messageDto.getMessage();
        System.out.println(message);

       // get id from "message": "Contact was added! ID: a100e140-b87a-4690-895f-8e25ff9c6363"
      String[] all = message.split(": ");
      id = all[1];

    }
     @Test
    public  void deleteContactByIDPositiveTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+ id)
                .delete()
                .addHeader("Authorization",token)
                .build();

         Response response = client.newCall(request).execute();
         Assert.assertEquals(response.code(),200);
         MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
         System.out.println(messageDto.getMessage());
         Assert.assertEquals(messageDto.getMessage(),"Contact was deleted!");
     }

    @Test
    public void deleteContactByIDWrongTokenNegativeTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", "wrongToken")
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getError());
         Assert.assertEquals(errorDto.getError(), "Unauthorized");
    }

    @Test
    public void deleteContactByWrongIDNegativeTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + 1+id)
                .delete()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);
        ErrorDto  errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getError());
        Assert.assertEquals(errorDto.getError(), "Bad Request");
        System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getMessage(), "Contact with id: "+1+id +" not found in your contacts!");


    }






}
