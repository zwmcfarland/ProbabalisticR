package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

public class BooleanModel extends ProbabalisticIRModel {
	private static final Logger LOG = LogManager.getLogger(BooleanModel.class.getName());
	private List<TextDataLine> documents;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		this.documents = dataSet.getAllTextLines();
	}

	@Override
	public void runQuery(List<String> query) {
		LOG.debug("Starting boolean model");
		List<TextDataLine> R = this.documents.stream().filter(document -> document.containsAnyWord(query)).collect(Collectors.toList());
		super.runProbablisticR(this.documents, R, query);
	}

	public void clearData() {
		this.documents = null;
	}
}
