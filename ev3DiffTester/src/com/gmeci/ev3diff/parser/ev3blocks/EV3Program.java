package com.gmeci.ev3diff.parser.ev3blocks;

import java.util.ArrayList;

public class EV3Program {
	//describes a program - a PROJECT can have multiple programs. 
	//each program has a bunch of blocks

	public static final String HOW_LONG = "How\\ Long";

	public static final String LOOP_SUFFIX="Loop";
	public static final String LOOP_INFINITE="StopNever";
	public static final String MY_BLOCK_SUFFIX=".ev3p";
	public static final String START_BLOCK="StartBlock";
	public static final String MEDIUM_ROTATIONS="MediumMotorDistanceRotations";
	public static final String MEDIUM_STOP="MediumMotorStop";
	public static final String MEDIUM_START="MediumMotorUnlimited";
	public static final String MEDIUM_TIME="MediumMotorTime";
	public static final String MEDIUM_DEGREES="MediumMotorDistance";

	public static final String LOOP_TIME_COMPARE="TimeCompareLoop";

	public static final String LARGE_DISTANCE_ROTATIONS="MotorDistanceRotations";
	public static final String LARGE_STOP="MotorStop";
	public static final String LARGE_START="MotorUnlimited";
	public static final String LARGE_TIME="MotorTime";
	public static final String LARGE_DISTANCE_DEGREES="MotorDistance";


	public static final String MOVE_DISTANCE_ROTATIONS="MoveDistanceRotations";
	public static final String MOVE_STOP="MoveStop";
	public static final String MOVE_START="MoveUnlimited";
	public static final String MOVE_TIME="MoveTime";
	public static final String MOVE_DEGREES="MoveDistance";

	public static final String TANK_DISTANCE_ROTATIONS="MoveTankDistanceRotations";
	public static final String TANK_STOP="MoveTankStop";
	public static final String TANK_START="MoveTankMode";
	public static final String TANK_TIME="MoveTankTime";
	public static final String TANK_DEGREES="MoveTankDistance";

	public static final String PLAYSOUND_FILE="PlaySoundFile";
	public static final String PLAYSOUND_STOP="PlaySoundStop";

	public static final String LED_ON ="LedOn";
	public static final String LED_OFF="LedOff";

	public static final String DISPLAY_FILE="DisplayFile";
	public static final String DISPLAY_TEXT_GRID="DisplayTextGrid";
	
	//yellow stuff
	public static final String YELLOW_GYRO_DEGREES="GyroDegrees";
	public static final String YELLOW_BUTTON="ButtonValue";
	public static final String YELLOW_COLOR_VALUE="ColorValue";
	
	public String ProgramName;
	public ArrayList<EV3BlockItem> programBlocks=new ArrayList<EV3BlockItem>();

}
