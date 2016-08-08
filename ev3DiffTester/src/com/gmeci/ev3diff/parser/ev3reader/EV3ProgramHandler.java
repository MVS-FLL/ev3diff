package com.gmeci.ev3diff.parser.ev3reader;

import java.util.ArrayDeque;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gmeci.EV3Display.EV3ImageManager;
import com.gmeci.ev3diff.parser.ev3blocks.EV3BlockItem;
import com.gmeci.ev3diff.parser.ev3blocks.EV3BlockItem.TerminalItem;
import com.gmeci.ev3diff.parser.ev3blocks.EV3Program;

public class EV3ProgramHandler extends DefaultHandler {

	private static final String BLOCK_DIAGRAM="BlockDiagram";
	private static final String START_BLOCK="StartBlock";
	private static final String NAMESPACE="Namespace ";
	private static final String PROGRAM_BLOCK="ConfigurableMethodCall";
	private static final String WHILE_LOOP_BLOCK="ConfigurableWhileLoop";
	private static final String WAIT_FOR_METHID="ConfigurableWaitFor";
	private static final String WHILE_LOOP_METHID="ConfigurableWhileLoop.BuiltInMethod";
	private static final String SWITCH_METHOD_CALL="PairedConfigurableMethodCall";

	private static final String PROGRAM_BLOCK_DETAIL="ConfigurableMethodTerminal";
	private static final String PROGRAM_BLOCK_CONFIGURED_VALUE="ConfiguredValue";
	private static final String CALL_TYPE_ATTR = "CallType";
	private static final String CALL_TYPE_STOP_CONDITION ="StopCondition";
	private static final String ID_MOTORPORT="MotorPort";
	private static final String ID_SPEED="Speed";
	private static final String ID_ROTATIONS="Rotations";
	private static final String ID_BRAKE="Brake\\ At\\ End";
	private static final String INTERRUPT_NAME_ATR ="InterruptName";

	private static final String CHANGED_ATTR="changed";
	private static final String ID_ATTR="Id";
	private static final String TARGET_ATTR="Target";
	private static final String BOUNDS_ATTR="Bounds";
	private static final String TERMINAL="Terminal";
	private static final String WIRE="Wire";
	private static final String JOINTS="joints";

	ArrayDeque<String> BoundsList = new ArrayDeque<String>();
	ArrayDeque<Boolean> InsideWhileQueue = new ArrayDeque<Boolean>();
	ArrayDeque<Boolean> InsideSwitchQueue = new ArrayDeque<Boolean>();
	
