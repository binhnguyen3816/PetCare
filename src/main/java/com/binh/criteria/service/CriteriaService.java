package com.binh.criteria.service;

import com.binh.criteria.dto.CriteriaImportResult;
import com.binh.criteria.entity.Criteria;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface CriteriaService {
    Criteria save(Criteria criteria);
    List<Criteria> findAll();
    CriteriaImportResult importCriteria(InputStream inputStream, long fileSize);
}
