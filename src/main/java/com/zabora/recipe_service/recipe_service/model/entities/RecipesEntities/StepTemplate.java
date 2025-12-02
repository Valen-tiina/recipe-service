package com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "step_templates")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StepTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(name = "short_desc", length = 255, nullable = false)
    private String shortDescription;

    @Column(name = "image_url", length = 500, nullable = false)
    private String imageUrl;
}
