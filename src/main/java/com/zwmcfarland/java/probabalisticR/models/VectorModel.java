package com.zwmcfarland.java.probabalisticR.models;

import java.util.*;

import com.zwmcfarland.java.probabalisticR.Application;
import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.soap.Text;

public class VectorModel extends ProbabalisticIRModel {
	private static final Logger LOG = LogManager.getLogger(ProbabalisticIRModel.class.getName());
	List<TextDataLine> documents;
	List<Map<String, Double>> documentVectors;
	List<String> allKeywords;

	@Override
	public void initializeDataStructures(DataSet dataSet) {
		LOG.trace("Building index for vector model");
		this.documents = dataSet.getAllTextLines();
		this.allKeywords = new ArrayList<String>();
		this.documentVectors = new ArrayList<Map<String, Double>>();
		for(TextDataLine line : this.documents) {
			for(String keyword : line.getWordList()) {
				this.allKeywords.add(keyword);
			}
		}
		LOG.trace("Building document vector index on " + this.allKeywords.size() + " keywords");
		int documentCount = this.documents.size();
		for(int i = 0; i < this.documents.size(); i ++) {
			LOG.trace("Calculating vector index for document " + (i + 1) + " of " + documentCount);
			this.documentVectors.add(this.tfIdfCalculator(this.documents.get(i), this.allKeywords));
		}
	}

	@Override
	public double runQuery(List<String> query) {
		LOG.info("Running vector model query");
		TextDataLine queryLine = new TextDataLine();
		for(String queryTerm: query) {
			queryLine.addWord(queryTerm);
		}

		Map<String, Double> queryVector = this.tfIdfCalculator(queryLine, this.allKeywords);
		List<Integer> documents = new ArrayList<Integer>();
		List<TextDataLine> R = new ArrayList<TextDataLine>();

		for(int i = 0; i < this.documents.size(); i++) {
			double distance = this.cosine_similarity(queryVector, this.documentVectors.get(i));
			System.out.println("Distance Found " + distance);
			if(distance >= 0.5d) {
				R.add(this.documents.get(i));
			}
		}
		return super.runProbablisticR(R);
	}

	public double cosine_similarity(Map<String, Double> v1, Map<String, Double> v2) {
		Set<String> both = new HashSet<String>(v1.keySet());
		both.retainAll(v2.keySet());
		double sclar = 0, norm1 = 0, norm2 = 0;
		for (String k : both) sclar += v1.get(k) * v2.get(k);
		for (String k : v1.keySet()) norm1 += v1.get(k) * v1.get(k);
		for (String k : v2.keySet()) norm2 += v2.get(k) * v2.get(k);
		return sclar / Math.sqrt(norm1 * norm2);
	}

	public double tf(TextDataLine doc, String term) {
		return doc.getWordCount(term) / doc.getWordList().size();
	}

	/**
	 * Method to create termVector according to its tfidf score.
	 */
	public Map<String, Double> tfIdfCalculator(TextDataLine document, List<String> allTerms) {
		LOG.debug("Calculating document vector");


		Map<String, Double> tfidfvector = new HashMap<String, Double>();
		allTerms.parallelStream().forEach(term -> {
			double tf = this.tf(document, term);
			double idf = this.idf(this.documents, term);
			double tfidf = tf * idf;
			tfidfvector.put(term, tfidf);
		});
		return tfidfvector;
	}

	public double idf(List<TextDataLine> docs, String term) {
		double n = 0;
		for (TextDataLine doc : docs) {
			for (String word : doc.getWordList()) {
				if (term.equalsIgnoreCase(word)) {
					n++;
					break;
				}
			}
		}
		return Math.log(docs.size() / n);
	}

	public double tfIdf(TextDataLine doc, List<TextDataLine> docs, String term) {
		return tf(doc, term) * idf(docs, term);
	}
}
