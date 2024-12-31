package com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
    String name;
    String email;
}
