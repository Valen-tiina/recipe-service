package com.zabora.recipe_service.recipe_service.controller.IngredientsControllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.zabora.recipe_service.recipe_service.model.dtos.ingredientsdtos.MeasurementResponse;
import com.zabora.recipe_service.recipe_service.service.IngredientsServices.MeasurementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    public ResponseEntity<List<MeasurementResponse>> getAllMeasurements() {
        return ResponseEntity.ok(measurementService.findAll());
    }
}
