package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

// This model is pretty simple, it simply uses the entire document for R.
public class InclusiveModel extends ProbabalisticIRModel {
	private List<TextDataLine> documents;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		this.documents = dataSet.getAllTextLines();
	}

	@Override
	public double runQuery(List<String> query) {
		return super.runProbablisticR(this.documents);
	}
}
