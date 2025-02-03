package com.binh.criteria.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CriteriaImportResult {
    int total;
    int success;
    int invalid;
    List<CriteriaResult> criteriaImportResults = new ArrayList<>();
}

