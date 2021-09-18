package com.ssau.QA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphPrinter {

    // Cache
    private List<Vertex> hangingVertexesCache = null;

    // Data
    private List<Vertex> graph = new ArrayList<>();
    private final static int START_VERTEX_NUM = 1;

    public void init() {

        hangingVertexesCache = null;

        int currentVertexLevel = 0;

        // Start condition

        graph.add(new Vertex(START_VERTEX_NUM, 0, currentVertexLevel));

        int currentVertexNum = 1;
        // while (5 /* Condition A */) {
        while (true) {
            //for (int k = 0; k < 5; ++k) {
            // Generate children for current vertex

            List<Vertex> parentsOnCurrentLevel = getParentsByLevel(currentVertexLevel);

            // For each parent
            for (Vertex curParent : parentsOnCurrentLevel) {
                int curChildsCount = getChildsCountForVertex(GraphType.NON_DETERMINED, Consts.MAX_CHILD_VERTEXES_COUNT);

                if (curChildsCount != 0) {
                    curParent.setHanging(false);
                }
                for (int i = 0; i < curChildsCount; ++i) {

                    //Condition A
                    if (Conditions.conditionA(currentVertexNum)) {
                        if (i == 0) {
                            curParent.setHanging(true);
                        }
                        print();
                        getHangingVertexes();
                        printHangingVertex();
                        return;
                    }

                    Vertex currentVertex = new Vertex(++currentVertexNum, curParent.getNum(), curParent.getLevel() + 1);
                    graph.add(currentVertex);
                }
            }

            //Condition B (check!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
            /*if(Conditions.conditionB(graph.get(graph.size()-1).getNum()))
            {
                return;
            }*/
            ++currentVertexLevel;
            //}
        }

        /*print();
        getHangingVertexes();
        printHangingVertex();*/

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
        for (Vertex curVertex : hangingVertexesCache) {
            System.out.print(printVertex(curVertex));
        }
    }

    private String printVertex(Vertex vertex) {
        return vertex.getNum() + "-" + vertex.getParent() + " ";
    }

    public static void main(String[] args) {

        GraphPrinter gp = new GraphPrinter();
        gp.init();
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

        if (hangingVertexesCache == null || hangingVertexesCache.isEmpty()) {
            List<Vertex> result = new ArrayList<>();
            for (Vertex currentVertex : graph) {
                if (currentVertex.isHanging()) {
                    result.add(currentVertex);
                }
            }
            hangingVertexesCache = result;
            return result;
        }
        // Cache
        return hangingVertexesCache;
    }

    private int getChildsCountForVertex(int type, int maxChildsCount) {
        if (type == GraphType.DETERMINED) {
            return maxChildsCount;
        } else {
            //return new Random().nextInt() % maxChildsCount;
            return new Random().nextInt(maxChildsCount);
        }
    }
}

// 1-0
