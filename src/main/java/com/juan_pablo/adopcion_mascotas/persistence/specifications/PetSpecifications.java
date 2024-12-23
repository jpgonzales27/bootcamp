package com.juan_pablo.adopcion_mascotas.persistence.specifications;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Pet;
import com.juan_pablo.adopcion_mascotas.persistence.enums.Genre;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PetSpecifications implements Specification<Pet> {
    private final String name;
    private final Long typeId;
    private final String typeName;
    private final Integer minAge;
    private final Integer maxAge;
    private final Genre genre;
    private final Boolean available;

    public PetSpecifications(String name, Long typeId, String typeName, Integer minAge, Integer maxAge, Genre genre, Boolean available) {
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.genre = genre;
        this.available = available;
    }

    @Override
    public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        //root = select * from Pet
        //query = criterios de la consulta en si misma
        //criteriaBuilder = fabrica que te permite construir predicados y expresiones

        List<Predicate> predicates = new ArrayList<>();

        // Filtrar por nombre
        if (StringUtils.hasText(this.name)) {
            Predicate nameLike = criteriaBuilder.like(root.get("name"), "%" + this.name + "%");
            predicates.add(nameLike);
        }

        // Filtrar por tipo de mascota (typeId)
        if (this.typeId != null) {
            Predicate typeEqual = criteriaBuilder.equal(root.get("petType").get("id"), this.typeId);
            predicates.add(typeEqual);
        }

        // Filtrar por nombre tipo de mascota (typeName)
        if (this.typeName != null) {
            Predicate typeNameEqual = criteriaBuilder.equal(root.get("petType").get("typeName"), this.typeName);
            predicates.add(typeNameEqual);
        }

        // Filtrar por rango de edad (minAge y maxAge)
        if (this.minAge != null) {
            Predicate ageGreaterThanOrEqual = criteriaBuilder.greaterThanOrEqualTo(root.get("age"), this.minAge);
            predicates.add(ageGreaterThanOrEqual);
        }

        if (this.maxAge != null) {
            Predicate ageLessThanOrEqual = criteriaBuilder.lessThanOrEqualTo(root.get("age"), this.maxAge);
            predicates.add(ageLessThanOrEqual);
        }

        if(genre!=null){
            Predicate genreEqual = criteriaBuilder.equal(root.get("genre"),this.genre);
            //m.genre = "this.genre"
            predicates.add(genreEqual);
        }

        // Filtrar por disponibilidad
        if (this.available != null) {
            Predicate availableEqual = criteriaBuilder.equal(root.get("available"), this.available);
            predicates.add(availableEqual);
        }

        // Combinar los predicados con AND
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
