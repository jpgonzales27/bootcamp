package com.juan_pablo.adopcion_mascotas.dto.response;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPetDTO {
    private Long id;
    private String name;
    private String typeName;
    private Integer age;
    private Boolean available;
    private List<Adoption> adoptions;
}
