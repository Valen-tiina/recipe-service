package com.zabora.recipe_service.recipe_service.service.CategoriesServices;

import com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities.Flavor;
import com.zabora.recipe_service.recipe_service.repository.CategoriesRepository.FlavorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FlavorService {
    private final FlavorRepository flavorRepo;
    public FlavorService(FlavorRepository flavorRepo) {
        this.flavorRepo = flavorRepo;
    }

    public List<Flavor> findAll() {
        return flavorRepo.findAll();
    }

    public Flavor findById(Integer id) {
        return flavorRepo
                .findById(id)
                .orElseThrow(()-> new RuntimeException("El sabor con ID " + id + " no existe"));
    }
}
