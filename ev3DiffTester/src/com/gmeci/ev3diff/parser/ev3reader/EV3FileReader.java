package com.gmeci.ev3diff.parser.ev3reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gmeci.ev3diff.parser.ev3blocks.EV3Program;


public class EV3FileReader {


	public class EV3DifferenceItem
	{
		String AttributeKey;
		int DifferenceID;
		int ConfigurableMethodCallNode, ConfigurableMethodTerminalNode;
	}

	public class ZipFileEntry
	{
		public String ProgramName;
		public InputStream ProgramZipStream;
		public EV3Program program;

	}
	public ProjectItem project;
	public class ProjectItem
	{
		ProjectItem()
		{
			ProgramList=new ArrayList<ZipFileEntry>();
		}
		public String FileName;
		public String ProjectName;
		public ArrayList<ZipFileEntry> ProgramList;
		public boolean Valid() {
			if(ProjectName==null || ProgramList==null|| ProgramList.size()<1)
				return false;
			return true;
		}
	}
	public EV3FileReader()
	{
		project = new ProjectItem();
	}


	private  static final String PROJECT_TITLE_FILE_NAME = "ProjectTitle";
	private  static final String PROGRAM_FILE_EXTENSION = ".ev3p";
	public boolean ReadEV3File(String EV3FileName)
	{
		project.FileName=EV3FileName;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(EV3FileName);	
			ZipEntry projectTitleFile= zipFile.getEntry(PROJECT_TITLE_FILE_NAME);
			//			if(projectTitleFile==null)
			//				return false;
			//			InputStream projectNameInputStream;
			//			projectNameInputStream = zipFile.getInputStream(projectTitleFile);
			//			String ProjectName = IOUtils.toString(projectNameInputStream, "UTF-8");
			//			System.out.println("Found Project Name:"+ ProjectName);
			//

			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while(entries.hasMoreElements()){
				ZipEntry entry = entries.nextElement();
				if(entry.getName().contains(PROGRAM_FILE_EXTENSION))
				{
					ZipFileEntry newEntry=new ZipFileEntry();
					newEntry.ProgramName= entry.getName();
					newEntry.ProgramZipStream = zipFile.getInputStream(entry);
					project.ProgramList.add(newEntry);
					System.out.println("\tFound Program:"+entry.getName());
				}
				else if(entry.getName().contains(PROJECT_TITLE_FILE_NAME))
				{
					InputStream projectNameInputStream;
					projectNameInputStream = zipFile.getInputStream(entry);
					project.ProjectName = IOUtils.toString(projectNameInputStream, "UTF-8");
					System.out.println("Found Project Name:"+ project.ProjectName);
				}
			}
		} catch (IOException e) {
			System.out.println("Failed to read file");
			//e.printStackTrace();
			return false;
		}

		EV3DiffParser parser=new EV3DiffParser();
		//can I read the input stream here?
		for(ZipFileEntry entry:project.ProgramList)
		{
			System.out.println("Parsing program: "+entry.ProgramName);
			entry.program= parser.ParseProgram(new InputSource(entry.ProgramZipStream));
		}

