package com.juan_pablo.adopcion_mascotas.persistence.specifications;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.PetSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PetSpecifications implements Specification<Pet> {
    private PetSearchCriteria petSearchCriteria;


    public PetSpecifications(PetSearchCriteria petSearchCriteria) {
        this.petSearchCriteria = petSearchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        //root = select * from Pet
        //query = criterios de la consulta en si misma
        //criteriaBuilder = fabrica que te permite construir predicados y expresiones

        List<Predicate> predicates = new ArrayList<>();

        // Filtrar por nombre
        if (StringUtils.hasText(this.petSearchCriteria.getName())) {
            Predicate nameLike = criteriaBuilder.like(root.get("name"), "%" + this.petSearchCriteria.getName() + "%");
            predicates.add(nameLike);
        }

        // Filtrar por tipo de mascota (typeId)
        if (this.petSearchCriteria.getTypeId() != null) {
            Predicate typeEqual = criteriaBuilder.equal(root.get("petType").get("id"), this.petSearchCriteria.getTypeId());
            predicates.add(typeEqual);
        }

        // Filtrar por nombre tipo de mascota (typeName)
        if (this.petSearchCriteria.getTypeName() != null) {
            Predicate typeNameEqual = criteriaBuilder.equal(root.get("petType").get("typeName"), this.petSearchCriteria.getTypeName());
            predicates.add(typeNameEqual);
        }

        // Filtrar por rango de edad (minAge y maxAge)
        if (this.petSearchCriteria.getMinAge() != null) {
            Predicate ageGreaterThanOrEqual = criteriaBuilder.greaterThanOrEqualTo(root.get("age"), this.petSearchCriteria.getMinAge());
            predicates.add(ageGreaterThanOrEqual);
        }

        if (this.petSearchCriteria.getMaxAge() != null) {
            Predicate ageLessThanOrEqual = criteriaBuilder.lessThanOrEqualTo(root.get("age"), this.petSearchCriteria.getMaxAge());
            predicates.add(ageLessThanOrEqual);
        }

        if (this.petSearchCriteria.getGenre() != null) {
            Predicate genreEqual = criteriaBuilder.equal(root.get("genre"), this.petSearchCriteria.getGenre());
            //m.genre = "this.genre"
            predicates.add(genreEqual);
        }

        // Filtrar por disponibilidad
        if (this.petSearchCriteria.getAvailable() != null) {
            Predicate availableEqual = criteriaBuilder.equal(root.get("available"), this.petSearchCriteria.getAvailable());
            predicates.add(availableEqual);
        }

        // Combinar los predicados con AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
