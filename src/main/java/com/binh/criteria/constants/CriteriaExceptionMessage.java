package com.binh.criteria.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaExceptionMessage {
    public static final String FILE_EMPTY = "The file is empty.";
    public static final String INVALID_TEMPLATE = "The file does not follow the required template.";
    public static final String CRITERIA_MISSING = "Criteria is missing.";
    public static final String DESCRIPTION_MISSING = "Description is missing.";
    public static final String CRITERIA_LENGTH_ERROR = "Criteria must be more than 2 characters and less than 50 characters.";
    public static final String DESCRIPTION_LENGTH_ERROR = "Description must be more than 20 characters and less than 200 characters.";
    public static final String DUPLICATE_NAME_ERROR = "%s is duplicated.";
    public static final String INVALID_FILE_FORMAT = "The file must be .xlsx format.";
    public static final String IMPORT_SUCCESS = "Criteria import successful";
    public static final String EXCEED_MAX_SIZE = "The file size exceeds the maximum size of 5MB.";
}
