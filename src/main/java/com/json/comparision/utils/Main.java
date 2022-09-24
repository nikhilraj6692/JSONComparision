package com.json.comparision.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        JSONCompareFunction jsonCompareFunction = new JSONCompareFunction(mapper);
        Map<String, Object> resultMap = jsonCompareFunction.apply("file1.json", "file2.json");

        if (null != resultMap && resultMap.size() > 0) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("output.json"), resultMap);
        }
    }
}



