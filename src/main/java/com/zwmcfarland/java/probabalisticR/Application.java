package com.zwmcfarland.java.probabalisticR;

import javax.xml.soap.Text;

import com.zwmcfarland.java.probabalisticR.dto.Command;
import com.zwmcfarland.java.probabalisticR.dto.DataSet;
import com.zwmcfarland.java.probabalisticR.dto.TextDataLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Application {
    public static void main(String[] args) throws Exception {
		/*
		 * Here we assume that args[0] is the preprocessed data file with the stemmed words
		 * and args[1] is the input.txt file that contains the commands to be processed.
		 */
        File dataFile;
        File commandFile;
        if(args.length < 1) { // We don't have either the pre-processed file or the commands file
            System.err.println("Invalid arguments, to run use the form java TextProcessor dataFile commandFile.");
            System.err.println("Note: commandFile argument is optional.");
            return;
        } else if(args.length == 1) {
            System.out.println("No command file specified, assuming input.txt in current working directory.");
            commandFile = getValidFile("input.txt");
        } else {
            commandFile = getValidFile(args[1]);
        }
        dataFile = getValidFile(args[0]);

        List<Command> commands = getCommands(commandFile);
        DataSet dataSet = getDataSet(dataFile);
        for(Command command: commands) {
            command.runOn(dataSet);
        }
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

    private static List<Command> getCommands(File commandFile) {
        List<Command> commands = new ArrayList<Command>();
        try (BufferedReader br = new BufferedReader(new FileReader(commandFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                commands.add(new Command(line.split(",")));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return commands;
    }
}
