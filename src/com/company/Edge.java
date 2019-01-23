package com.company;

public class Edge {

    private Vertex v1;
    private Vertex v2;

    public Edge(String edge) {
        int firstVertex = edge.indexOf(" ");
        int lastVertex = edge.lastIndexOf(" ");

        v1 = new Vertex(Integer.valueOf(edge.substring(firstVertex + 1, lastVertex)));
        v2 = new Vertex(Integer.valueOf(edge.substring(lastVertex + 1, edge.length())));

        /*int firstVertex = 0;
        int secondVertex = 0;
        char c[] = edge.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] != ' ') {
                int k = edge.indexOf(" ", i);
                if (firstVertex == 0) {
                    firstVertex = Integer.valueOf(edge.substring(i, k));
                } else {
                    secondVertex = Integer.valueOf(edge.substring(i, k));
                    break;
                }
                i = k;
            }
        }


        v1 = new Vertex(firstVertex);
        v2 = new Vertex(secondVertex); */
    }

    public boolean contain(Vertex vertex) {
        return v1.get() == vertex.get() || v2.get() == vertex.get();
    }

    public Vertex getAdjacentVertex(Vertex vertex) {
        if (v1.get() == vertex.get()) {
            return v2;
        }

        if (v2.get() == vertex.get()) {
            return v1;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (v1 != null ? !v1.equals(edge.v1) : edge.v1 != null) return false;
        return v2 != null ? v2.equals(edge.v2) : edge.v2 == null;
    }

    @Override
    public int hashCode() {
        int result = v1 != null ? v1.hashCode() : 0;
        result = 31 * result + (v2 != null ? v2.hashCode() : 0);
        return result;
    }

    public Vertex getLeftVertex() {
        return v1;
    }

    public void setLeftVertex(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getRightVertex() {
        return v2;
    }

    public void setRightVertex(Vertex v2) {
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "{" + v1.get() + " - " + v2.get() + '}';
    }
}
