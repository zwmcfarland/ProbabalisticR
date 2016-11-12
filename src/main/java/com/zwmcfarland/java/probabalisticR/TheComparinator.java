package com.zwmcfarland.java.probabalisticR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.models.BooleanModel;
import com.zwmcfarland.java.probabalisticR.models.ProbabalisticIRModel;
import com.zwmcfarland.java.probabalisticR.models.InclusiveModel;
import com.zwmcfarland.java.probabalisticR.models.VectorModel;

public class TheComparinator {
	Map<String, ProbabalisticIRModel> models;
	Map<String, List<String>> queries;
	
	public TheComparinator() {
		this.models = new HashMap<String, ProbabalisticIRModel>();
		this.models.put("BooleanModel", new BooleanModel());
		this.models.put("InclusiveModel", new InclusiveModel());
		this.models.put("VectorModel", new VectorModel());
		this.queries = new HashMap<String, List<String>>();
		List<String> query1 = Arrays.asList("religion", "god", "false", "prophet");
		List<String> query2 = Arrays.asList("mpeg", "player", "decode");
		this.queries.put("Query1", query1);
		this.queries.put("Query1", query2);
	}

	public void setData(DataSet dataset) {
		this.models.entrySet().stream().forEach(a -> a.getValue().initializeDataStructures(dataset));
	}

	public Map<String, Map<String, Double>> comparinate() {
		Map<String, Map<String, Double>> results = new HashMap<String, Map<String, Double>>();
		this.models.entrySet().stream().forEach(model -> {
			Map<String, Double> result = new HashMap<String, Double>();
			this.queries.entrySet().stream().forEach(query -> {
				result.put(query.getKey(), model.getValue().runQuery(query.getValue()));
			});
			results.put(model.getKey(), result);
		});
		return results;
	}
}
