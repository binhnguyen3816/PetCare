package com.binh.criteria.entity;

import com.binh.criteria.dto.CriteriaStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "criterias")
public class Criteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty
    @NotBlank
    @NotNull
    @Length(min = 2, max = 50)
    @Column(unique = true, nullable = false, length = 50)
    String name;

    @NotEmpty
    @NotBlank
    @NotNull
    @Length(min = 20, max = 200)
    @Column(nullable = false, length = 200)
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CriteriaStatusEnum status = CriteriaStatusEnum.ACTIVE;

    public Criteria(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
