package com.juan_pablo.adopcion_mascotas.persistence.specifications;

import com.juan_pablo.adopcion_mascotas.persistence.entity.Adoption;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdoptionSpecifications implements Specification<Adoption> {

    private final LocalDate  exactDate;
    private final LocalDate  startDate;
    private final LocalDate endDate;


    public AdoptionSpecifications(LocalDate exactDate, LocalDate startDate, LocalDate endDate) {
        this.exactDate = exactDate;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Predicate toPredicate(Root<Adoption> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.exactDate != null) {
            Predicate exactDatePredicate = criteriaBuilder.equal(root.get("adoptionDate"), this.exactDate);
            predicates.add(exactDatePredicate);
        }

        if (this.startDate != null && this.endDate != null) {
            Predicate dateRangePredicate = criteriaBuilder.between(root.get("adoptionDate"), this.startDate, this.endDate);
            predicates.add(dateRangePredicate);
        } else if (this.startDate != null) {
            Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("adoptionDate"), this.startDate);
            predicates.add(startDatePredicate);
        } else if (this.endDate != null) {
            Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("adoptionDate"), this.endDate);
            predicates.add(endDatePredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
