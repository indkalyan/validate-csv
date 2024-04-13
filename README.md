# validate-csv
Java Library to easily validate csv using CSV Schemas.

## Inspired By: https://github.com/digital-preservation/csv-validator
The build was not working at the time so I ended up creating something myself.

# How to use
The library is pretty easy to use. Consider a sample CSV file **group_sample.csv**

|GroupName|Groupcode                    |GroupOwner|GroupCategoryID                              |
|---------|-----------------------------|----------|---------------------------------------------|
|System Administrators|sysadmin                     |13456     |100                                          |
|Independence High Teachers|HS Teachers                  |          |101                                          |
|John Glenn Middle Teachers|MS Teachers                  |13458     |102                                          |
|Liberty Elementary Teachers|Elem Teachers                |13559     |103                                          |
|1st Grade Teachers|1stgrade                     |          |104                                          |
|2nd Grade Teachers|2nsgrade                     |13561     |105                                          |
|3rd Grade Teachers|3rdgrade                     |13562     |106                                          |
|Guidance Department|guidance                     |          |107                                          |
|Independence Math Teachers|HS Math                      |13660     |108                                          |
|Independence English Teachers|HS English                   |13661     |109                                          |
|John Glenn 8th Grade Teachers|8thgrade                     |          |110                                          |
|John Glenn 7th Grade Teachers|7thgrade                     |13452     |111                                          |
|Elementary Parents|Elem Parents                 |          |112                                          |
|Middle School Parents|MS Parents                   |18001     |113                                          |
|High School Parents|HS Parents                   |18002     |114                                          |


The validation process is dependent on a CSV schema. The CSV schema is a yaml file which is a simple version of http://digital-preservation.github.io/csv-schema/csv-schema-1.1.html.
The CSV schema is only dependent on regex expressions for now.

A simple CSV schema looks like this:


#### group_schema.yaml
```yaml
---
schemaName: "group_schema"
totalColumns: 4
columns:
  -
    columnName: "GroupName"
    regexPattern: "^[a-zA-Z0-9\\s]*$"
  -
    columnName: "Groupcode"
    regexPattern: "^[a-zA-Z0-9\\s]*$"
  -
    columnName: "GroupOwner"
    regexPattern: "^[a-zA-Z0-9\\s]*$"
  -
    columnName: "GroupCategoryID"
    regexPattern: "^[0-9]*$"
 ```

 To validate,


 ```java
File schemaFile = Paths.get(getClass().getClassLoader().getResource("group_schema.yaml").toURI()).toFile();
File csvFile = Paths.get(getClass().getClassLoader().getResource("group_sample.csv").toURI()).toFile();

CsvSchemaParser schemaParser = new YAMLSchemaParser();
CsvSchema csvSchema = schemaParser.parse(schemaFile);

CsvValidator validator = new SimpleCsvValidator();
List<ErrorMessage> errorMessageList = validator.validate(csvFile, csvSchema);

for (ErrorMessage errorMessage: errorMessageList)
    System.out.println(errorMessage);
```

After running this,

```
[WARN]: The field {columnName: GroupOwner, regexPattern: ^[a-zA-Z0-9\s]*$} value() is empty at row 3.
[WARN]: The field {columnName: GroupOwner, regexPattern: ^[a-zA-Z0-9\s]*$} value() is empty at row 6.
[WARN]: The field {columnName: GroupOwner, regexPattern: ^[a-zA-Z0-9\s]*$} value() is empty at row 9.
[WARN]: The field {columnName: GroupOwner, regexPattern: ^[a-zA-Z0-9\s]*$} value() is empty at row 12.
[WARN]: The field {columnName: GroupOwner, regexPattern: ^[a-zA-Z0-9\s]*$} value() is empty at row 14.
```



# Contributing
Please feel free to contribute if I missed something or you want some advanced features. :)