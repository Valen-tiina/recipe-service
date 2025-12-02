package com.zabora.recipe_service.recipe_service.service.IngredientsServices;

import com.zabora.recipe_service.recipe_service.model.entities.IngredientsEntities.Measurement;
import com.zabora.recipe_service.recipe_service.repository.IngredientsRepository.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MeasurementService {
    private final MeasurementRepository measurementRepo;

    public MeasurementService(MeasurementRepository measurementRepo) {
        this.measurementRepo = measurementRepo;
    }

    public List<Measurement> findAll(){
        return measurementRepo.findAll();
    }
    public Measurement findById(Integer id){
        return measurementRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("La medida con ID "+ id + " no existe"));
    }
}
