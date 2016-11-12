package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

public abstract class ProbabalisticIRModel {
	// Initializes any required data structures to run queries against.
	public abstract void initializeDataStructures(DataSet s);

	// Takes the list of query terms, and retuns the score for this query
	public abstract double runQuery(List<String> query);
	
	//Implementing classes will simply provide R for this method.
	protected double runProbablisticR(List<TextDataLine> R) {
		return 0d;
	}
}
