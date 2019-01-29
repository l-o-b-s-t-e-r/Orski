package com.company;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Controller {

    @FXML
    private BarChart<String, Integer> chart;

    private Graph graph;
    private Random r = new Random();
    private List<List<Integer>> combinations;
    private List<List<Integer>> bestCandidatesList;
    private final Integer ALPHA = 3;
    private Integer covered, candidates, bestCandidatesCount, parentsCountPerChild, childCountFromParents, mutationGenCount, maxLifeCyclesNumber;
    private Float genGenerateProbability, crossOverProbability, mutationProbability;
    private XYChart.Series series;
    private String dataSetPath = "/Users/macos/Downloads/frb30-15-mis/frb30-15-1.mis";
    private int MAX_CYCLES = 50;
    private List<Result> results = new ArrayList<>();

    public void loadDataSet() {
        for (int i = 0; i < 10; i++) {
            series = new XYChart.Series();
            chart.getXAxis().setLabel("Life Cycle #");
            chart.getYAxis().setLabel("Function");
            init();
            loadGraph();
            startAlgorithm();
            chart.getData().clear();
            chart.getData().add(series);
        }

        Result avgResult = Result.calculateAVG(results);
        results.add(avgResult);
        for (Result r: results) {
            System.out.println(r.toString());
        }
    }

    private void init() {
        genGenerateProbability = 0.5f;
        crossOverProbability = 0.5f;
        mutationProbability = 0.05f;
        parentsCountPerChild = 2;
        childCountFromParents = 1;
        candidates = 12;
        bestCandidatesCount = 4;
        mutationGenCount = 1;
        maxLifeCyclesNumber = 10;
    }

    private void loadGraph() {
        List<Edge> edges = new ArrayList<>();
        Map<Vertex, Set<Edge>> vertices = new HashMap<>();
        try {
            String s;
            BufferedReader in = new BufferedReader(new FileReader(dataSetPath));

            in.readLine();
            Edge edge;
            while ((s = in.readLine()) != null) {
                edge = new Edge(s);
                vertices.put(edge.getLeftVertex(), new HashSet<Edge>());
                vertices.put(edge.getRightVertex(), new HashSet<Edge>());
                edges.add(new Edge(s));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        graph = new Graph(edges, vertices);
    }

    private void startAlgorithm() {
        List<List<Integer>> candidatesList = generateCandidatesList();

        int bestF = 0;
        int bestCandidateF = Integer.MAX_VALUE;
        int countSameF = 0;
        List<List<Integer>> bestCandidate;

        bestCandidatesList = findBest(candidatesList);
        System.out.println("bestCandidatesCount list size: " + bestCandidatesList.size());

        combinations = combinations(parentsCountPerChild, bestCandidatesList.size());
        System.out.println("parentsCountPerChild: " + parentsCountPerChild + " || bestCandidatesList: " + bestCandidatesList.size());
        System.out.println("combinations: " + combinations.size());

        System.out.println("Populacja przed krzyżowaniem oraz mutacją: ");
        int a = 0;
        for (List<Integer> s : candidatesList) {
            Integer f = F(s, graph);
            a += f;
            System.out.println("F: " + f + " covered/uncovered: " + covered + "/" + (graph.sizeEdges() - covered) + " - " + graph.involvedVertices + " || solution: " + s);
        }
        System.out.println("Sum F = " + a);
        System.out.println("=====================================================================================");
        System.out.println("Najlepsze osobniki");
        int sumFofbestCandidate1 = 0;
        for (List<Integer> s : bestCandidatesList) {
            Integer f = F(s, graph);
            sumFofbestCandidate1 += f;
            System.out.println("F: " + f + " covered/uncovered: " + covered + "/" + (graph.sizeEdges() - covered) + " - " + graph.involvedVertices + " || solution: " + s);
        }
        System.out.println("Sum F = " + sumFofbestCandidate1);

        long runTime = System.currentTimeMillis();
        int i = 0;
        List<List<Integer>> children = new ArrayList<>();
        while (countSameF < maxLifeCyclesNumber && i < MAX_CYCLES) {
            bestCandidateF = Integer.MAX_VALUE;
            System.out.println("=====================================================================================");
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.println("Life cycle: " + ++i);

            for (List<Integer> combination : combinations) {
                children.addAll(crossover(getParents(combination)));
            }

            System.out.println("Populacja po krzyżowaniem: ");
            for (List<Integer> s : children) {
                Integer f = F(s, graph);
                System.out.println("F: " + f + " covered/uncovered: " + covered + "/" + (graph.sizeEdges() - covered) + " - " + graph.involvedVertices + " || solution: " + s);
            }

            mutation(children);
            System.out.println("Populacja po mutacją: ");

            int sumFofPopulation = 0;
            for (List<Integer> s : children) {
                Integer f = F(s, graph);
                sumFofPopulation += f;
                System.out.println("F: " + f + " covered/uncovered: " + covered + "/" + (graph.sizeEdges() - covered) + " - " + graph.involvedVertices + " || solution: " + s);
            }
            System.out.println("Sum F = " + sumFofPopulation);

            System.out.println("=====================================================================================");
            System.out.println("Najlepsze osobniki po " + i + " iteracji");

            if (!children.isEmpty()) {
                bestCandidatesList = findBest(new ArrayList<>(children));
                children.clear();
            }

            int sumFofbestCandidate = 0;
            for (List<Integer> s : bestCandidatesList) {
                Integer f = F(s, graph);
                sumFofbestCandidate += f;

                if (f < bestCandidateF) {
                    bestCandidateF = f;
                }

                System.out.println("F: " + f + " covered/uncovered: " + covered + "/" + (graph.sizeEdges() - covered) + " - " + graph.involvedVertices + " || solution: " + s);
            }
            series.getData().add(new XYChart.Data(String.valueOf(i), sumFofbestCandidate));
            System.out.println("Sum F = " + sumFofbestCandidate);

            if (bestF == sumFofbestCandidate) {
                countSameF++;
            }
            bestF = sumFofbestCandidate;
            System.out.println("Repeat best F = " + countSameF);
        }
        System.out.println("");
        System.out.println("");
        System.out.println("====================================================");
        double time = ((double) (System.currentTimeMillis() - runTime) / 1000);
        System.out.println("Czas wykonania = " + time + " sek.");

        results.add(new Result(time, i, bestCandidateF, bestF, findDifferentSolutions()));
    }

    private List<List<Integer>> generateCandidatesList() {
        List<List<Integer>> CL = new ArrayList<>();
        while (CL.size() < candidates) {
            CL.add(generateCandidate(graph));
        }

        return CL;
    }

    private List<Integer> generateCandidate(Graph graph) {
        List<Integer> candidate = new ArrayList<>();
        for (int i = 0; i < graph.sizeVertex(); i++) {
            if (getRandomBoolean()) {
                candidate.add(1);
            } else {
                candidate.add(0);
            }
        }

        return candidate;
    }

    private List<List<Integer>> findBest(List<List<Integer>> candidatesList) {
        Collections.sort(candidatesList, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return F(o1, graph) - F(o2, graph);
            }
        });

        return candidatesList.subList(0, bestCandidatesCount);
    }

    private List<List<Integer>> combinations(int parentsCountPerChild, int bestCandidateListSize) {
        return createCombinations(new ArrayList<Integer>(), new ArrayList<List<Integer>>(), parentsCountPerChild, bestCandidateListSize, bestCandidateListSize);
    }

    private List<List<Integer>> createCombinations(List<Integer> current, List<List<Integer>> allCombinations, int k, int n, int size) {
        if (current.isEmpty()) {
            for (int i = 0; i < n; i++) {
                current.add(i);
                createCombinations(current, allCombinations, k - 1, n - 1, size);
                current.clear();
            }

            return allCombinations;
        } else {
            if (k > 0) {
                int initSize = current.size();
                for (int i = 0; i < size; i++) {
                    if (!current.contains(i)) {
                        current.add(i);
                        createCombinations(current, allCombinations, k - 1, n - 1, size);
                        current = current.subList(0, initSize);
                    }
                }
            } else {
                allCombinations.add(new ArrayList<>(current));
                return allCombinations;
            }

            return allCombinations;
        }
    }

    private boolean getRandomBoolean() {
        return r.nextFloat() < genGenerateProbability;
    }

    private boolean getRandomBooleanForCross() {
        return r.nextFloat() < crossOverProbability;
    }

    private boolean getRandomBooleanForMutation() {
        return r.nextFloat() < mutationProbability;
    }

    private List<List<Integer>> getParents(List<Integer> combination) {
        List<List<Integer>> parents = new ArrayList<>();

        for (int index : combination) {
            parents.add(bestCandidatesList.get(index));
        }

        return parents;
    }

    private List<List<Integer>> crossover(List<List<Integer>> parents) {
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < childCountFromParents; i++) {
            children.add(createChild(parents));
            System.out.println("solution: " + parents);
        }

        return children;
    }

    private List<Integer> createChild(List<List<Integer>> parents) {
        List<Integer> child = new ArrayList<>();
        int dividerIndex;
        if (getRandomBooleanForMutation()) {
            dividerIndex = r.nextInt(graph.sizeVertex());
        } else {
            dividerIndex = 0;
        }

        child.addAll(parents.get(0).subList(0, dividerIndex));
        child.addAll(parents.get(1).subList(dividerIndex, graph.sizeVertex()));

        return child;
    }

    private List<List<Integer>> mutation(List<List<Integer>> children) {
        int gen, genNumber;
        for (List<Integer> child : children) {
            for (int i = 0; i < mutationGenCount; i++) {
                if (getRandomBooleanForMutation()) {
                    genNumber = r.nextInt(graph.sizeVertex());
                    gen = child.get(genNumber);
                    child.set(genNumber, gen == 0 ? 1 : 0);
                }
            }
        }

        return children;
    }

    private int F(List<Integer> solution, Graph graph) {
        covered = graph.covered(solution);
        return covered + ALPHA * (graph.sizeEdges() - covered) + graph.involvedVertices;
    }

    private int findDifferentSolutions() {
        return new HashSet<List<Integer>>(bestCandidatesList).size();
    }
}