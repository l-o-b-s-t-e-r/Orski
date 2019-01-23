package com.company;

import java.util.Set;

public class Vertex {

    private int vertex;
    private Set<Edge> edges;

    public Vertex(int v){
        vertex = v;
    }

    public Vertex(int v, Set<Edge> e){
        vertex = v;
        edges = e;
    }

    public int get() {
        return vertex;
    }

    public void set(int vertex) {
        this.vertex = vertex;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex1 = (Vertex) o;

        return vertex == vertex1.vertex;
    }

    @Override
    public int hashCode() {
        return vertex;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "vertex=" + vertex +
                '}';
    }
}
