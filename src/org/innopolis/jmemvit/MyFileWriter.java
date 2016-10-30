package org.innopolis.jmemvit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class MyFileWriter {
	File filename;
	public MyFileWriter(String filePath) {
		filename = new File(filePath);
	}
	
	public void write(String stringToWrite){
		try {
		    FileWriter fWriter = new FileWriter (filename, true);
		    PrintWriter pWriter = new PrintWriter (fWriter);
		    pWriter.println (stringToWrite);		   
		    pWriter.close();
		} catch (IOException e) {			
			JOptionPane.showMessageDialog(null, "Error with writing to file!");
			e.printStackTrace();
		}
	}
	
}
