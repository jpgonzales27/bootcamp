package com.juan_pablo.adopcion_mascotas.persistence.specifications;

import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications implements Specification<User> {

    private final String name;
    private final String email;

    public UserSpecifications(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(this.name)) {
            Predicate nameLike = criteriaBuilder.like(root.get("name"), "%" + this.name + "%");
            predicates.add(nameLike);
        }

        if (StringUtils.hasText(this.email)) {
            Predicate nameLike = criteriaBuilder.like(root.get("email"), "%" + this.email + "%");
            predicates.add(nameLike);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
