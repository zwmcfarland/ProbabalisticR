package com.zwmcfarland.java.probabalisticR.models;

import java.util.*;

import com.zwmcfarland.java.probabalisticR.Application;
import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.soap.Text;

public class VectorModel extends ProbabalisticIRModel {
	private static final Logger LOG = LogManager.getLogger(VectorModel.class.getName());
	List<TextDataLine> documents;
	List<Map<String, Double>> documentVectors;
	List<String> allKeywords;
	Map<String, Double> idfMap;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		LOG.trace("Building index for vector model");
		this.documents = dataSet.getAllTextLines();
		this.allKeywords = new ArrayList<String>();
		this.documentVectors = new ArrayList<Map<String, Double>>();
		this.idfMap = new HashMap<String, Double>();

		for(TextDataLine line : this.documents) {
			for(String keyword : line.getWordList()) {
				this.allKeywords.add(keyword);
			}
		}

		LOG.trace("Building IDF map");
		allKeywords.stream().forEach(term -> {
			this.idfMap.put(term, this.idf(this.documents, term));
		});

		LOG.trace("Building document vector index on " + this.allKeywords.size() + " keywords");
		int documentCount = this.documents.size();
		for(int i = 0; i < this.documents.size(); i ++) {
			LOG.trace("Calculating vector index for document " + (i + 1) + " of " + documentCount);
			this.documentVectors.add(this.tfIdfCalculator(this.documents.get(i)));
		}
	}

	@Override
	public void runQuery(List<String> query) {
		LOG.info("Running vector model query");
		TextDataLine queryLine = new TextDataLine();
		for(String queryTerm: query) {
			queryLine.addWord(queryTerm);
		}

		Map<String, Double> queryVector = this.tfIdfCalculator(queryLine);
		List<Integer> documents = new ArrayList<Integer>();
		List<TextDataLine> R = new ArrayList<TextDataLine>();

		for(int i = 0; i < this.documents.size(); i++) {
			double distance = this.cosine_similarity(queryVector, this.documentVectors.get(i));
			if(distance > 0.0d) {
				R.add(this.documents.get(i));
			}
		}
		LOG.debug("Vector space identified " + R.size() + " relevant documents");
		super.runProbablisticR(this.documents, R, query);
	}

	public double cosine_similarity(Map<String, Double> v1, Map<String, Double> v2) {
		Set<String> both = new HashSet<String>(v1.keySet());
		both.retainAll(v2.keySet());
		double sclar = 0d, norm1 = 0d, norm2 = 0d;
		for (String k : both) sclar += v1.get(k) * v2.get(k);
		for (String k : v1.keySet()) norm1 += v1.get(k) * v1.get(k);
		for (String k : v2.keySet()) norm2 += v2.get(k) * v2.get(k);
		return sclar / Math.sqrt(norm1 * norm2);
	}

	public double tf(TextDataLine doc, String term) {
		return doc.getWordCount(term) / doc.getTotalWords();
	}

	/**
	 * Method to create termVector according to its tfidf score.
	 */
	public Map<String, Double> tfIdfCalculator(TextDataLine document) {

		Map<String, Double> tfidfvector = new HashMap<String, Double>();
		this.allKeywords.stream().forEach(term -> {
			if(document.containsWord(term)) {
				double tf = this.tf(document, term);
				double tfidf = tf * this.idfMap.get(term);
				tfidfvector.put(term, tfidf);
			}
		});
		return tfidfvector;
	}

	public double idf(List<TextDataLine> docs, String term) {
		double n = 0;
		for (TextDataLine doc : docs) {
			n += doc.getWordCount(term);
		}
		return Math.log(docs.size() / n);
	}

	public double tfIdf(TextDataLine doc, List<TextDataLine> docs, String term) {
		return tf(doc, term) * idf(docs, term);
	}

	public void clearData() {
		this.allKeywords = null;
		this.documents = null;
		this.documentVectors = null;
		this.idfMap = null;
	}
}
