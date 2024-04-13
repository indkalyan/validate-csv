package org.rajat.validator.schema;


import lombok.Data;

import java.util.regex.Pattern;
@Data
public class CsvColumn {
    private String columnName;
    private int columnIndex;
    private Pattern compiledRegexPattern;
    private String regexPattern;

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
        this.compiledRegexPattern = Pattern.compile(regexPattern);
    }
}
