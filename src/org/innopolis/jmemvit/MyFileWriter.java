package org.innopolis.jmemvit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

/**
 * The MyFileWriter class helps to write data in the file
 */
public class MyFileWriter {
	
	private static File filename = new File ("/home/yuliya/Thesis/Eclipse Project/JMemvit/src/org/innopolis/jmemvit/output.txt");
	
	/**
	 * The constructor
	 */
	public MyFileWriter(String filePath) {
		filename = new File(filePath);
	}
	
	/*
	 * Method writes string to file
	 */
	public static void write(String stringToWrite){
		try {
		    FileWriter fWriter = new FileWriter (filename, false);
		    PrintWriter pWriter = new PrintWriter (fWriter);
		    pWriter.println (stringToWrite);		   
		    pWriter.close();
		} catch (IOException e) {			
			JOptionPane.showMessageDialog(null, "Error with writing to file!");
			e.printStackTrace();
		}
	}
	
}
