package com.zwmcfarland.java.probabalisticR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    private static final Logger LOG = LogManager.getLogger(Application.class.getName());

    public static void main(String[] args) throws Exception {
		/*
		 * Here we assume that args[0] is the preprocessed data file with the stemmed words
		 */
        LOG.debug("Starting Comparinator");
        File dataFile;
        if(args.length < 1) { // We don't have either the pre-processed file or the commands file
            System.err.println("Invalid arguments, to run use the form java TextProcessor dataFile.");
            return;
        }
        dataFile = getValidFile(args[0]);
        DataSet dataSet = getDataSet(dataFile);
        TheComparinator comparinator = new TheComparinator();
        LOG.trace("Sending data to the comparinator");
        comparinator.setData(dataSet);
		LOG.trace("Time for comparinating!");
        Map<String, Map<String, Double>> results = comparinator.comparinate();
		LOG.trace("Sending comparination results to the printinator");
        Printinator.printinate(results);
    }

    private static DataSet getDataSet(File dataFile) {
        DataSet ds = new DataSet();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                TextDataLine dataLine = new TextDataLine(line);
                ds.addTextDataLine(dataLine);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return ds;
    }


    private static File getValidFile(String fileString) throws Exception {
        File dataFile = new File(fileString);
        if(!dataFile.exists()) {
            throw new Exception("Failed to get the file " + dataFile.getPath() + " please provide a valid file.");
        }
        return dataFile;
    }
}
