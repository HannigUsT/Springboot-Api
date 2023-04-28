package com.unieuro.aula.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unieuro.aula.model.CalculatorEntity;
import com.unieuro.aula.repository.CalculatorRepository;
import com.unieuro.aula.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CalculatorController {

    @Autowired
    private CalculatorRepository calcRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/calculator")
    public ResponseEntity<?> getResult(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            Long id = jwtTokenProvider.getUserIdFromToken(token);
            CalculatorEntity calcResult = calcRepository.findById(id).orElse(null);
            if (calcResult != null) {
                return ResponseEntity.ok(calcResult);
            } else {
                CalculatorEntity calcEntity = new CalculatorEntity(id);
                calcRepository.save(calcEntity);
                return ResponseEntity.ok(calcEntity);
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    // Somar
    @PostMapping("/calculator/sum/{nums}")
    public ResponseEntity<?> sumPost(@PathVariable String nums, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            Long id = jwtTokenProvider.getUserIdFromToken(token);
            float[] numsFloat = stringToArrayFloat(nums);
            CalculatorEntity calcResult = calcRepository.findById(id).orElse(null);
            float result = calcResult.getResult();
            for (float num : numsFloat) {
                result += num;
            }
            CalculatorEntity saved = calcRepository.save(new CalculatorEntity(id, result));
            return ResponseEntity.ok(saved);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    // Subtrair
    @PostMapping("/calculator/sub/{nums}")
    public ResponseEntity<?> subtractionPost(@PathVariable String nums, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            Long id = jwtTokenProvider.getUserIdFromToken(token);
            float[] numsFloat = stringToArrayFloat(nums);
            CalculatorEntity calcResult = calcRepository.findById(id).orElse(null);
            float result = calcResult.getResult();
            for (float num : numsFloat) {
                result -= num;
            }
            CalculatorEntity saved = calcRepository.save(new CalculatorEntity(id, result));
            return ResponseEntity.ok(saved);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    // Multiplicar
    @PostMapping("/calculator/mult/{nums}")
    public ResponseEntity<?> multiplicationPost(@PathVariable String nums, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            Long id = jwtTokenProvider.getUserIdFromToken(token);
            float[] numsFloat = stringToArrayFloat(nums);
            CalculatorEntity calcResult = calcRepository.findById(id).orElse(null);
            float result = calcResult.getResult();
            for (float num : numsFloat) {
                result *= num;
            }
            CalculatorEntity saved = calcRepository.save(new CalculatorEntity(id, result));
            return ResponseEntity.ok(saved);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    // Dividir
    @PostMapping("/calculator/div/{nums}")
    public ResponseEntity<?> divisionPost(@PathVariable String nums, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            Long id = jwtTokenProvider.getUserIdFromToken(token);
            float[] numsFloat = stringToArrayFloat(nums);
            CalculatorEntity calcResult = calcRepository.findById(id).orElse(null);
            float result = calcResult.getResult();
            for (float num : numsFloat) {
                if (num == 0) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("error", "Não dá pra dividir por zero");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
                }
                result /= num;
            }
            CalculatorEntity saved = calcRepository.save(new CalculatorEntity(id, result));
            return ResponseEntity.ok(saved);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    public float[] stringToArrayFloat(String nums) {
        String[] numsArray = nums.replaceAll("\\[|\\]", "").split(",");
        float[] numsFloat = new float[numsArray.length];
        for (int i = 0; i < numsArray.length; i++) {
            numsFloat[i] = Float.parseFloat(numsArray[i]);
        }
        return numsFloat;
    }

}