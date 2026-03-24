package com.zabora.recipe_service.recipe_service.repository;

import com.zabora.recipe_service.recipe_service.model.dtos.authDTO.MedicalInfoResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "${services.auth.url}")
public interface AuthClient {
    @GetMapping("/api/medical/info/user/{userId}")
    MedicalInfoResponse getUserMedicalInfo(@PathVariable("userId") Long userId);

    @GetMapping("/api/auth/validate-role")
    boolean validateRole(@RequestParam("role") String role);
}
