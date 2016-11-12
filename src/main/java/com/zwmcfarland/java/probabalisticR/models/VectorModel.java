package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

public class VectorModel extends ProbabalisticIRModel {
	List<TextDataLine> documents;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		this.documents = dataSet.getAllTextLines();
	}

	@Override
	public double runQuery(List<String> query) {
		
		super.runProbablisticR(R);
	}
}
