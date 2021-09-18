package com.ssau.QA;

import java.util.List;

public class Conditions {

    public static boolean conditionA(int currentVertexNumber) {
        return currentVertexNumber >= Consts.MAX_VERTEXES_COUNT;
    }

    public static boolean conditionB(int finalOnLevelVertexNumber) {
        return finalOnLevelVertexNumber >= Consts.MAX_VERTEXES_COUNT;
    }

}