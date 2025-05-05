package com.example.evaluator;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads rules from a CSV file with a metadata row.
 * Row 0: types ("condition"/"action")
 * Row 1: field names
 * Rows 2+: rule values
 */
public class CsvRuleLoader {
    public static <T> List<Rule<T>> load(Class<T> type, File file) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(file));
        List<String[]> rows = reader.readAll();
        reader.close();

        if (rows.size() < 3) {
            throw new IllegalArgumentException(
                    "CSV must have metadata, header, and at least one rule row"
            );
        }

        String[] metadata = rows.get(0);
        String[] headers  = rows.get(1);

        List<Rule<T>> rules = new ArrayList<>();
        for (int i = 2; i < rows.size(); i++) {
            String[] values = rows.get(i);
            Map<String,String> condMap   = new LinkedHashMap<>();
            Map<String,String> actionMap = new LinkedHashMap<>();

            for (int j = 0; j < metadata.length; j++) {
                String typeToken = metadata[j].trim();
                String fieldName = headers[j].trim();
                String rawValue  = j < values.length ? values[j].trim() : "";

                if ("condition".equalsIgnoreCase(typeToken)) {
                    condMap.put(fieldName, rawValue);
                } else if ("action".equalsIgnoreCase(typeToken)) {
                    actionMap.put(fieldName, rawValue);
                }
            }

            // <-- here’s the only change: drop the 'type' arg
            rules.add(Rule.fromMaps(condMap, actionMap));
        }

        return rules;
    }
}
