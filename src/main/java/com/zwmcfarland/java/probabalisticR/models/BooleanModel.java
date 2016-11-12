package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;
import java.util.stream.Collectors;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

public class BooleanModel extends ProbabalisticIRModel {
	private List<TextDataLine> documents;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		this.documents = dataSet.getAllTextLines();
	}

	@Override
	public double runQuery(List<String> query) {
		List<TextDataLine> R = this.documents.stream().filter(document -> document.containsAnyWord(query)).collect(Collectors.toList());
		return super.runProbablisticR(R);
	}
}
