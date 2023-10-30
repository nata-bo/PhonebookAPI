package com.ait.tests.okhttp;

import com.ait.dto.ContactDto;
import com.ait.dto.ErrorDto;
import com.ait.dto.GetAllContactsDto;
import com.ait.dto.MessageDto;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsOkhttpTests {

     String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibmJvMDA3QGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjk5MDAxMjU4LCJpYXQiOjE2OTg0MDEyNTh9.C7KX46b3yIM0pePkBh5UgsU1qizXeaAHY6g4QkSHIoc";

     Gson gson = new Gson();
     OkHttpClient client = new OkHttpClient();

   @Test
     public void getAllContactsPositiveTest() throws IOException {
       Request request = new Request.Builder()
               .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
               .get()
               .addHeader("Authorization", token)
               .build();

       Response response = client.newCall(request).execute();
       Assert.assertTrue(response.isSuccessful());
       Assert.assertEquals(response.code(), 200);

       GetAllContactsDto contactsDto = gson.fromJson(response.body().string(), GetAllContactsDto.class);
       List<ContactDto> contacts = contactsDto.getContacts();

       for (ContactDto c : contacts) {
           System.out.println(c.getId());
           System.out.println(c.getEmail());
           System.out.println(c.getName());

       }
   }

    @Test
    public void getAllContactsWithWrongTokenTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", "wrong"+token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);
        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getError(), "Unauthorized");
        System.out.println(errorDto.getError());
    }

}
