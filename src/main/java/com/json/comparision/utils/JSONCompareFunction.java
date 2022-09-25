package com.json.comparision.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;

public class JSONCompareFunction implements BiFunction<String, String, Map<String, Object>> {

    private final ObjectMapper objectMapper;

    public JSONCompareFunction(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static String joinString(String[] subKeys, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < subKeys.length; i++) {
            if (i != subKeys.length - 1) {
                builder.append(subKeys[i]).append(delimiter);
            } else {
                builder.append(subKeys[i]);
            }
        }

        return builder.toString();
    }

    @Override
    public Map<String, Object> apply(String fileName1, String fileName2) {
        CommonUtil commonUtil = new CommonUtil(objectMapper);

        Map<String, Object> flattenedMap1 = commonUtil.convertJsonToMap(fileName1);
        Map<String, Object> flattenedMap2 = commonUtil.convertJsonToMap(fileName2);

        if(null!=flattenedMap1 && null!=flattenedMap2 && flattenedMap1.size()>0 && flattenedMap2.size()>0) {
            System.out.println("==============Flattened Map 1============= :: \n" + flattenedMap1 + "\n\n");
            System.out.println("==============Flattened Map 2============= :: \n" + flattenedMap2 + "\n\n");

            Set<String> combinedKeys = new LinkedHashSet<>();
            combinedKeys.addAll(flattenedMap1.keySet());
            combinedKeys.addAll(flattenedMap2.keySet());

            Map<String, Object> resultMap = new LinkedHashMap<>();
            for (String key : combinedKeys) {
                String[] subKeys = key.split("/");
                resultMap.put(subKeys[0], addToResultMap(subKeys, 0, flattenedMap1, flattenedMap2, resultMap));
            }

            //write to excel
            Map<String, Object> excelMap = new LinkedHashMap<>();
            for(String key: combinedKeys){
                excelMap.put(key, getCombinedValue(flattenedMap1, flattenedMap2, key));
            }
            new DocumentGeneratorService().writeToExcel(excelMap, "jsoncompare.xlsx");

            return resultMap;
        }
        return null;
    }

    private Object getCombinedValue(Map<String, Object> flattenedMap1, Map<String, Object> flattenedMap2, String key) {
        return flattenedMap1.get(key) + "/" + flattenedMap2.get(key) + "(" + (
            Objects.equals(flattenedMap1.get(key), flattenedMap2.get(key)) ? "Match" : "Not Match") + ")";
    }

    private Object addToResultMap(String[] subKeys, int index, Map<String, Object> flatennedMap1,
        Map<String, Object> flatennedMap2, Map<String, Object> resultMap) {

        //if subkeys is of only one length, meaning that the key value pair is a simple key value pair like "Name":"Ashwin"
        if (index == subKeys.length - 1) {
            String key = joinString(subKeys, "/");
            return getCombinedValue(flatennedMap1, flatennedMap2, key);
        } else
        //in this case,two complxities may happen, one that the value is a map or the value is of type list
        {
            String val = subKeys[index + 1];
            //next subkey is numberic, that means the value is a list
            if (StringUtils.isNumeric(val)) {
                int ind = Integer.valueOf(val);
                //if map contains the key, then find the list for that key. if list contains "ind" index, that means we
                // just have to add recursively in that list
                //but if list does not contain "ind" index, we have to create a new list and add recursively in this new list
                if (resultMap.containsKey(subKeys[index])) {
                    List<Object> list = (List<Object>) resultMap.get(subKeys[index]);
                    if (list.size() <= ind) {
                        list.add(addToResultMap(subKeys, index + 1, flatennedMap1, flatennedMap2, resultMap));
                        return list;
                    } else {
                        Map<String, Object> tempMap = (Map<String, Object>) list.get(ind);
                        tempMap.put(subKeys[index + 2],
                            addToResultMap(subKeys, index + 2, flatennedMap1, flatennedMap2, tempMap));
                        return list;
                    }

                } else
                //if map does not contain key then create a list and add to map
                {
                    List<Object> list = new ArrayList<>();
                    list.add(addToResultMap(subKeys, index + 1, flatennedMap1, flatennedMap2, resultMap));
                    return list;
                }
            } else
            //next subkey is not numeric, that means the value is a map
            //if map contains the key, then find the map for that key and add recursively
            {
                if (resultMap.containsKey(subKeys[index])) {
                    Map<String, Object> tempMap = (Map<String, Object>) resultMap.get(subKeys[index]);
                    tempMap.put(subKeys[index + 1],
                        addToResultMap(subKeys, index + 1, flatennedMap1, flatennedMap2, tempMap));
                    return tempMap;
                } else
                //if map does not contain the key, then create a new map that key and add recursively
                {
                    Map<String, Object> tempMap = new LinkedHashMap<>();
                    tempMap.put(subKeys[index + 1],
                        addToResultMap(subKeys, index + 1, flatennedMap1, flatennedMap2, resultMap));
                    return tempMap;
                }
            }

        }

    }
}