		try {
			zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public static List<Difference> GetDiff(ProjectItem baseProject, ProjectItem newProject, int baseItemNumber, int newItemNumber) {
		ZipFile baseZipFile =null, newZipFile=null;
		try {
			baseZipFile = new ZipFile(baseProject.FileName);
			newZipFile = new ZipFile(newProject.FileName);	

			Enumeration<? extends ZipEntry> baseEntries = baseZipFile.entries();
			Enumeration<? extends ZipEntry> newEntries = newZipFile.entries();
			ZipEntry baseEntry = null, newEntry=null;

			baseEntry= GetProgramEntryByIndex(baseEntries, baseItemNumber);
			newEntry= GetProgramEntryByIndex(newEntries, newItemNumber);

			InputStream baseStream = baseZipFile.getInputStream(baseEntry);
			InputStream newStream = newZipFile.getInputStream(newEntry);

			Diff diff=new Diff(new InputSource(baseStream), new InputSource(newStream));
			//						diff.overrideDifferenceListener(new DifferenceListener()
			//						{
			//			
			//							@Override
			//							public int differenceFound(Difference arg0) {
			//								if(arg0.getControlNodeDetail().getXpathLocation().contains("Bounds"))
			//									return RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
			//								return RETURN_ACCEPT_DIFFERENCE;
			//							}
			//			
			//							@Override
			//							public void skippedComparison(Node arg0, Node arg1) {
			//								// TODO Auto-generated method stub
			//			
			//							}
			//			
			//						});
			DetailedDiff myDiff = new DetailedDiff(diff);
			List<Difference> differences=  myDiff.getAllDifferences();
			//now we doctor up the existing XML???
			baseStream.close();newStream.close();

			//get a fresh stream?
			baseStream = baseZipFile.getInputStream(baseEntry);
			newStream = newZipFile.getInputStream(newEntry);

			String modifiedBaseXML= UpdateStreamWithDifferences(baseStream, differences, true);
			String modifiedNewXML= UpdateStreamWithDifferences(newStream, differences, false);
			baseZipFile.close();newZipFile.close();

			//now rebuild the program we're looking at...

			EV3DiffParser parser=new EV3DiffParser();
			EV3Program program = parser.ParseProgram(new InputSource(new StringReader(modifiedBaseXML)));
			if(program!= null )
			{
				baseProject.ProgramList.get(baseItemNumber).program= program;
			}

			program = parser.ParseProgram(new InputSource(new StringReader(modifiedNewXML)));
			if(program!= null )
			{
				newProject.ProgramList.get(newItemNumber).program= program;
			}


			return differences;

		}
		catch(Exception e)
		{
			System.out.println("Failed to diff:"+e.toString());
		}
		return new ArrayList<Difference>();


	}

	private static String UpdateStreamWithDifferences(InputStream baseStream, List<Difference> differences, boolean base) {
		// TODO Auto-generated method stub
		//test DOM
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		Document doc ;
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(baseStream);
			XPathFactory xpathFactory = XPathFactory.newInstance();

			for(Difference difference:differences)
			{
				{
					String xpath = "";
					if(base)
					{
						xpath = difference.getControlNodeDetail().getXpathLocation();	
					}
					else
						xpath=difference.getTestNodeDetail().getXpathLocation();

					if(xpath!= null && !xpath.contains("Bounds")&& !xpath.contains("Hotspot")&& !xpath.contains("text()"))
					{
						int attrStart= xpath.indexOf("@");
						if(attrStart<=0)
						{
							attrStart = xpath.length()+1;
						}
						xpath = xpath.substring(0,attrStart-1);
						javax.xml.xpath.XPathExpression xpathExp = xpathFactory.newXPath().compile(xpath);
						NodeList nodeList = (NodeList)xpathExp.evaluate(doc, XPathConstants.NODESET);
						try
						{
							Element nodeAsElement = (Element)nodeList.item(0);
							nodeAsElement.setAttribute("changed","true");					
						}
						catch (Exception e)
						{
							System.out.println("failed at "+xpath);
						}
					}

				}
			}

			//System.out.println("/SourceFile[1]/Namespace[1]/VirtualInstrument[1]/BlockDiagram[1]/ConfigurableMethodCall[4]/ConfigurableMethodTerminal[3]");

			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();

		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("1");
			e1.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			System.out.println("2");
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.println("3");
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			System.out.println("4");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			System.out.println("5");
			e.printStackTrace();
		}
		return "";
	}
	public List<EV3DifferenceItem> GetBaseDifferences(List<Difference> differences)
	{
		ArrayList<EV3DifferenceItem> EV3Differences=new ArrayList<EV3DifferenceItem> ();
		for(Difference difference:differences)
		{
			EV3DifferenceItem nextItem = new EV3DifferenceItem();
			nextItem.DifferenceID= difference.getId();
			if(nextItem.DifferenceID==DifferenceConstants.ATTR_VALUE_ID)
			{
				String StartingXPath= difference.getControlNodeDetail().getXpathLocation().replace("/SourceFile[1]/Namespace[1]/VirtualInstrument[1]/BlockDiagram[1]", "");
			}

		}
		return null;
	}

	private static ZipEntry GetProgramEntryByIndex(Enumeration<? extends ZipEntry> baseEntries, int index)
	{
		int programIndex=0;
		ZipEntry foundEntry=null;
		while(baseEntries.hasMoreElements()){
			foundEntry=baseEntries.nextElement();
			if(foundEntry.getName().contains(PROGRAM_FILE_EXTENSION))
			{
				if(programIndex == index)
				{
					//found the right entry...
					return foundEntry;
				}
				else
					programIndex++;
			}
		}		
		return foundEntry;
	}
}
