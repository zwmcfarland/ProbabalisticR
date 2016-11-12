package com.zwmcfarland.java.probabalisticR;

import java.util.Map;

public class Printinator {

	public static void printinate(Map<String, Map<String, Double>> results) {
		results.entrySet().stream().forEach(model -> {
			System.out.println("Results for Model " + model.getKey());
			model.getValue().entrySet().stream().forEach(modelResults -> {
				System.out.println("\tQuery " + modelResults.getKey() + " result " + modelResults.getValue());
			});
		});
	}

}
