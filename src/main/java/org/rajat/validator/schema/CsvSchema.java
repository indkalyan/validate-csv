package org.rajat.validator.schema;

import lombok.Data;

import java.util.List;
@Data
public class CsvSchema {
    private String schemaName;
    private long totalColumns;
    private List<CsvColumn> columns;
}
