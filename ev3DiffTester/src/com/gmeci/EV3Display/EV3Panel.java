package com.gmeci.EV3Display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.gmeci.ev3diff.parser.ev3blocks.EV3BlockItem;
import com.gmeci.ev3diff.parser.ev3blocks.EV3BlockItem.TerminalItem;
import com.gmeci.ev3diff.parser.ev3blocks.EV3Program;
import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader;
import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader.ProjectItem;
import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader.ZipFileEntry;

public class EV3Panel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8394404224767921245L;
	ProjectItem newProject, baseProject;

	int DragX=0, DragY=0;

	private static final int BASE_FONT_SIZE=12;
	private static final int BLOCK_WIDTH = 2;

	EV3ImageManager ev3im= new EV3ImageManager();
	private float Scale=1f;

	private  int baseItemNumber=0, newItemNumber=0;
	public EV3Panel(ProjectItem baseProject, ProjectItem newProject) {
		this.baseProject=baseProject;
		this.baseProject=baseProject;

		this.addMouseListener(mouseListener);
		this.addMouseMotionListener(mml);
		ev3im.LoadImages();
	}


	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(baseProject==null||!baseProject.Valid())
			return;
		if(baseProject.ProgramList.size()<=baseItemNumber)
		{
			baseItemNumber=baseProject.ProgramList.size()-1;
		}	
		if(baseProject.ProgramList.size()<=newItemNumber)
		{
			newItemNumber=baseProject.ProgramList.size()-1;
		}
		g.setFont(new Font("Breuer", Font.PLAIN, (int)(BASE_FONT_SIZE*Scale)));
		//	ArrayList<> BaseDiffernces;
		//maybe this is the spot to figure out the diff???
		EV3FileReader.GetDiff(baseProject, newProject, baseItemNumber, newItemNumber);


		float[] fadeScales = { .3f, .3f, .3f, 0.3f };
		float[] scales = { 1, 1, 1, 1 };
		float[] offsets = new float[4];
		Graphics2D g2d = (Graphics2D)g;
		RescaleOp fadeROP = new RescaleOp(fadeScales, offsets, null);
		RescaleOp noROP = new RescaleOp(scales, offsets, null);


		if(baseItemNumber>=0)
		{

			ZipFileEntry entry = baseProject.ProgramList.get(baseItemNumber);
			//drawBaseFile(g);
			if(FadeNew)
				drawNewFile(g2d, noROP,0,0,entry);
			else drawNewFile(g2d, fadeROP, 0,0,entry);

		}
		if(newItemNumber>=0)
		{
			ZipFileEntry entry =newProject.ProgramList.get(newItemNumber);
			if(FadeNew)
				drawNewFile(g2d, fadeROP,20,90,entry);
			else
				drawNewFile(g2d, noROP,20,90,entry);
		}
	}

	private static final int LOOP_OFFSET_Y = -20;
	private static final int LOOP_TIMER_OFFSET_X = -40;
	private void drawNewFile(Graphics2D g2d, RescaleOp baseRop,int XOffset, int YOffset,ZipFileEntry entry) {


		float[]  offsets ={80,0,0,0};
		RescaleOp rop, red = new RescaleOp(baseRop.getScaleFactors(null),offsets, null);

		float RealStretch = StrectchValue+1;
		XOffset += ev3im.getPanXOffset();//PanOffsetX;
		YOffset += ev3im.getPanYOffset();//PanOffsetY;

		XOffset+=2;
		for(EV3BlockItem block:entry.program.programBlocks)
		{
			if(block == null|| block.Target==null)
				break;

			String TargetName = block.Target.replace("\\.vix", "");
			TargetName = TargetName.replace("\\\\.vix", "");
			TargetName = TargetName.replace(".\\vix", "");
			TargetName = TargetName.replace("X3\\.Lib:", "");
		//	TargetName = TargetName+block.Modifier;
			if(block.changed)
				rop= red;
			else
				rop = baseRop;

			if(TargetName.contains("LoopIndex"))
			{
				continue;
			}

			System.out.println("Target Name "+ TargetName);
			
			if(!ev3im.imageAvailable(TargetName))
			{
				if(TargetName.contains(EV3Program.MY_BLOCK_SUFFIX))
				{				
					BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.myBlockImage,Scale);
					g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*( block.Bound[1]+YOffset)))); // see javadoc for more info on the parameters 

					String ModifiedName = TargetName.replace("\\.ev3p", "");
					g2d.setColor(Color.white);
					g2d.drawString(ModifiedName,  (int)(Scale*(block.Bound[0]+XOffset+15)),YOffset+15+(int)(RealStretch*(Scale*(block.Bound[1]))));
					g2d.setColor(Color.black);

				}
				else if(TargetName.contains(EV3Program.START_BLOCK))
				{				

					BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.startImage,Scale);
					g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int)(RealStretch*(Scale* (block.Bound[1]+YOffset)))); 
				}
				else
				{
					BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.unknownImage,Scale);
					System.out.println("Could not find :"+block.Target+" named "+TargetName);
					g2d.drawImage(newImage2Draw,rop, (int)(Scale* ( block.Bound[0]+ XOffset)),(int)(RealStretch*(Scale* (block.Bound[1]+YOffset))));
				}
			}
			else
			{	
				//is on the hashmap
				if(TargetName.contains(EV3Program.LOOP_SUFFIX))
				{//is a loop image - special
					BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.loopLeftImage,Scale);
//					g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*( LOOP_OFFSET_Y+ block.Bound[1]+YOffset)))); 
					g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*(  block.Bound[1]+YOffset)))); 
					//					//need a right side also...
					BufferedImage newImage2Draw2 = ev3im.ImageNameMap.get(TargetName);	
					int RightImageWidth = newImage2Draw2.getWidth();
					g2d.drawImage(newImage2Draw2,rop,  (int)(Scale* (block.Bound[0]+ block.Bound[2]-RightImageWidth+XOffset)),(int) ( RealStretch* (Scale*(LOOP_OFFSET_Y+ block.Bound[1]+YOffset)))); // see javadoc for more info on the parameters 

					//put the interrupt name in the middle???
					g2d.drawString(block.interrupt,(int)(Scale* (block.Bound[0]+ (block.Bound[2]/2)+XOffset)),(int) ( RealStretch* (Scale*( block.Bound[1]+YOffset))));
					
					//draw all the text by hand for loops
					TerminalItem LongItem = GetTerminalListItemValue(block.terminalItems, EV3Program.HOW_LONG);
					if(LongItem!= null)
					{
						g2d.drawString(LongItem.ConfiguredValue, (int)(Scale*(block.Bound[0]+ + block.Bound[2]+XOffset+LOOP_TIMER_OFFSET_X)),(int) ( RealStretch* (Scale*(  LongItem.Bound[1]+YOffset- LOOP_OFFSET_Y))));
					}
					continue;
				}
				else if(TargetName.contains(EV3Program.LOOP_INFINITE) )
				{//loop
					//BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.ImageNameMap.get(TargetName),Scale);
					//g2d.drawImage(newImage2Draw, rop, (int)(Scale* ( block.Bound[0]+XOffset)),(int)(RealStretch*(Scale*  (block.Bound[1]+YOffset))));//(steerRotationsImage, rop, steerRotationsImage,rop,  block.Bound[0],block.Bound[1]);//.drawImage(try2,  rop,  0, 0);
					//YOffset-=100;
					BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.loopLeftImage,Scale);
