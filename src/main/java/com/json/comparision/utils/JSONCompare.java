package com.json.comparision.utils;

import java.util.Map;

@FunctionalInterface
public interface JSONCompare {
    public Map<String, Object> compareJson(String fileName1, String fileName2);
}
