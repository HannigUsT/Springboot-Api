package com.unieuro.aula.controller;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unieuro.aula.model.CoinEntity;
import com.unieuro.aula.security.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

@RestController
public class CoinController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/convert")
    public ResponseEntity<?> convertCurrency(@RequestBody CoinEntity coin, HttpServletRequest req) {
        String token = jwtTokenProvider.getTokenFromRequest(req);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonString = mapper.writeValueAsString(coin);
                JSONObject jsonObject = new JSONObject(jsonString);
                String url = "https://api.apilayer.com/exchangerates_data/convert?to="
                        + jsonObject.getString("convertTo")
                        + "&from=" + jsonObject.getString("from")
                        + "&amount=" + jsonObject.getString("amount");
                OkHttpClient client = new OkHttpClient().newBuilder().build();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("apikey", Dotenv.load().get("APIKEY"))
                        .method("GET", null).build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonResult = new JSONObject(responseBody);
                boolean success = jsonResult.getBoolean("success");
                if (success) {
                    Double result = jsonResult.getDouble("result");
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("success", true);
                    jsonResponse.put("result", result);
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(jsonResponse.toString());
                } else {
                    throw new IllegalArgumentException("A Api de moedas deu algum erro.");
                }
            } catch (IOException | JSONException e) {
                return ResponseEntity.badRequest().body(e);
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }
}
