package com.gmeci.EV3Display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader;
import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader.ProjectItem;
import com.gmeci.ev3diff.parser.ev3reader.EV3FileReader.ZipFileEntry;

@SuppressWarnings("serial")
public class EV3Frame extends JFrame {


	private void updateBase(ProjectItem project) 
	{

		if(project==null || !project.Valid())
		{
			baseProjectNameLabel.setText("Invalid File Selected");
			baseProjectNameLabel.setBackground(Color.red);
			baseProjectNameLabel.setText(project.FileName);
			baseProjectNameLabel.setBackground(Color.red);
			return;
		}
		baseProjectNameLabel.setBackground(Color.white);
		baseProjectNameLabel.setText(project.ProjectName+"   ");

		baseProjectNameLabel.setText(project.FileName);
		baseProjectNameLabel.setBackground(Color.white);
		baseFileProgramSpinner.removeAllItems();
		for(ZipFileEntry entry:project.ProgramList)
			baseFileProgramSpinner.addItem(entry.ProgramName);
		ev3DisplayPanel.updateBase(project);

	}
	private void updateNew(ProjectItem project) 
	{

		if(project==null || !project.Valid())
		{
			newProjectNameLabel.setText("Invalid File Selected");
			newProjectNameLabel.setBackground(Color.red);

			newFileNameLabel.setText(project.FileName);
			newFileNameLabel.setBackground(Color.red);
			return;
		}

		newFileNameLabel.setText(project.FileName);
		newFileNameLabel.setBackground(Color.white);
		newProjectNameLabel.setBackground(Color.white);
		newProjectNameLabel.setText(project.ProjectName+"   ");
		newFileProgramSpinner.removeAllItems();
		for(ZipFileEntry entry:project.ProgramList)
			newFileProgramSpinner.addItem(entry.ProgramName);
		ev3DisplayPanel.updateNew(project);

	}
	

	DropTarget baseFileDropTarget = new DropTarget()
	{
		public synchronized void drop(DropTargetDropEvent evt)
		{
			try 
			{
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				@SuppressWarnings("unchecked")
				List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
				if(!droppedFiles.isEmpty())
				{
					//only use the first one///
					//parse the new file

					EV3FileReader baseFileReader = new EV3FileReader();
					baseFileReader.ReadEV3File(droppedFiles.get(0).getAbsolutePath());
					//update the label and spinner
					updateBase(baseFileReader.project);
					//redraw everything?
				}

			} catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}
	};
	
