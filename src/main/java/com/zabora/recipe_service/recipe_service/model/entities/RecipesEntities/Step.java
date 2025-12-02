package com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities;
import com.zabora.recipe_service.recipe_service.model.entities.RecipesEntities.Recipe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "steps")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "step_template_id")
    private StepTemplate template;

    @Column(name = "step_order", nullable = false)
    private Integer order;

    @Column(name = "description_step", length = 255)
    private String description;

    @Column(name = "time_seconds")
    private Integer timeSeconds;

    @Column(name = "image_url")
    private String imageUrl;
}
