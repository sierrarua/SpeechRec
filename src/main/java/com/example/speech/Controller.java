package com.example.speech;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Controller extends JPanel implements ActionListener{

	private JTextArea display; 
	private JTextArea lineNum;
	//processing unit
	public Process process;
	String [] currentFile;
	String latest;
	RecordThread recorder;
	JButton enter;
	JButton proceed;
	JPanel buttons;


	/**
	 * Constructor
	 */
	public Controller(){
		process = new Process();
		currentFile = new String[0];
		recorder = new RecordThread();
		panel();
	}


	private void process() {
		boolean notDone = true;

	}


	/**
	 * Set up the visualization
	 */
	public void panel(){
		this.setLayout(new FlowLayout());
		lineNum = new JTextArea(40, 2);
		add(lineNum);
		lineNum.setEditable(false);
		
		buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,1));
		
		display = new JTextArea(40, 40);
		add(display);
		display.setLineWrap(true);
		display.setEditable(true);
		display.setTabSize(2);
		display.setMargin(new Insets(0, 20, 0, 0));
		
		
		enter = new JButton("rec ●");
		enter.setActionCommand("enter");
		enter.addActionListener(this);
		buttons.add(enter);
		lineNum();
		
		proceed = new JButton("proceed >>");
		proceed.setActionCommand("proceed");
		proceed.addActionListener(this);
		buttons.add(proceed);
		add(buttons);

	}
	private void saveFile(){
		String content = toStr();
		//Change the path here! 
		String path = "/Users/mkulkarni/Downloads/java-docs-samples-master/speech/cloud-client/resources/output.java";
		try {
			Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String toStr() {
		String res = "";
		for(int i = 0; i< currentFile.length; i++) {
			res+=currentFile[i];
		}
		return res;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("proceed")) {
			String[] a1  = Process.process(currentFile, latest);
			currentFile = concatArr(currentFile, a1);
			display.setText(toStr());
			lineNum();
			saveFile();
		}
		if (e.getActionCommand().equals("enter")) {
			//activate recording - to speech stuff 
			//returns string 
			//call proccessing unit, passing current file 
			//returns an array of strings
			//set TextArea to array of Strings.
			recorder.record();
			try {
				latest = QuickstartSample.recognize();
				display.setText(toStr() + "\n You recorded: \n" + latest);
				lineNum();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}


	public void lineNum() {
		String num = "";

		for(int i = 0; i< currentFile.length; i++) {
			num = num + " " + (i+1) +"\n";
		}

		lineNum.setText(num);

	}
	
	public static String[] concatArr(String[] a1, String[] a2) {
		String[] newArr = new String[a1.length + a2.length];
		for(int i = 0; i < a1.length; i++) {
			newArr[i] = a1[i];
		}
		for(int i = a1.length; i < newArr.length; i++) {
			newArr[i] = a2[i-a1.length];
		}
		return newArr;
	}
}
