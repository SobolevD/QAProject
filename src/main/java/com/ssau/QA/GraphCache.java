package com.ssau.QA;

import java.util.HashMap;
import java.util.Map;

public class GraphCache {
    private Map<Integer, Integer> childsCountFrequency = new HashMap<>();

    public GraphCache(int maxChildsCountForParent) {
        for (int i = 0; i < maxChildsCountForParent; ++i) {
            childsCountFrequency.put(i, 0);
        }
    }

    public Map<Integer, Integer> getChildsCountFrequency() {
        return childsCountFrequency;
    }

    public void increment(int curChildsCount) {
        childsCountFrequency.replace(curChildsCount, childsCountFrequency.get(curChildsCount) + 1);
    }

    public void print() {
        System.out.println("\nCHILDS STATISTIC: ");
        for (Map.Entry<Integer, Integer> curValue : childsCountFrequency.entrySet()) {
            System.out.println(curValue.getKey() + ":" + curValue.getValue());
        }
    }
}