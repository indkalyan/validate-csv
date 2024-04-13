package org.rajat.validator.validate;


import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;
import org.rajat.validator.schema.CsvColumn;
import org.rajat.validator.schema.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleCsvValidator {

    public List<ErrorMessage> validate(File csvFile, CsvSchema schema) throws IOException {
        return validationHelper(csvFile, schema);
    }

    public List<ErrorMessage> validationHelper(File file, CsvSchema schema) throws IOException {

        List<ErrorMessage> errorMessageList = new ArrayList<>();
        List<String> schemaHeaders = schema.getColumns().stream().map(col -> col.getColumnName()).collect(Collectors.toList());

        final CsvReader.CsvReaderBuilder builder = CsvReader.builder().detectBomHeader(true);
        try (Stream<CsvRecord> stream = builder.ofCsvRecord(file.toPath()).stream()) {
            stream.forEach(row -> {
                // Validation 1: Check the number of columns is equal to totalColumns.
                if (row.getFieldCount() != schema.getTotalColumns()) {
                    errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR, "The totalColumns specified in the schema(" + schema.getTotalColumns() + ") is " + "not equal to total columns in the csv(" + row.getFieldCount() + ") at row " + row.getStartingLineNumber() + "."));
                }

//                // Validation 2: Check all field names in schema matches the ones in the csv.
//                if (checkHeader) {              // Check header just once
//                    errorMessageList.addAll(compareHeaders(csvParser.getHeader(), schemaHeaders));
//                    checkHeader = false;
//                }

                // Validation 3: Check each field's value matches its corresponding regex or not.
                for (int i = 0; i < schema.getTotalColumns(); i++) {
                    CsvColumn column = schema.getColumns().get(i);

                    String fieldValue = row.getField(column.getColumnIndex()-1);
                    //System.out.println(column);

                    if (fieldValue == null) {
                        errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.WARN, "The field " + column + " value(" + fieldValue + ") is null at row " + row.getStartingLineNumber() + "."));
                    } else if (fieldValue.equalsIgnoreCase("")) {
                        errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.WARN, "The field " + column + " value(" + fieldValue + ") is empty at row " + row.getStartingLineNumber() + "."));
                    } else {
                        if (!column.getCompiledRegexPattern().matcher(fieldValue).matches()) {
                            errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR, "The field " + column + " value(" + fieldValue + ") doesn't matches its regex at row " + row.getStartingLineNumber() + "."));
                        }
                    }
                }
            });
        }
        return errorMessageList;
    }

    private List<ErrorMessage> compareHeaders(List<String> csvHeader, List<String> schemaHeader) {

        List<ErrorMessage> errorMessageList = new ArrayList<>();

        // Validation 2a: If csvHeader and schemaHeader are not equal in size.
        if (csvHeader.size() != schemaHeader.size())
            errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR, "The csvHeader and the schemaHeader are not equal in length."));

        // Validation 2b: If there's some column mismatch between csvHeader and schemaHeader.
        for (int i = 0; i < csvHeader.size(); i++) {
            if (!csvHeader.get(i).equals(schemaHeader.get(i)))
                errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR, "The csvHeader(" + csvHeader.get(i) + ") is not equal to the schemaHeader(" + schemaHeader.get(i) + ")"));
        }

        return errorMessageList;
    }

}
