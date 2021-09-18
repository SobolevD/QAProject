package com.ssau.QA;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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

        for (int i = 0; i < graphsCount; ++i) {
            GraphPrinter currentGraph = new GraphPrinter(GraphType.NON_DETERMINED, conditionType);

            int graphSize = currentGraph.getGraphSize();
            int hangingVertexesCount = currentGraph.getHangingListSize();
            int hierarchyLevel = currentGraph.getHierarchyLevel();

            Row currentRow = sheet.createRow(rowNum);
            currentRow.createCell(0).setCellValue(i);
            currentRow.createCell(1).setCellValue(graphSize);
            currentRow.createCell(2).setCellValue(hangingVertexesCount);
            currentRow.createCell(3).setCellValue(calculateAlpha(graphSize, hangingVertexesCount));
            currentRow.createCell(4).setCellValue(hierarchyLevel);
            ++rowNum;
        }

        // Save excel
        try (FileOutputStream out = new FileOutputStream(new File("C:\\Users\\Nikita\\Desktop\\lab4.xls"))) {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Excel saved!");
    }

    public static Double getMathExpectation (Map<Integer, Integer> childsCountFrequency, int pow){
        double numerator = 0.0;
        double denominator = 0.0;
        for(Map.Entry<Integer, Integer> currFreq: childsCountFrequency.entrySet()){
            numerator += Math.pow(currFreq.getKey(), pow) * currFreq.getValue();
            denominator += currFreq.getValue();
        }

        return numerator/denominator;
    }

    public static Double getDispersion (Map<Integer, Integer> childsCountFrequency){
        return getMathExpectation(childsCountFrequency, 2) - Math.pow(getMathExpectation(childsCountFrequency, 1) ,2);
    }
}