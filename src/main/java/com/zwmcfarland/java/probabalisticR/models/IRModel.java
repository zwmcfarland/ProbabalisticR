package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;

public interface IRModel {
	// Initializes any required data structures to run queries against.
	public void initializeDataStructures(DataSet s);
	// Takes the list of query terms, and retuns the score for this query
	public double runQuery(List<String> query);
}
