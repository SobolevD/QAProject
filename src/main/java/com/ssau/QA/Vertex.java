package com.ssau.QA;

public class Vertex {

    private final int num;
    private final int parent;
    private final int level;
    private boolean isHanging;

    public boolean isHanging() {
        return isHanging;
    }

    public void setHanging(boolean hanging) {
        isHanging = hanging;
    }

    public int getNum() {
        return num;
    }

    public int getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public Vertex(int num, int parent, int level) {
        this.num = num;
        this.parent = parent;
        this.level = level;
        this.isHanging = true;
    }
}