package com.zwmcfarland.java.probabalisticR.dto;
import javax.xml.soap.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Zac on 9/10/2016.
 */
public class DataSet {
	private Map<String, List<TextDataLine>> newsGroups;

	public DataSet() {
		this.newsGroups = new HashMap<String, List<TextDataLine>>();
	}

	public Set<String> getNewsGroups() {
		return this.newsGroups.keySet();
	}

	public List<TextDataLine> getLinesInNewsGroup(String newsGroup) {
		return this.newsGroups.get(newsGroup);
	}

	public void addTextDataLine(TextDataLine line) {
		if(!this.newsGroups.containsKey(line.getNewsGroup())) {
			// If this is the first time the news group shows up, then we have to instantiate the list of lines.
			this.newsGroups.put(line.getNewsGroup(), new ArrayList<TextDataLine>());
		}
		this.newsGroups.get(line.getNewsGroup()).add(line);
	}
	
	public int linesContaingWord(String newsGroup, String word) {
		int total = 0;
		if(newsGroup.toUpperCase().equals("ALL")) {
			for(String newsGroupKey: this.newsGroups.keySet()) {
				total += countLinesInNewsGroup(word, this.newsGroups.get(newsGroupKey));
			}
		}
		else {
			total += countLinesInNewsGroup(word, this.newsGroups.get(newsGroup));
		}
		return total;
	}
	
	public int linesContainingAB(String newsGroup, String a, String b) {
		int total = 0;
		if(newsGroup.toUpperCase().equals("ALL")) {
			for(String newsGroupKey: this.newsGroups.keySet()) {
				for(TextDataLine line : this.newsGroups.get(newsGroupKey)) {
					if(line.containsWord(a) && line.containsWord(b)) {
						total += 1;
					}
				}
			}
		}
		else {
			for(TextDataLine line : this.newsGroups.get(newsGroup)) {
				if(line.containsWord(a) && line.containsWord(b)) {
					total += 1;
				}
			}
		}
		return total;
	}

	private int countLinesInNewsGroup(String word, List<TextDataLine> lines) {
		int total = 0;
		for(TextDataLine line : lines) {
			if(line.containsWord(word)) {
				total += 1;
			}
		}
		return total;
	}

	public int getTotalLinesForNewsGroup(String newsGroup) {
		int total = 0;
		if(newsGroup.toUpperCase().equals("ALL")) {
			for(String newsGroupKey: this.newsGroups.keySet()) {
				total += this.newsGroups.get(newsGroupKey).size();
			}
		}
		else {
			total = this.newsGroups.get(newsGroup).size();
		}
		return total;
	}
}