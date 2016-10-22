package com.zwmcfarland.java.probabalisticR.dto;

import javax.xml.crypto.Data;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zac on 9/10/2016.
 */
public class Command {
	private int commandNumber;
	private String newsGroup;
	private List<String> keywords;

	public Command(String[] commandLine) {
		this.keywords = new ArrayList<String>();
		this.commandNumber = Integer.parseInt(commandLine[0].replaceAll("\\s",""));
		this.newsGroup = commandLine[1].replaceAll("\\s","");
		this.keywords = Arrays.asList(commandLine[2].substring(1).split(" "));
	}

	public void runOn(DataSet ds) {
		printCommand();
		switch(this.commandNumber) {
			case 1: // Entropy
				calculateEntropy(this.newsGroup, this.keywords, ds);
				break;
			case 2: // Conditional entropy
				calculateConditionalEntropy(this.newsGroup, this.keywords, ds);
				break;
			case 3: // mutual information
				calculateMutualInformation(this.newsGroup, this.keywords, ds);
				break;
			case 4: // KL Distance
				klDivergence(this.newsGroup, this.keywords, ds);
				break;
			default:
				System.err.println(" Invalid command found! Cannot find command number " + commandNumber);
		}
	}

	private void printCommand() {
		System.out.println("\nCommand :" + this.commandNumber + "," + this.newsGroup + "," + this.keywords.toString());
	}

	// This function computes the entropy of the given list of keywords in each of the news groups.
	private void calculateEntropy(String newsGroup, List<String> keywords, DataSet ds) {
		double entropyAccumulator = 0;
		if(newsGroup.toUpperCase().equals("ALL")) {
			for(String newsGroupKey: ds.getNewsGroups()) {
				double totalLines = ds.getTotalLinesForNewsGroup(newsGroupKey);
				entropyAccumulator = 0;
				for(String keyword: keywords) {
					double linesContaining = ds.linesContaingWord(newsGroupKey, keyword);
					double probability = linesContaining / totalLines;
					if(probability != 0) {
						double entropy = probability * Math.log(probability);
						entropyAccumulator += entropy;
					}
				}
				System.out.println("Entropy for newsGroup " + newsGroupKey + " = " + -1 * entropyAccumulator);
			}
		}
		else {
			double totalLines = ds.getTotalLinesForNewsGroup(newsGroup);
			for(String keyword: keywords) {
				double linesContaining = ds.linesContaingWord(newsGroup, keyword);
				double probability = linesContaining / totalLines;
				if(probability != 0) {
					double entropy = probability * Math.log(probability);
					entropyAccumulator += entropy;
				}
			}
			System.out.println("Entropy for newsGroup" + newsGroup + " = " + -1 * entropyAccumulator);
		}
	}
	
	private double calculateConditionalEntropy(String newsGroup, List<String> keywords, DataSet ds) {
		double condEntropyAccumulator = 0;
		String a = keywords.get(0);
		if(newsGroup.toUpperCase().equals("ALL")) {
			for (String newsGroupKey : ds.getNewsGroups()) {
				condEntropyAccumulator = 0;
				double totalLines = ds.getTotalLinesForNewsGroup(newsGroupKey);
				for(String keyword: keywords.subList(1, keywords.size())) { //Skip X, only loop over words in Y
					double linesContainingAB = ds.linesContainingAB(newsGroupKey, a, keyword);
					double linesContainingB = ds.linesContaingWord(newsGroupKey, keyword);
					if(linesContainingAB != 0 && linesContainingB != 0) {
						double entropyAgivenB = linesContainingAB / linesContainingB;
						double probBA = linesContainingAB / totalLines;
						condEntropyAccumulator += probBA * Math.log(entropyAgivenB);
					}
				}
				System.out.println("Conditional Entropy for newsGroup" + newsGroupKey + " = " + -1 * condEntropyAccumulator);
			}
		}
		else {
			double totalLines = ds.getTotalLinesForNewsGroup(newsGroup);
			for (String keyword : keywords.subList(1, keywords.size())) { //Skip X, only loop over words in Y
				double linesContainingAB = ds.linesContainingAB(newsGroup, a, keyword);
				double linesContainingB = ds.linesContaingWord(newsGroup, keyword);
				double entropyAgivenB = linesContainingAB / linesContainingB;
				if(linesContainingAB > 0 && linesContainingB != 0) {
					double probBA = linesContainingAB / totalLines;
					condEntropyAccumulator += probBA * Math.log(entropyAgivenB);
				}
			}
			System.out.println("Conditional Entropy for newsGroup" + newsGroup + " = " + -1 * condEntropyAccumulator);
		}
		return -1 * condEntropyAccumulator;
	}

	private void klDivergence(String newsGroup, List<String> keywords, DataSet ds) {
		double kldivergence = 0;
		Map<String, Double> probabilityDistributionForX = new HashMap<String, Double>();
		double totalLinesContainingX = ds.linesContaingWord(newsGroup, keywords.get(0)); // News group will be ALL here so it will be total lines. 
		for(String newsGroupKey : ds.getNewsGroups()) {
			probabilityDistributionForX.put(newsGroupKey, (Double)(ds.linesContaingWord(newsGroupKey, keywords.get(0))/ totalLinesContainingX));
		}

		Map<String, Double> probabilityDistributionForY = new HashMap<String, Double>();
		double totalLinesContainingY = ds.linesContaingWord(newsGroup, keywords.get(1)); // News group will be ALL here so it will be total lines. 
		for(String newsGroupKey : ds.getNewsGroups()) {
			probabilityDistributionForY.put(newsGroupKey, (Double)(ds.linesContaingWord(newsGroupKey, keywords.get(1))/ totalLinesContainingY));
		}

		for(String newsGroupKey: probabilityDistributionForX.keySet()) {
			if(probabilityDistributionForY.get(newsGroupKey) != 0 && probabilityDistributionForX.get(newsGroupKey) != 0) {
				System.out.println("KL Divergence for news group " + newsGroupKey + " = " + probabilityDistributionForX.get(newsGroupKey) * Math.log(probabilityDistributionForX.get(newsGroupKey)/probabilityDistributionForY.get(newsGroupKey)));
			} else {
				System.out.println("KL Divergence for news group is not solveable (0% probability)");
			}
		}
	}

	// This function computes the mutual information of given keywords in all news groups.
	private void calculateMutualInformation(String newsGroup, List<String> keywords, DataSet ds) {
		if(newsGroup.toUpperCase().equals("ALL")) {
			for (String newsGroupKey : ds.getNewsGroups()) {
				double mutualInfoAccumulator = 0;
				for(String keyword: keywords) {
					double linesContaining = ds.linesContaingWord(newsGroupKey, keyword);
					double totalLines = ds.getTotalLinesForNewsGroup(newsGroupKey);
					double probability = linesContaining / totalLines;
					if(probability != 0) {
						double entropy = probability * Math.log(probability);
						mutualInfoAccumulator += entropy;
					}
				}
				System.out.println("Mutual information for news group " + newsGroupKey + " = " + -1 * mutualInfoAccumulator);
			}
		} else {
			double mutualInfoAccumulator = 0;
			for(String keyword: keywords) {
				double linesContaining = ds.linesContaingWord(newsGroup, keyword);
				double totalLines = ds.getTotalLinesForNewsGroup(newsGroup);
				double probability = linesContaining / totalLines;
				double entropy = probability * Math.log(probability);
				mutualInfoAccumulator += entropy;
			}
			System.out.println("Mutual information for news group " + newsGroup + " = " + -1 * mutualInfoAccumulator);
		}
	}
}
