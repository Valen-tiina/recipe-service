package com.zabora.recipe_service.recipe_service.model.dtos.authDTO;

import lombok.Data;

import java.util.List;

@Data
public class MedicalInfoResponse {
    private Long userId;
    private String email;
    private List<CondicionMedicaDTO> condicionesMedicas;
    private PreferenciaDTO preferenciaAlimenticia;
    private List<AlergiaDTO> alergias;

}
