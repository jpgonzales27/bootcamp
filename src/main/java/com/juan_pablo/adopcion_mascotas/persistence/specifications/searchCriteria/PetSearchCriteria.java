package com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria;

import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetSearchCriteria {
     String name;
     Long typeId;
     String typeName;
     Integer minAge;
     Integer maxAge;
     Genre genre;
     Boolean available;
}
