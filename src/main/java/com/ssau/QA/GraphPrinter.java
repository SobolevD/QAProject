package com.ssau.QA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GraphPrinter {

    // Data
    private List<Vertex> graph = new ArrayList<>();
    private final GraphCache cache = new GraphCache(Consts.MAX_CHILD_VERTEXES_COUNT);
    private final static int START_VERTEX_NUM = 1;

    int graphType;
    int conditionType;

    public GraphPrinter() {
        this.graphType = GraphType.DETERMINED;
        this.conditionType = ConditionType.CONDITION_A;
        init(Consts.MAX_VERTEXES_COUNT);
    }

    public GraphPrinter(int conditionType, int graphType, int maxVertexesCount) {
        this.graphType = graphType;
        this.conditionType = conditionType;
        init(maxVertexesCount);
    }

    private void init(int maxVertexesCount) {

        int currentVertexLevel = 0;

        graph.add(new Vertex(START_VERTEX_NUM, 0, currentVertexLevel));

        int currentVertexNum = 1;

        // Generates new level of tree
        while (true) {

            // Generate children for current vertex
            List<Vertex> parentsOnCurrentLevel = getParentsByLevel(currentVertexLevel);

            // Bad graph. Try to build another one
            if (parentsOnCurrentLevel.size() == 0) {
                graph = new ArrayList<>();
                init(maxVertexesCount);
                return;
            }

            // For each parent create children
            for (Vertex curParent : parentsOnCurrentLevel) {
                int curChildsCount = getChildsCountForVertex(graphType, Consts.MAX_CHILD_VERTEXES_COUNT);

                // For statistic and diagram
                if (graphType != GraphType.DETERMINED) {
                    cache.increment(curChildsCount);
                }

                if (curChildsCount != 0) {
                    curParent.setHanging(false);
                }
                for (int i = 0; i < curChildsCount; ++i) {

                    if (conditionType == ConditionType.CONDITION_A) {

                        // Condition A
                        if (currentVertexNum >= maxVertexesCount) {
                            if (i == 0) {
                                curParent.setHanging(true);
                            }
                            return;
                        }
                    }

                    Vertex currentVertex = new Vertex(++currentVertexNum, curParent.getNum(), curParent.getLevel() + 1);
                    graph.add(currentVertex);
                }
            }

            ++currentVertexLevel;

            // Condition B

            if (conditionType == ConditionType.CONDITION_B && currentVertexNum >= maxVertexesCount) {
                for (Vertex curVertex : graph) {
                    if (curVertex.getLevel() == currentVertexLevel) {
                        curVertex.setHanging(true);
                    }
                }
                return;
            }

        }

    }

    public void print() {

        int currentLevel = 0;
        System.out.print("Level 0: ");
        for (Vertex vertex : graph) {

            if (vertex.getLevel() > currentLevel) {
                ++currentLevel;
                System.out.println();
                System.out.print("Level " + currentLevel + ": ");
            }
            System.out.print(printVertex(vertex));
        }
    }

    public void printHangingVertex() {
        System.out.println("\nHANGING VERTEXES: ");
        for (Vertex curVertex : getHangingVertexes()) {
            System.out.print(printVertex(curVertex));
        }
        System.out.println();
    }

    public void printCache() {
        if (graphType != GraphType.DETERMINED) {
            cache.print();
        }
    }

    private String printVertex(Vertex vertex) {
        return vertex.getNum() + "-" + vertex.getParent() + " ";
    }

    private List<Vertex> getParentsByLevel(int level) {
        List<Vertex> result = new ArrayList<>();
        for (Vertex vertex : graph) {
            if (vertex.getLevel() == level) {
                result.add(vertex);
            }
        }
        return result;
    }

    public List<Vertex> getHangingVertexes() {

        List<Vertex> result = new ArrayList<>();
        for (Vertex currentVertex : graph) {
            if (currentVertex.isHanging()) {
                result.add(currentVertex);
            }
        }
        return result;
    }

    private int getChildsCountForVertex(int type, int maxChildsCount) {
        if (type == GraphType.DETERMINED) {
            return maxChildsCount - 1;
        } else {
            return new Random().nextInt(maxChildsCount);
        }
    }

    public int getGraphSize() {
        return graph.size();
    }

    public int getHangingListSize() {
        return getHangingVertexes().size();
    }

    public int getHierarchyLevel() {
        return graph.get(graph.size() - 1).getLevel();
    }

    public Map<Integer, Integer> getChildCountFrequency(){
        return cache.getChildsCountFrequency();
    }

    public void printAlpha(double alpha) {
        System.out.println("\nAlpha: " + alpha);
    }

    public List<Vertex> subGraph(int vertexesCount){
        return graph.subList(0, vertexesCount);
    }

    public static void main(String[] args) {

        GraphPrinter gp = new GraphPrinter(Consts.CURRENT_VARIANT_CONDITION, GraphType.NON_DETERMINED, Consts.MAX_VERTEXES_COUNT);
        gp.print();
        gp.printHangingVertex();

        System.out.println("\nMATH EXPECTATION: " + Calculator.getMathExpectationIntInt(gp.getChildCountFrequency(), 1));
        System.out.println("DISPERSION: " + Calculator.getDispersionIntInt(gp.getChildCountFrequency()));

        gp.printCache();
        System.out.println("Graph size: " + gp.getGraphSize() + "; Hanging vertexes count: " + gp.getHangingListSize());
        gp.printAlpha(Calculator.calculateAlpha(gp.getGraphSize(), gp.getHangingListSize()));
        Calculator.printStatisticExcelTable(Consts.GRAPHS_COUNT, Consts.CURRENT_VARIANT_CONDITION);

        Calculator.printAlphaParameterGraphExcel(100);
    }
}