//					g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*( LOOP_OFFSET_Y+ block.Bound[1]+YOffset)))); 
					g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*(  block.Bound[1]+YOffset-24)))); 
					//					//need a right side also...
					BufferedImage newImage2Draw2 = ev3im.ImageNameMap.get(TargetName);	
					int RightImageWidth = newImage2Draw2.getWidth();
					g2d.drawImage(newImage2Draw2,rop,  (int)(Scale* (block.Bound[0]+ block.Bound[2]-RightImageWidth+XOffset)),(int) ( RealStretch* (Scale*(LOOP_OFFSET_Y+ block.Bound[1]+YOffset)))); // see javadoc for more info on the parameters 

					//put the interrupt name in the middle???
					g2d.drawString(block.interrupt,(int)(Scale* (block.Bound[0]+ (block.Bound[2]/2)+XOffset)),(int) ( RealStretch* (Scale*( block.Bound[1]+YOffset))));
					
					//draw all the text by hand for loops
					TerminalItem LongItem = GetTerminalListItemValue(block.terminalItems, EV3Program.HOW_LONG);
					if(LongItem!= null)
					{
						g2d.drawString(LongItem.ConfiguredValue, (int)(Scale*(block.Bound[0]+ + block.Bound[2]+XOffset+LOOP_TIMER_OFFSET_X)),(int) ( RealStretch* (Scale*(  LongItem.Bound[1]+YOffset- LOOP_OFFSET_Y))));
					}
					continue;
				}
				else
				{
					if(block.Modifier.equals(EV3ImageManager.LOOP))
					{
						//BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.ImageNameMap.get(TargetName),Scale);
						//g2d.drawImage(newImage2Draw, rop, (int)(Scale* ( block.Bound[0]+XOffset)),(int)(RealStretch*(Scale*  (block.Bound[1]+YOffset))));//(steerRotationsImage, rop, steerRotationsImage,rop,  block.Bound[0],block.Bound[1]);//.drawImage(try2,  rop,  0, 0);
						//YOffset-=100;
						BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.loopLeftImage,Scale);
//						g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*( LOOP_OFFSET_Y+ block.Bound[1]+YOffset)))); 
						g2d.drawImage(newImage2Draw,rop,  (int)(Scale* (block.Bound[0]+XOffset)),(int) ( RealStretch* (Scale*(  block.Bound[1]+YOffset-24)))); 
						//					//need a right side also...
						BufferedImage newImage2Draw2 = ev3im.ImageNameMap.get(TargetName);	
						int RightImageWidth = newImage2Draw2.getWidth();
						g2d.drawImage(newImage2Draw2,rop,  (int)(Scale* (block.Bound[0]+ block.Bound[2]-RightImageWidth+XOffset)),(int) ( RealStretch* (Scale*(LOOP_OFFSET_Y+ block.Bound[1]+YOffset)))); // see javadoc for more info on the parameters 

						//put the interrupt name in the middle???
						g2d.drawString(block.interrupt,(int)(Scale* (block.Bound[0]+ (block.Bound[2]/2)+XOffset)),(int) ( RealStretch* (Scale*( block.Bound[1]+YOffset))));
						
						//draw all the text by hand for loops
						TerminalItem LongItem = GetTerminalListItemValue(block.terminalItems, EV3Program.HOW_LONG);
						if(LongItem!= null)
						{
							g2d.drawString(LongItem.ConfiguredValue, (int)(Scale*(block.Bound[0]+ + block.Bound[2]+XOffset+LOOP_TIMER_OFFSET_X)),(int) ( RealStretch* (Scale*(  LongItem.Bound[1]+YOffset- LOOP_OFFSET_Y))));
						}
					}
					else
					{
						BufferedImage newImage2Draw= ev3im.scaleImage(ev3im.ImageNameMap.get(TargetName),Scale);
						g2d.drawImage(newImage2Draw, rop, (int)(Scale* ( block.Bound[0]+XOffset)),(int)(RealStretch*(Scale*  (block.Bound[1]+YOffset))));//(steerRotationsImage, rop, steerRotationsImage,rop,  block.Bound[0],block.Bound[1]);//.drawImage(try2,  rop,  0, 0);
					}	
				}
				//	g2d.drawImage(ImageNameMap.get(TargetName),                   rop,  block.Bound[0]+XOffset, block.Bound[1]+YOffset);//(steerRotationsImage, rop, steerRotationsImage,rop,  block.Bound[0],block.Bound[1]);//.drawImage(try2,  rop,  0, 0);
			}
			for(TerminalItem terminal:block.terminalItems)
			{
				try
				{
					int offsetX=XOffset,offsetY=YOffset;
					if(terminal.ID.equals("SequenceOut")||terminal.ID.contains("Interrupts"))
						continue;
					if(terminal.ID.equals("Ports")|| terminal.ID.equals("MotorPort"))
					{
						offsetX +=  block.Bound[BLOCK_WIDTH]-40;
						//						offsetY +=15;
					}
					else if(terminal.ID.equals("IRSeeker"))
					{
						offsetX += 160;
					}
					else if(terminal.ID.equals("Text"))
					{
						offsetX += 130;
					}
					else if( TargetName.equals("ColorCompare")&& terminal.ID.equals("Port"))
					{
						offsetX += 90;
					}
					else if(terminal.ID.equals("Port"))
					{
						offsetX += 60;
					}
					else if(TargetName.equals(EV3ImageManager.TOGGLE_INTERRUPT))
					{
						offsetX += 60;
					}
					else offsetY += 20;
//System.out.println("ID "+terminal.ID+ " value:"+terminal.ConfiguredValue);
					String Text2Show = terminal.ConfiguredValue.replace("1.","");
					if(Text2Show.equals("3"))
						System.out.println("GGG");;
					if(terminal.ConfiguredValue.equals("True"))
						Text2Show =String.format(" %c", 0x2714);
					else if(terminal.ConfiguredValue.equals("False"))
						Text2Show=" X";

					Font oldFont=null;
					if(terminal.changed)
					{
						oldFont= g2d.getFont();
						g2d.setFont(new Font("Breuer", Font.BOLD, (int)(BASE_FONT_SIZE*Scale)));
						g2d.setColor(Color.RED);
					}

					g2d.drawString(Text2Show, (int)(Scale*(terminal.Bound[0]+ block.Bound[0]+ offsetX)), (RealStretch* YOffset)+terminal.Bound[1]+(int)(RealStretch*(Scale*(15+block.Bound[1]))));

					if(terminal.changed)
					{
						g2d.setFont(oldFont);
						g2d.setColor(Color.BLACK);
					}
					g2d.setBackground(Color.white);
				}
				catch(Exception e)
				{

				}
			}
		}
	}
	private TerminalItem GetTerminalListItemValue(ArrayList<TerminalItem> terminalItems, String howLong) {
		for(TerminalItem ti:terminalItems)
		{
			if(ti.ID.equals(howLong))
				return ti;
		}
		return null;
	}

	public void updateBase(ProjectItem project) {
		this.baseProject = project;
		this.revalidate();
		repaint();
	}
	public void updateNew(ProjectItem project) {
		newProject=project;
		this.newProject = project;
		this.revalidate();
		repaint();
	}
	public void setBaseIndex(int index) {
		baseItemNumber=index;
		revalidate();
		repaint();

	}

	public void setNewIndex(int index) {
		newItemNumber=index;
		revalidate();
		repaint();

	}

	public void Zoom(boolean ZoomIn) {
		if(ZoomIn)
		{
			Scale +=.1;
		}
		else
		{

			Scale -= .1;
			if(Scale<0)
			{
				Scale += .1;
				Scale =Scale/2;
			}
		}
		revalidate();
		repaint();
	}

	MouseListener mouseListener= new MouseListener()
	{

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent event) {
			ev3im.setPanBase( event.getPoint());
		
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	};

	MouseMotionListener mml=new MouseMotionListener()
	{

		@Override
		public void mouseDragged(MouseEvent event) {
			Point p = event.getPoint();

			ev3im.updatePanOffset(p);
			revalidate();
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	};

	public void Pan(int key) {
		ev3im.Pan(key);
		revalidate();
		repaint();
	}


	boolean FadeNew=true;
	public void updateFade(boolean fadeNew) {
		FadeNew=fadeNew;
		revalidate();
		repaint();
	}

	//LOU GET RID OFTHIS
	float StrectchValue=0;
	public void Strech(int newOffset) {
		ev3im.setStretch(newOffset/100f);
		revalidate();
		repaint();
	}

}
