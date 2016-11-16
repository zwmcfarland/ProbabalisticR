package com.zwmcfarland.java.probabalisticR.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

public abstract class ProbabalisticIRModel {
	private static final Logger LOG = LogManager.getLogger(ProbabalisticIRModel.class.getName());
	private Map<String, Double> termWeights;

	// Initializes any required data structures to run queries against.
	public abstract void initializeDataStructures(DataSet s);

	// Takes the list of query terms, and retuns the score for this query
	public abstract void runQuery(List<String> query);

	public abstract void clearData();

	//Implementing classes will simply provide R for this method.
	protected void runProbablisticR(List<TextDataLine> allDocuments, List<TextDataLine> R, List<String> query) {
		LOG.debug("Starting the probabalistic model with " + R.size() + " Documents");
		Map<TextDataLine, Double> documentScore = new HashMap<TextDataLine, Double>();
		formatData(allDocuments);
		for(TextDataLine document: R) {
			for(String queryTerm: query) {
				if(document.containsWord(queryTerm)) {
					if(documentScore.containsKey(document)) {
						documentScore.put(document, Math.abs(documentScore.get(document) * this.termWeights.get(queryTerm)));
					} else {
						documentScore.put(document, Math.abs(this.termWeights.get(queryTerm)));
					}
				}
			}
		}
		
		TextDataLine max = null;
		for(Entry<TextDataLine, Double> entry : documentScore.entrySet()) {
			LOG.debug("Found relevant documment " + entry.getValue());
			if(max == null ||  entry.getValue() > documentScore.get(max)) {
				max = entry.getKey();
			}
		}
		LOG.debug("Maximum Scored document was" + max.getNewsGroup() + " " + max.getWordList());
	}

	private void formatData(List<TextDataLine> R) {
		this.termWeights = new HashMap<String, Double>();
		Map<String, Double> occurences = new HashMap<String, Double>();
		for(TextDataLine document : R) {
			for(String word : document.getWordList()) {
				if(occurences.containsKey(word)) {
					occurences.put(word, occurences.get(word) + 1);
				} else {
					occurences.put(word, 1d);
				}
			}
		}
		
		for(String key: occurences.keySet()) {
			this.termWeights.put(key, this.score(R.size(), occurences.get(key)));
		}
	}
	
	private double score(double numDocuments, double termOccurences) {
		return (numDocuments - termOccurences  + 0.5d) / (termOccurences + 0.5d); 
	}
}
