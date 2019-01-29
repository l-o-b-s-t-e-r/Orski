package com.company;

import java.util.List;

public class Result {
    private double time = 0;
    private double iterationsNumber = 0;
    private double bestF = 0;
    private double bestSumF = 0;
    private double differentSolutionsNumber = 0;

    public Result() {

    }

    public Result(double time, int iterationsNumber, int bestF, int bestSumF, int differentSolutionsNumber) {
        this.time = time;
        this.iterationsNumber = iterationsNumber;
        this.bestF = bestF;
        this.bestSumF = bestSumF;
        this.differentSolutionsNumber = differentSolutionsNumber;
    }

    public void addResult(Result result) {
        time += result.getTime();
        iterationsNumber += result.getIterationsNumber();
        bestF += result.getBestF();
        bestSumF += result.getBestSumF();
        differentSolutionsNumber += result.getDifferentSolutionsNumber();
    }

    public void divideResult(int n) {
        time /= n;
        iterationsNumber /= n;
        bestF /= n;
        bestSumF /= n;
        differentSolutionsNumber /= n;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setIterationsNumber(int iterationsNumber) {
        this.iterationsNumber = iterationsNumber;
    }

    public void setBestF(int bestF) {
        this.bestF = bestF;
    }

    public void setBestSumF(int bestSumF) {
        this.bestSumF = bestSumF;
    }

    public double getIterationsNumber() {
        return iterationsNumber;
    }

    public void setIterationsNumber(double iterationsNumber) {
        this.iterationsNumber = iterationsNumber;
    }

    public double getBestF() {
        return bestF;
    }

    public void setBestF(double bestF) {
        this.bestF = bestF;
    }

    public double getBestSumF() {
        return bestSumF;
    }

    public void setBestSumF(double bestSumF) {
        this.bestSumF = bestSumF;
    }

    public double getDifferentSolutionsNumber() {
        return differentSolutionsNumber;
    }

    public void setDifferentSolutionsNumber(double differentSolutionsNumber) {
        this.differentSolutionsNumber = differentSolutionsNumber;
    }

    public void setDifferentSolutionsNumber(int differentSolutionsNumber) {
        this.differentSolutionsNumber = differentSolutionsNumber;
    }

    public static Result calculateAVG(List<Result> results) {
        Result avgResult = new Result();
        for (Result result : results) {
            avgResult.addResult(result);
        }

        avgResult.divideResult(results.size());
        return avgResult;
    }

    @Override
    public String toString() {
        return "Result{" +
                "time=" + time +
                ", iterationsNumber=" + iterationsNumber +
                ", bestF=" + bestF +
                ", bestSumF=" + bestSumF +
                ", differentSolutionsNumber=" + differentSolutionsNumber +
                '}';
    }
}
