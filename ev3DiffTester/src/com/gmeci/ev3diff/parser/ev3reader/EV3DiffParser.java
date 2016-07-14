package com.gmeci.ev3diff.parser.ev3reader;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.gmeci.ev3diff.parser.ev3blocks.EV3Program;

public class EV3DiffParser {

	protected SAXParserFactory spf;
	protected SAXParser sp;
	protected XMLReader xr;
	EV3Program program;
	
	public EV3DiffParser()
	{
		spf = SAXParserFactory.newInstance();
	    try 
	    {
	    	 sp = spf.newSAXParser();
		     xr = sp.getXMLReader();
		} 
	    catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} 
	    catch (SAXException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public EV3Program ParseProgram(InputSource singleProgramXMLStream)
	{
		EV3ProgramHandler handler = new EV3ProgramHandler();
		xr.setContentHandler(handler);
		try {
			xr.parse(singleProgramXMLStream);
			program = handler.getProgram();
			return program;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}


	public EV3Program getProgram() {
		return program;
	}
	
}
