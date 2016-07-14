package com.gmeci.ev3diff.parser.ev3blocks;

import java.util.ArrayList;

public class EV3BlockItem {
	//individual block of a program
	public class TerminalItem
	{
		public String toString()
		{
			return String.format("ID "+ID+" Value "+ConfiguredValue);
		}
		public boolean changed=false;
		public int TerminalPosition;
		public String ConfiguredValue, ID, Direction;
		public int Bound[]=new int[4];
	}

	public String toString()
	{
		return String.format("%s %s %d %d", Target, ID, Bound[0], Bound[1]);
		
	}
	public boolean changed = false;
	public int BlockPosition=0;
	public int Bound[]=new int[4];
	public String ID, Target,interrupt;

	public ArrayList<TerminalItem> terminalItems=new ArrayList<TerminalItem>();

	public String Modifier="";
	
	
}
