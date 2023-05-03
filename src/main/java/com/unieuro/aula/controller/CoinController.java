package com.unieuro.aula.controller;

import org.springframework.web.bind.annotation.RestController;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;
import okhttp3.*;

@RestController
public class CoinController {

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("APIKEY");

    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://api.apilayer.com/exchangerates_data/convert?to=USD&from=BRL&amount=15000")
                .addHeader("apikey", apiKey)
                .method("GET", null).build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        return ResponseEntity.ok().body(null);
    }
}
