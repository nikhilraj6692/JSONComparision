package com.json.comparision.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CommonUtil {

    private final ObjectMapper objectMapper;

    public CommonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //method to flatten a json to a map..see output for further details
    public void flattenMap(Map<String, Object> flattenedMap, Map<String, Object> map) {
        Iterator<Entry<String, Object>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<?, ?> obj = itr.next();
            flattenMapUtil(obj, flattenedMap);
        }
    }

    public Map<String, Object> convertJsonToMap(String fileName) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(fileName);
            Map<String, Object> map = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {
            });

            Map<String, Object> flattenedMap = new LinkedHashMap<>();
            flattenMap(flattenedMap, map);
            return flattenedMap;
        } catch (Exception e) {
            // show error message
            e.printStackTrace();
        }

        return null;
    }

    private void flattenMapUtil(Entry<?, ?> obj, Map<String, Object> flattenedMap) {
        if (obj.getValue() instanceof Map<?, ?>) {
            Iterator<Entry<String, Object>> itr = ((Map<String, Object>) obj.getValue()).entrySet().iterator();
            while (itr.hasNext()) {
                Entry<?, ?> innerObj = itr.next();
                flattenMapUtil(new SimpleEntry<>(obj.getKey() + "/" + innerObj.getKey(), innerObj.getValue()), flattenedMap);
            }

        } else if (obj.getValue() instanceof List<?>) {
            for (int i = 0; i < ((List) obj.getValue()).size(); i++) {
                Iterator<Entry<String, Object>> itr = ((Map<String, Object>) ((List) obj.getValue()).get(i)).entrySet()
                    .iterator();
                while (itr.hasNext()) {
                    Entry<?, ?> innerObj = itr.next();
                    flattenMapUtil(new SimpleEntry<>(obj.getKey() + "/" + i + "/" + innerObj.getKey(), innerObj.getValue()),
                        flattenedMap);
                }
            }
        } else {
            flattenedMap.put((String) obj.getKey(), obj.getValue());
        }
    }


}
