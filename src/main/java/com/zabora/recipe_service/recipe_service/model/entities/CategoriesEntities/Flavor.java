package com.zabora.recipe_service.recipe_service.model.entities.CategoriesEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="flavors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flavor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name_flavor", length = 10)
    private String name;
}
