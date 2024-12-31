package com.juan_pablo.adopcion_mascotas.persistence.specifications;

import com.juan_pablo.adopcion_mascotas.persistence.entity.User;
import com.juan_pablo.adopcion_mascotas.persistence.specifications.searchCriteria.UserSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications implements Specification<User> {

    private UserSearchCriteria userSearchCriteria;

    public UserSpecifications(UserSearchCriteria userSearchCriteria) {
        this.userSearchCriteria = userSearchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(this.userSearchCriteria.getName())) {
            Predicate nameLike = criteriaBuilder.like(root.get("name"), "%" + this.userSearchCriteria.getName() + "%");
            predicates.add(nameLike);
        }

        if (StringUtils.hasText(this.userSearchCriteria.getEmail())) {
            Predicate nameLike = criteriaBuilder.like(root.get("email"), "%" + this.userSearchCriteria.getEmail() + "%");
            predicates.add(nameLike);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
