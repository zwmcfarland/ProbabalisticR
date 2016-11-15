package com.zwmcfarland.java.probabalisticR.dto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zac on 9/10/2016.
 */
public class TextDataLine {
	/*
	 * We will fill this data structure with an entry for
	 * every word, and increment the integer every time we see it again.
	 */
	private Map<String, Integer> wordCountAgrigater;
	private List<String> wordList;

	private String newsGroup;

	public TextDataLine() {
		this.wordCountAgrigater = new HashMap<String, Integer>();
		this.wordList = new ArrayList<String>();
	}

	public TextDataLine(String line) {
		this.wordCountAgrigater = new HashMap<String, Integer>();
		this.wordList = new ArrayList<String>();
		String[] lineToken = line.split("\\t"); // Newsgroup is split apart by tab.
		this.newsGroup = lineToken[0];
		for(String word: lineToken[1].split(" ")) {
			this.addWord(word);
		}
	}

	public Integer getWordCount(String term) {
		Integer count = this.wordCountAgrigater.get(term);
		if(count != null) {

		}
		return count != null ? count : 0;
	}

	public int getTotalWords() {
		int total = 0;
		for(String word: this.wordCountAgrigater.keySet()) {
			total += this.wordCountAgrigater.get(word).intValue();
		}
		return total;
	}

	public void addWord(String word) {
		this.wordCountAgrigater.put(
				word,
				this.wordCountAgrigater.containsKey(word) ?
						this.wordCountAgrigater.get(word) + 1 :
						1
		);
		this.wordList.add(word);
	}

	public void setNewsGroup(String newsGroup) {
		this.newsGroup = newsGroup;
	}

	public String getNewsGroup() {
		return this.newsGroup;
	}

	public List<String> getWordList() {
		return this.wordList;
	}

	public boolean containsWord(String word) {
		return this.wordCountAgrigater.containsKey(word);
	}

	public boolean containsAnyWord(List<String> query) {
		return query.stream().anyMatch(term -> this.containsWord(term));
	}

	public void destroyKeywordList() {
		this.wordList = null; // We can use this to free up memory
	}
}
