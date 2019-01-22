/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics.statistics;

/**
 *
 * @author anei
 */
public class StatData {
    
    private final int numCrates;
    private double solvingTime;
    private long solutionLength;
    private long statesExamined;
    private long statesAlreadySeen;
    private long statesInFringe;
    private long deadlocksFound;
    
    
    
    public StatData(int numCrates) {
        this.numCrates = numCrates;
        this.solvingTime = 0;
        this.solutionLength = 0;
        this.statesExamined = 0;
        this.statesAlreadySeen = 0;
        this.statesInFringe = 0;
        this.deadlocksFound = 0;
    }

    public int getNumCrates() {
        return numCrates;
    }
    
    public double getSolvingTime() {
        return solvingTime;
    }

    public void setSolvingTime(double solvingTime) {
        this.solvingTime = solvingTime;
    }

    public long getSolutionLength() {
        return solutionLength;
    }

    public void setSolutionLength(long solutionLength) {
        this.solutionLength = solutionLength;
    }

    public long getStatesExamined() {
        return statesExamined;
    }

    public void setStatesExamined(long statesExamined) {
        this.statesExamined = statesExamined;
    }

    public long getStatesAlreadySeen() {
        return statesAlreadySeen;
    }

    public void setStatesAlreadySeen(long statesAlreadySeen) {
        this.statesAlreadySeen = statesAlreadySeen;
    }

    public long getStatesInFringe() {
        return statesInFringe;
    }

    public void setStatesInFringe(long statesInFringe) {
        this.statesInFringe = statesInFringe;
    }

    public long getDeadlocksFound() {
        return deadlocksFound;
    }

    public void setDeadlocksFound(long deadlocksFound) {
        this.deadlocksFound = deadlocksFound;
    }
    
    
    
    /**
     * Merges two pieces of solver data - calculates averages.
     * @param data another piece of solver data from same problem
     */
    public void merge(StatData data) {
        solvingTime += data.getSolvingTime();
        solvingTime /= 2;
        
        solutionLength += data.getSolutionLength();
        solutionLength /= 2;
        
        statesExamined += data.getStatesExamined();
        statesExamined /= 2;
        
        statesAlreadySeen += data.getStatesAlreadySeen();
        statesAlreadySeen /= 2;
        
        statesInFringe += data.getStatesInFringe();
        statesInFringe /= 2;
        
        deadlocksFound += data.getDeadlocksFound();
        deadlocksFound /= 2;
    }
    
    
    /**
     * Copies stats from a piece of solver data.
     * @param data another piece of solver data to be copied
     */
    public void copy(StatData data) {
        solvingTime = data.getSolvingTime();
        solutionLength = data.getSolutionLength();
        statesExamined = data.getStatesExamined();
        statesAlreadySeen = data.getStatesAlreadySeen();
        statesInFringe = data.getStatesInFringe();
        deadlocksFound = data.getDeadlocksFound();
    }
    
    
    public void parseData(String[] data) {
        solvingTime = Double.parseDouble(data[2]);
        solutionLength = Long.parseLong(data[3]);
        statesExamined = Long.parseLong(data[4]);
        statesAlreadySeen = Long.parseLong(data[5]);
        statesInFringe = Long.parseLong(data[6]);
        deadlocksFound = Long.parseLong(data[7]);
    }

    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.numCrates;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StatData other = (StatData) obj;
        return this.numCrates == other.numCrates;
    }
    
    
}
