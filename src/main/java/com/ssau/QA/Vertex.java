package com.ssau.QA;

public class Vertex {

    private int num;
    private int parent;
    private int level;
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

    public void setNum(int num) {
        this.num = num;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Vertex(int num, int parent, int level) {
        this.num = num;
        this.parent = parent;
        this.level = level;
        this.isHanging = true;
    }
}