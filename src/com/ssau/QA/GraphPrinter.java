package com.ssau.QA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphPrinter {

    // Cache
    // private List<Vertex> hangingVertexesCache = null;

    // Data
    private List<Vertex> graph = new ArrayList<>();
    private final static int START_VERTEX_NUM = 1;

    public GraphPrinter() {
        init(ConditionType.CONDITION_A);
    }

    public GraphPrinter(int conditionType) {
        init(conditionType);
    }

    private void init(int conditionType) {

        int currentVertexLevel = 0;

        graph.add(new Vertex(START_VERTEX_NUM, 0, currentVertexLevel));

        int currentVertexNum = 1;

        // Generates new level of tree
        while (true) {

            // Generate children for current vertex
            List<Vertex> parentsOnCurrentLevel = getParentsByLevel(currentVertexLevel);

            // For each parent
            for (Vertex curParent : parentsOnCurrentLevel) {
                int curChildsCount = getChildsCountForVertex(GraphType.DETERMINED, Consts.MAX_CHILD_VERTEXES_COUNT);

                if (curChildsCount != 0) {
                    curParent.setHanging(false);
                }
                for (int i = 0; i < curChildsCount; ++i) {

                    if (conditionType == ConditionType.CONDITION_A) {

                        // Condition A
                        if (Conditions.conditionA(currentVertexNum)) {
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

            if (conditionType == ConditionType.CONDITION_B && Conditions.conditionB(currentVertexNum)) {
                for (Vertex curVertex : graph) {
                    if (curVertex.getLevel() == currentVertexLevel) {
                        curVertex.setHanging(true);
                    }
                }
                return;
            }

            // }
        }

        /*
         * print(); getHangingVertexes(); printHangingVertex();
         */

    }

    public void stopPoint() {
    }

    public void print() {

        int currentLevel = 0;
        for (Vertex vertex : graph) {

            if (vertex.getLevel() > currentLevel) {
                ++currentLevel;
                System.out.println();
            }
            System.out.print(printVertex(vertex));
        }
    }

    public void printHangingVertex() {
        System.out.println("\nHANGING VERTEXES: ");
        for (Vertex curVertex : getHangingVertexes()) {
            System.out.print(printVertex(curVertex));
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
            return maxChildsCount;
        } else {
            // return new Random().nextInt() % maxChildsCount;
            return new Random().nextInt(maxChildsCount);
        }
    }

    public static void main(String[] args) {

        GraphPrinter gp = new GraphPrinter(ConditionType.CONDITION_B);
        gp.print();
        gp.printHangingVertex();
    }
}

// 1-0
