package com.ssau.QA;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Calculator {
    public static Double calculateAlpha(int vertexesCount, int hangingVertexesCount) {
        return (double) vertexesCount / (double) hangingVertexesCount;
    }

    public static void printStatisticExcelTable(int graphsCount, int conditionType) {
        // Excel file
        HSSFWorkbook workbook = new HSSFWorkbook();
        // Create sheet with 'Statistic' name
        HSSFSheet sheet = workbook.createSheet("Statistic");

        int rowNum = 0;

        // Headers
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Graph num");
        row.createCell(1).setCellValue("Vertexes count");
        row.createCell(2).setCellValue("Hanging vertexes count");
        row.createCell(3).setCellValue("Alpha");
        row.createCell(4).setCellValue("Hierarchy levels");
        ++rowNum;

        Map<Double, Integer> tableAlpha = new HashMap<>();
        Map<Integer, Integer> tableLeaves = new HashMap<>();
        Map<Integer, Integer> tableLevels = new HashMap<>();

        for (int i = 0; i < graphsCount; ++i) {
            GraphPrinter currentGraph = new GraphPrinter(conditionType, GraphType.NON_DETERMINED, Consts.MAX_VERTEXES_COUNT);

            int graphSize = currentGraph.getGraphSize();
            int hangingVertexesCount = currentGraph.getHangingListSize();
            int hierarchyLevel = currentGraph.getHierarchyLevel();
            double alpha = calculateAlpha(graphSize, hangingVertexesCount);

            Row currentRow = sheet.createRow(rowNum);
            currentRow.createCell(0).setCellValue(i);
            currentRow.createCell(1).setCellValue(graphSize);
            currentRow.createCell(2).setCellValue(hangingVertexesCount);
            currentRow.createCell(3).setCellValue(alpha);
            currentRow.createCell(4).setCellValue(hierarchyLevel);
            tableAlpha.put(alpha, i);
            tableLeaves.put(i, hangingVertexesCount);
            tableLevels.put(hierarchyLevel, i);

            ++rowNum;
        }

        Row maxExpectationRow = sheet.createRow(rowNum);
        maxExpectationRow.createCell(0).setCellValue("Math expectation (alpha): ");
        maxExpectationRow.createCell(1).setCellValue(getMathExpectationDoubleInt(tableAlpha, 1));
        maxExpectationRow.createCell(2).setCellValue("Math expectation (leaves): ");
        maxExpectationRow.createCell(3).setCellValue(getMathExpectationIntInt(tableLeaves, 1));
        maxExpectationRow.createCell(4).setCellValue("Math expectation (levels): ");
        maxExpectationRow.createCell(5).setCellValue(getMathExpectationIntInt(tableLevels, 1));

        ++rowNum;

        Row dispersionRow = sheet.createRow(rowNum);
        dispersionRow.createCell(0).setCellValue("Dispersion (alpha): ");
        dispersionRow.createCell(1).setCellValue(getDispersionDoubleInt(tableAlpha));
        dispersionRow.createCell(2).setCellValue("Dispersion (leaves): ");
        dispersionRow.createCell(3).setCellValue(getDispersionIntInt(tableLeaves));
        dispersionRow.createCell(4).setCellValue("Dispersion (levels): ");
        dispersionRow.createCell(5).setCellValue(getDispersionIntInt(tableLevels));

        // Save excel
        try (FileOutputStream out = new FileOutputStream(new File(PathConsts.PATH_TO_SAVE_STATISTIC_FILE))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel saved!");
    }

    public static Double getMathExpectationIntInt(Map<Integer, Integer> table, int pow){
        double numerator = 0.0;
        double denominator = 0.0;
        for(Map.Entry<Integer, Integer> currFreq: table.entrySet()){
            numerator += Math.pow(currFreq.getKey(), pow) * currFreq.getValue();
            denominator += currFreq.getValue();
        }

        return numerator/denominator;
    }

    public static Double getMathExpectationDoubleInt(Map<Double, Integer> table, int pow){
        double numerator = 0.0;
        double denominator = 0.0;
        for(Map.Entry<Double, Integer> currFreq: table.entrySet()){
            numerator += Math.pow(currFreq.getKey(), pow) * currFreq.getValue();
            denominator += currFreq.getValue();
        }

        return numerator/denominator;
    }

    public static Double getDispersionIntInt(Map<Integer, Integer> table){
        return Calculator.getMathExpectationIntInt(table, 2) - Math.pow(Calculator.getMathExpectationIntInt(table, 1) ,2.0);
    }

    public static Double getDispersionDoubleInt(Map<Double, Integer> table){
        return Calculator.getMathExpectationDoubleInt(table, 2) - Math.pow(Calculator.getMathExpectationDoubleInt(table, 1) ,2.0);
    }

    public static void printAlphaParameterGraphExcel(int maxVertexesCount, int accuracy) {

        final int minAccuracy = 1;
        final int maxAccuracy = 20;

        if (accuracy < minAccuracy)
        {
            accuracy = minAccuracy;
        }

        if (accuracy > maxAccuracy)
        {
            accuracy = maxAccuracy;
        }

        // Excel file
        HSSFWorkbook workbook = new HSSFWorkbook();
        // Create sheet with 'Statistic' name
        HSSFSheet sheet = workbook.createSheet("Alpha");

        int rowNum = 0;

        // Headers
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("Vertexes count");
        row.createCell(1).setCellValue("Average alpha");
        ++rowNum;

        for (int i = 1; i < maxVertexesCount; ++i) {

            double sumAllAlpha = 0.0;

            for (int j = 0; j < accuracy; ++j){
                GraphPrinter currentGraph = new GraphPrinter(/* Do not clear this field. It is true */ ConditionType.CONDITION_A, GraphType.NON_DETERMINED, i);
                sumAllAlpha+=calculateAlpha(currentGraph.getGraphSize(), currentGraph.getHangingListSize());
            }

            double averageAlpha = sumAllAlpha/accuracy;

            Row currentRow = sheet.createRow(rowNum);
            currentRow.createCell(0).setCellValue(i);
            currentRow.createCell(1).setCellValue(averageAlpha);

            ++rowNum;
        }

        // Save excel
        try (FileOutputStream out = new FileOutputStream(new File(PathConsts.PAHT_TO_SAVE_ALPHA_VALUES_FILE))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel saved!");
    }
}