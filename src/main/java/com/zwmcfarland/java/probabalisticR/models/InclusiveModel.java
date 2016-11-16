package com.zwmcfarland.java.probabalisticR.models;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

// This model is pretty simple, it simply uses the entire document for R.
public class InclusiveModel extends ProbabalisticIRModel {
	private static final Logger LOG = LogManager.getLogger(InclusiveModel.class.getName());
	private List<TextDataLine> documents;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		this.documents = dataSet.getAllTextLines();
	}

	@Override
	public void runQuery(List<String> query) {
		LOG.debug("Starting inclusive model");
		super.runProbablisticR(this.documents, this.documents, query);
	}

	public void clearData() {
		this.documents = null;
	}
}
