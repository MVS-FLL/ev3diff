package com.gmeci.ev3diff;


import java.awt.EventQueue;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.gmeci.EV3Display.EV3Frame;
import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader;
import com.sun.webkit.Utilities;


public class EV3DiffMain {

	
	public static void main(final String[] args) throws InterruptedException, URISyntaxException {
		
		if(args.length<2)
			System.out.println("usage: EV3Diff <Base.ev3> <New.ev3>");
		
			
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EV3FileReader baseFileReader = new EV3FileReader();
					EV3FileReader newFileReader = new EV3FileReader();
					if(args.length>0)
						baseFileReader.ReadEV3File(args[0]);

					if(args.length>1)
						newFileReader.ReadEV3File(args[1]);
					
					
					EV3Frame frame = new EV3Frame(baseFileReader.project,newFileReader.project);
					
					frame.setVisible(true);
					frame.pack();
					frame.setSize(400,400);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
			        javax.swing.SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			                //Turn off metal's use of bold fonts
				        UIManager.put("swing.boldMetal", Boolean.FALSE);
			               
			            }
			        });
			        
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
