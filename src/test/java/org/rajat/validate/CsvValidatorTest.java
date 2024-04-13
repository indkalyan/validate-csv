package org.rajat.validate;


import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.rajat.validator.schema.CsvSchema;
import org.rajat.validator.schema.YAMLSchemaParser;
import org.rajat.validator.validate.ErrorMessage;
import org.rajat.validator.validate.SimpleCsvValidator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CsvValidatorTest {

    @Test
    public void schemaTest() throws IOException {

        YAMLSchemaParser schemaParser = new YAMLSchemaParser();
        CsvSchema csvSchema = schemaParser.parse(
                IOUtils.toString(getClass().getClassLoader().getResourceAsStream("group_schema.yaml"), "utf-8"));

        assertNotNull(csvSchema);
        assertEquals("group_schema", csvSchema.getSchemaName());
        assertEquals(4, csvSchema.getTotalColumns());
        for (int i=0;i<csvSchema.getTotalColumns(); i++) {
            assertNotNull(csvSchema.getColumns().get(i));
            if (i == 0) {
                assertEquals("GroupName", csvSchema.getColumns().get(i).getColumnName());
                assertEquals("^[a-zA-Z0-9\\s]*$", csvSchema.getColumns().get(i).getCompiledRegexPattern().pattern());
            }
            if (i == 1) {
                assertEquals("Groupcode", csvSchema.getColumns().get(i).getColumnName());
                assertEquals("^[a-zA-Z0-9\\s]*$", csvSchema.getColumns().get(i).getCompiledRegexPattern().pattern());
            }
            if (i == 2) {
                assertEquals("GroupOwner", csvSchema.getColumns().get(i).getColumnName());
                assertEquals("^[a-zA-Z0-9\\s]*$", csvSchema.getColumns().get(i).getCompiledRegexPattern().pattern());
            }
            if (i == 3) {
                assertEquals("GroupCategoryID", csvSchema.getColumns().get(i).getColumnName());
                assertEquals("^[0-9]*$", csvSchema.getColumns().get(i).getCompiledRegexPattern().pattern());
            }
        }
    }

    @Test
    public void mainTest() throws IOException, URISyntaxException {

        File schemaFile = Paths.get(getClass().getClassLoader().getResource("group_schema.yaml").toURI()).toFile();
        File csvFile = Paths.get(getClass().getClassLoader().getResource("group_sample.csv").toURI()).toFile();

        YAMLSchemaParser schemaParser = new YAMLSchemaParser();
        CsvSchema csvSchema = schemaParser.parse(schemaFile);

        SimpleCsvValidator validator = new SimpleCsvValidator();
        List<ErrorMessage> errorMessageList = validator.validate(csvFile, csvSchema);

        for (ErrorMessage errorMessage: errorMessageList)
            System.out.println(errorMessage);
    }

}
