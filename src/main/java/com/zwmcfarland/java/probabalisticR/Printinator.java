package com.zwmcfarland.java.probabalisticR;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class Printinator {
	private static final Logger LOG = LogManager.getLogger(Printinator.class.getName());

	public static void printinate(Map<String, Map<String, Double>> results) {
		results.entrySet().stream().forEach(model -> {
			LOG.info("Results for Model " + model.getKey());
			model.getValue().entrySet().stream().forEach(modelResults -> {
				LOG.info("\tQuery " + modelResults.getKey() + " result " + modelResults.getValue());
			});
		});
	}
}