	EV3Program program;
	EV3BlockItem tempBlock=new EV3BlockItem();
	TerminalItem tempTerminalItem=tempBlock.new TerminalItem();
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		program=new EV3Program();
		InsideWhileQueue.push(false);
		InsideSwitchQueue.push(false);
		insideStopCondition=false;
		BoundsList.clear();
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		program.programBlocks.add(tempBlock);
	}
	String ProgramName;
	boolean insideBlockDiagram=false,insideStopCondition=false;
	int BlockCount=0, DetailCount=0;
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals(BLOCK_DIAGRAM))
			insideBlockDiagram=true;
		System.out.println(qName);
		if(qName.equals(NAMESPACE))
		{
			BlockCount=0;
			program.ProgramName = attributes.getValue("Name");
		}

		if(!insideBlockDiagram)
			return;


		
		String BoundsStringTest= attributes.getValue(BOUNDS_ATTR);
		if(BoundsStringTest!= null)
		{
			BoundsList.addFirst(BoundsStringTest);
		}
		else
			BoundsList.addFirst("");
		
		if(qName.equals(START_BLOCK))
		{
			tempBlock= new EV3BlockItem();
			tempBlock.ID = attributes.getValue("StartBlock");
			String BoundsString= attributes.getValue(BOUNDS_ATTR);
			tempBlock.Bound=parseBounds(BoundsString);
			tempBlock.Target= attributes.getValue(TARGET_ATTR);
		}
		else if(qName.equals(PROGRAM_BLOCK))
		{
			tempBlock= new EV3BlockItem();
			tempBlock.BlockPosition=BlockCount;
			BlockCount++;
			tempBlock.changed = getBoolean(attributes, CHANGED_ATTR);
			tempBlock.ID = attributes.getValue(ID_ATTR);
			
			tempBlock.Bound=AggregateBounds(BoundsList, false);
			
			tempBlock.Target= attributes.getValue(TARGET_ATTR);
			DetailCount=0;
		}
		else if(qName.equals(WHILE_LOOP_METHID))
		{
			InsideWhileQueue.push(true);
			InsideSwitchQueue.push(false);
			String CallMethod =  attributes.getValue(CALL_TYPE_ATTR);
			if(CallMethod.equals(CALL_TYPE_STOP_CONDITION))
			{
				insideStopCondition=true;
			}
		}
		else if(qName.equals(SWITCH_METHOD_CALL))
		{

			InsideWhileQueue.push(false);
			InsideSwitchQueue.push(true);
			tempBlock= new EV3BlockItem();
			String BoundsString= attributes.getValue(BOUNDS_ATTR);
			tempBlock.Bound = parseBounds(BoundsString);

			tempBlock.Target = attributes.getValue(TARGET_ATTR)+EV3ImageManager.SWITCH;
			tempBlock.changed = getBoolean(attributes, CHANGED_ATTR);
			tempBlock.Bound=AggregateBounds(BoundsList, false);
		}
		else if(qName.equals(WHILE_LOOP_BLOCK))
		{
			tempBlock= new EV3BlockItem();
			tempBlock.interrupt = attributes.getValue(INTERRUPT_NAME_ATR);
			tempBlock.BlockPosition=BlockCount;
			BlockCount++;
			tempBlock.changed = getBoolean(attributes, CHANGED_ATTR);
			tempBlock.ID = attributes.getValue(ID_ATTR);
		//	String BoundsString= attributes.getValue(BOUNDS_ATTR);
		//	tempBlock.Bound=parseBounds(BoundsString);

			tempBlock.Bound=AggregateBounds(BoundsList, false);
			tempBlock.Target= attributes.getValue(TARGET_ATTR);
			DetailCount=0;
		}
		else if(qName.equals(WAIT_FOR_METHID))
		{

			InsideWhileQueue.push(false);
			InsideSwitchQueue.push(true);
			
			tempBlock= new EV3BlockItem();
			tempBlock.Target = attributes.getValue(TARGET_ATTR)+EV3ImageManager.WAIT;
			tempBlock.Modifier= EV3ImageManager.WAIT;
//			String BoundsString= attributes.getValue(BOUNDS_ATTR);
//			tempBlock.Bound=parseBounds(BoundsString);

			tempBlock.Bound=AggregateBounds(BoundsList, false);
		}
		else if(qName.equals(PROGRAM_BLOCK_DETAIL))
		{
			insideDetail=true;
			if(tempBlock == null)
			{
				System.out.println("block");
			}
			tempTerminalItem = tempBlock.new TerminalItem();
			tempTerminalItem.changed = getBoolean(attributes, CHANGED_ATTR);
			tempTerminalItem.ConfiguredValue= attributes.getValue(PROGRAM_BLOCK_CONFIGURED_VALUE);
			tempTerminalItem.TerminalPosition=DetailCount;
			DetailCount++;
		}
		else if(qName.equals(TERMINAL))
		{
			if(insideDetail)
			{
				tempTerminalItem.ID = attributes.getValue(ID_ATTR);
				if(!tempTerminalItem.changed)
					tempTerminalItem.changed = getBoolean(attributes, CHANGED_ATTR);
			//	String BoundsString = attributes.getValue(BOUNDS_ATTR);
			//	tempTerminalItem.Bound = parseBounds(BoundsString);

				tempTerminalItem.Bound=AggregateBounds(BoundsList,true);
				tempBlock.terminalItems.add(tempTerminalItem);
			}
		}

		super.startElement(uri, localName, qName, attributes);
	}


	private boolean getBoolean(Attributes attributes, String changedAttr) {

		String boolValueString = attributes.getValue(changedAttr);
		try
		{
			if(boolValueString==null)
				return false;
			return boolValueString.equals("true");
		}
		catch(Exception e)
		{

		}
		return false;
	}
	boolean insideDetail=false;
	
	private int[] AggregateBounds(ArrayDeque<String> boundsList, boolean Allowskip) {
	
		Allowskip= true;
		int[] totalBounds = new int[4];
		boolean skip = false;
		for(String boundString:boundsList)
		{
			int[] nextBounds=parseBounds(boundString);
			for(int i=0;i<4;i++)
			{
				if((i==2 && !skip)|| i!=2)
					totalBounds[i] = totalBounds[i]+ nextBounds[i];
				
				if(i==2 && totalBounds[i]!=0&& Allowskip)
					skip = true;
			}
		}
		return totalBounds;
	}
	
	
	private int[] parseBounds(String boundsString) {
		StringTokenizer st = new StringTokenizer(boundsString);
		if(st.countTokens()!= 4)
			return new int[4];
		try
		{
			int[] bounds=new int[4];
			for(int i=0; st.hasMoreTokens();i++) {
				bounds[i] = Integer.parseInt(st.nextToken());
			}
			return bounds;
		}
		catch(Exception E)
		{
			return null;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		
		String Bounds2Remove="";
		if(!BoundsList.isEmpty())
			Bounds2Remove=BoundsList.removeFirst();
		
		if(qName.equals(BLOCK_DIAGRAM))
			insideBlockDiagram=false;

		if(qName.equals(START_BLOCK))
		{
			//done reading start block - store on the list?
			program.programBlocks.add(tempBlock);
			tempBlock=null;
		}
		else if(qName.equals(PROGRAM_BLOCK))//&&!insideWhile)
		{
			
			if(InsideWhileQueue.element() )
			{
				//remiove these bounds???
				int bounds2Remove[]= this.parseBounds(Bounds2Remove);
				
//				for(int i=0;i<4;i++)
//				{
//					tempBlock.Bound[i] = tempBlock.Bound[i]- bounds2Remove[i];
//				}
				tempBlock.Modifier= EV3ImageManager.LOOP;
				tempBlock.Target = tempBlock.Target+ EV3ImageManager.LOOP;
			}
			//done reading a program block - store on the list?
			program.programBlocks.add(tempBlock);
			tempBlock=null;

		}
		else if(qName.equals(WAIT_FOR_METHID))
		{

			InsideWhileQueue.pop();
			InsideSwitchQueue.pop();
			program.programBlocks.add(tempBlock);
			tempBlock=null;
		}
		else if(qName.equals(SWITCH_METHOD_CALL))
		{
			program.programBlocks.add(tempBlock);
			tempBlock=null;
		}
		else if(qName.equals(WHILE_LOOP_BLOCK))
		{
			//done reading a program block - store on the list?
//			if(tempBlock!= null)
//			{
//				program.programBlocks.add(tempBlock);
//				tempBlock=null;
//			}
		}
		else if(qName.equals(WHILE_LOOP_METHID))
		{

			InsideWhileQueue.pop();
			InsideSwitchQueue.pop();
			
			//moved from while_loop_block. - we lose the while loop if we don't 
			insideStopCondition=false;
			//done reading a program block - store on the list?
			if(tempBlock!= null)
			{
				tempBlock.Modifier= EV3ImageManager.LOOP;
				tempBlock.Target = tempBlock.Target+ EV3ImageManager.LOOP;
				program.programBlocks.add(tempBlock);
				tempBlock=null;
			}
		}
		else if(qName.equals(PROGRAM_BLOCK_DETAIL))
		{
			insideDetail=false;
		}
		super.endElement(uri, localName, qName);
	}

	public EV3Program getProgram() {
		return program;
	}



}
