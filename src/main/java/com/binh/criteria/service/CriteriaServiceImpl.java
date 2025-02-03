package com.binh.criteria.service;

import com.binh.criteria.constants.CriteriaExceptionMessage;
import com.binh.criteria.dao.CriteriaDAO;
import com.binh.criteria.dto.CriteriaImportResult;
import com.binh.criteria.dto.CriteriaResult;
import com.binh.criteria.entity.Criteria;
import com.binh.exception.TemplateException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class CriteriaServiceImpl implements CriteriaService {

    @Inject
    private CriteriaDAO criteriaDAO;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    public Criteria save(Criteria criteria) {
        return criteriaDAO.save(criteria);
    }

    @Override
    public List<Criteria> findAll() {
        return criteriaDAO.findAll();
    }

    @Override
    public CriteriaImportResult importCriteria(InputStream inputStream, long fileSize) {
        if (fileSize > MAX_FILE_SIZE) {
            throw new BadRequestException(CriteriaExceptionMessage.EXCEED_MAX_SIZE);
        }

        CriteriaImportResult result = new CriteriaImportResult();
        List<Criteria> criteriaListToImport = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            validateSheet(sheet);

            processRows(sheet, result, criteriaListToImport);
        } catch (IOException | POIXMLException | NotOfficeXmlFileException e) {
            throw new BadRequestException(CriteriaExceptionMessage.INVALID_FILE_FORMAT);
        } catch (TemplateException e) {
            throw new TemplateException(CriteriaExceptionMessage.INVALID_TEMPLATE);
        }
        criteriaDAO.saveAll(criteriaListToImport);
        return result;
    }

    private boolean existsByName(String name) {
        return criteriaDAO.existsByName(name);
    }

    private void validateSheet(Sheet sheet) {
        if (sheet.getPhysicalNumberOfRows() <= 1) {
            throw new BadRequestException(CriteriaExceptionMessage.FILE_EMPTY);
        }
    }

    private void processRows(Sheet sheet, CriteriaImportResult result, List<Criteria> criteriaListToImport) {
        Set<String> criteriaNamesInFile = new HashSet<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                validateHeader(row);
                continue;
            }
            processRow(row, result, criteriaListToImport, criteriaNamesInFile);
        }
    }

    private void validateHeader(Row row) {
        String nameLbl;
        String descriptionLbl;
        try {
            nameLbl = row.getCell(0).getStringCellValue().trim();
            descriptionLbl = row.getCell(1).getStringCellValue().trim();
        } catch (Exception e) {
            throw new TemplateException(CriteriaExceptionMessage.INVALID_TEMPLATE);
        }
        if (!"Criteria".equalsIgnoreCase(nameLbl.trim()) || !"Description".equalsIgnoreCase(descriptionLbl.trim())) {
            throw new TemplateException(CriteriaExceptionMessage.INVALID_TEMPLATE);
        }

    }

    private void processRow(Row row, CriteriaImportResult result, List<Criteria> criteriaListToImport, Set<String> criteriaNamesInFile) {
        String criteria = "";
        String description = "";
        List<String> messages = new ArrayList<>();
        boolean isSuccess = true;

        try {
            criteria = row.getCell(0).getStringCellValue().trim();
            if (criteria.isEmpty()) {
                messages.add(CriteriaExceptionMessage.CRITERIA_MISSING);
                isSuccess = false;
            }
        } catch (Exception e) {
            messages.add(CriteriaExceptionMessage.CRITERIA_MISSING);
            isSuccess = false;
        }

        try {
            description = row.getCell(1).getStringCellValue().trim();
            if (description.isEmpty()) {
                messages.add(CriteriaExceptionMessage.DESCRIPTION_MISSING);
                isSuccess = false;
            }
        } catch (Exception e) {
            messages.add(CriteriaExceptionMessage.DESCRIPTION_MISSING);
            isSuccess = false;
        }

        if (criteria.isEmpty() && description.isEmpty()) {
            return;
        }

        if (!criteria.isEmpty() && (criteria.length() < 2 || criteria.length() > 50)) {
            messages.add(CriteriaExceptionMessage.CRITERIA_LENGTH_ERROR);
            isSuccess = false;
        }

        if (!description.isEmpty() && (description.length() < 20 || description.length() > 200)) {
            messages.add(CriteriaExceptionMessage.DESCRIPTION_LENGTH_ERROR);
            isSuccess = false;
        }
        boolean isNameExist = existsByName(criteria);

        if (criteriaNamesInFile.contains(criteria) || isNameExist) {
            messages.add(String.format(CriteriaExceptionMessage.DUPLICATE_NAME_ERROR, criteria));
            isSuccess = false;
        } else {
            criteriaNamesInFile.add(criteria);
        }

        if (isSuccess) {
            messages.add("Criteria: " + criteria + " imported successfully.");
            criteriaListToImport.add(new Criteria(criteria, description));
            updateNoSuccess(result);
        } else {
            updateNoInvalid(result);
        }
        CriteriaResult criteriaResult = new CriteriaResult(row.getRowNum() + 1, messages, isSuccess);
        addCriteria(result, criteriaResult);
        updateNoTotal(result);
    }

    private void addCriteria(CriteriaImportResult result, CriteriaResult criteriaResult) {
        result.getCriteriaImportResults().add(criteriaResult);
    }

    private void updateNoSuccess(CriteriaImportResult result) {
        result.setSuccess(result.getSuccess() + 1);
    }

    private void updateNoInvalid(CriteriaImportResult result) {
        result.setInvalid(result.getInvalid() + 1);
    }

    private void updateNoTotal(CriteriaImportResult result) {
        result.setTotal(result.getTotal() + 1);
    }
}