	DropTarget newFileDropTarget = new DropTarget()
	{
		@SuppressWarnings("unchecked")
		public synchronized void drop(DropTargetDropEvent evt)
		{
			try 
			{
				evt.acceptDrop(DnDConstants.ACTION_COPY);
				List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
				if(!droppedFiles.isEmpty())
				{
					//only use the first one///
					//parse the new file

					EV3FileReader newFileReader = new EV3FileReader();
					newFileReader.ReadEV3File(droppedFiles.get(0).getAbsolutePath());
					//update the label and spinner
					updateNew(newFileReader.project);
					//redraw everything?
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	JButton btnNewButton_1 ;
	boolean FadeNew=true;
	/**
	 * Create the frame.
	 * @param programNameList 
	 */
	JLabel baseProjectNameLabel, newProjectNameLabel;
	String[] baseProgramNames, newProgramNames;
	JComboBox<String> baseFileProgramSpinner, newFileProgramSpinner;
	EV3Panel ev3DisplayPanel;
	public EV3Frame(ProjectItem baseProject, ProjectItem newProject) {

		getContentPane().setSize(500,500);
		getContentPane().setLayout(new BorderLayout(10, 10));
		//	this.setDropTarget(baseFileDropTarget);
		ImageIcon img = new ImageIcon(getClass().getResource( "/resources/ic_launcher.png" ));
		setIconImage(img.getImage());

		baseProgramNames= getProgramNames(baseProject.ProgramList);
		newProgramNames= getProgramNames(newProject.ProgramList);		

		setupPannel(baseProject, newProject);
		setupTopBar(baseProject, newProject);

		JPanel leftSidePanel = new JPanel();
		getContentPane().add(leftSidePanel, BorderLayout.WEST);

		Box verticalBox = Box.createVerticalBox();
		leftSidePanel.add(verticalBox);
		
		btnNewButton_1= new JButton("Fade New");
		btnNewButton_1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(FadeNew)
				{
					btnNewButton_1.setText("Fade Base");
				}
				else
				{
					btnNewButton_1.setText("Fade Base");
				}
				FadeNew=!FadeNew;
				ev3DisplayPanel.updateFade(FadeNew);
				EV3Frame.this.requestFocus();
			}});
		verticalBox.add(btnNewButton_1);
		  JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);

		    slider.setMinorTickSpacing(2);
		    slider.setMajorTickSpacing(10);
		    slider.setPaintTicks(true);
		    slider.setPaintLabels(true);

		    // We'll just use the standard numeric labels for now...
		    slider.setLabelTable(slider.createStandardLabels(10));

		    verticalBox.add(slider);
		    slider.addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent changeEvent) {
					JSlider theJSlider = (JSlider) changeEvent.getSource();
					
					int NewOffset= theJSlider.getValue();

					ev3DisplayPanel.Strech(NewOffset);
					EV3Frame.this.requestFocus();
				}
		    	
		    });
		    slider.setFocusable(false);

		updateBase(baseProject);
		updateNew(newProject);
		setVisible(true);
		this.addKeyListener(keyListener);
		setFocusable(true);
		ev3DisplayPanel.setFocusable(true);
		this.toFront();
		requestFocus();

	}
	JLabel baseFileNameLabel, newFileNameLabel;
	
	public void setupTopBar(ProjectItem baseProject, ProjectItem newProject) {
		
		Box VerticalBaseBox = Box.createVerticalBox();
		Box VerticalNewBox = Box.createVerticalBox();
		Box horizontalBox = Box.createHorizontalBox();
		Box baseTopHlBox = Box.createHorizontalBox();
		Box newTopHlBox = Box.createHorizontalBox();
		
		getContentPane().add(horizontalBox, BorderLayout.NORTH);
		horizontalBox.add(VerticalBaseBox);
		Component verticalStrut = Box.createVerticalStrut(20);
		horizontalBox.add(verticalStrut);
		horizontalBox.add(VerticalNewBox);
		VerticalBaseBox.add(baseTopHlBox);
		VerticalNewBox.add(newTopHlBox);
		
		baseFileNameLabel = new JLabel("None");
		baseFileNameLabel.setDropTarget(baseFileDropTarget);
		VerticalBaseBox.add(baseFileNameLabel);

		newFileNameLabel = new JLabel("None");
		newFileNameLabel.setDropTarget(newFileDropTarget);
		VerticalNewBox.add(newFileNameLabel);
		
		baseProjectNameLabel = new JLabel("None");
		baseProjectNameLabel.setDropTarget(baseFileDropTarget);

		baseTopHlBox.add(baseProjectNameLabel);
	//	horizontalBox.add(baseFileNameLabel);
		baseFileProgramSpinner = new JComboBox<String>(baseProgramNames);
		baseFileProgramSpinner.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				int Index= baseFileProgramSpinner.getSelectedIndex();
				System.out.println("Index"+Index);
				ev3DisplayPanel.setBaseIndex(Index);
				EV3Frame.this.requestFocus();
			}});

		baseFileProgramSpinner.setDropTarget(baseFileDropTarget);

		baseTopHlBox.add(baseFileProgramSpinner);

		//	enableDragAndDrop();


		newProjectNameLabel = new JLabel("None");
		if(newProject.ProjectName!= null)
			newProjectNameLabel.setText(newProject.ProjectName+"   ");

		newProjectNameLabel.setDropTarget(newFileDropTarget);
		newTopHlBox.add(newProjectNameLabel);

		newFileProgramSpinner = new JComboBox<String>(newProgramNames);
		newFileProgramSpinner.setDropTarget(newFileDropTarget);
		newFileProgramSpinner.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				int Index= newFileProgramSpinner.getSelectedIndex();
				System.out.println("Index"+Index);
				ev3DisplayPanel.setNewIndex(Index);
				EV3Frame.this.requestFocus();
			}});
		newTopHlBox.add(newFileProgramSpinner);
	}
	public void setupPannel(ProjectItem baseProject, ProjectItem newProject) {
		ev3DisplayPanel = new EV3Panel(baseProject, newProject);
		getContentPane().add(ev3DisplayPanel, BorderLayout.CENTER);
		ev3DisplayPanel.setOpaque(true);
		ev3DisplayPanel.setSize(400, 400);
		ev3DisplayPanel.setBackground(Color.white);
	}
	private String[] getProgramNames(ArrayList<ZipFileEntry> programNameList) {
		if(programNameList==null || programNameList.size()==0)
			return new String[1];

		String[] programNames= new String[programNameList.size()];
		int i=0;
		for( ZipFileEntry program:programNameList)
		{
			programNames[i]= program.ProgramName;
			i++;
		}	

		return programNames;
	}



	KeyListener keyListener= new KeyListener(){
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub



		}
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub;
			int key= e.getKeyCode();
			if(key==KeyEvent.VK_PAGE_UP)
			{
				ev3DisplayPanel.Zoom(false);
			}
			else if(key==KeyEvent.VK_PAGE_DOWN)
			{
				ev3DisplayPanel.Zoom(true);
			}
			else if(key==KeyEvent.VK_LEFT||key==KeyEvent.VK_RIGHT ||key==KeyEvent.VK_UP||key==KeyEvent.VK_DOWN)
			{
				ev3DisplayPanel.Pan(key);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}
	};
}
