package com.juan_pablo.adopcion_mascotas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPetTypeDTO {
    private Long id;
    private String typeName;
}
