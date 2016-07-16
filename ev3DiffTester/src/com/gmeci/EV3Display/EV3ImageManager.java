package com.gmeci.EV3Display;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.gmeci.ev3diff.parser.ev3blocks.EV3Program;

public class EV3ImageManager {

	static final String TOGGLE_INTERRUPT = "ToggleInterrupt";
	public static final String WAIT = "wait";
	public static final String LOOP = "loop";
	BufferedImage startImage,myBlockImage, unknownImage;
	BufferedImage largeMotorRotationsImage, mediumMotorRotationsImage, steerRotationsImage, tankRotationsImage, lightsImage, soundImage;
	BufferedImage largeMotorDegreesImage, mediumDegreesImage, steerDegreesImage,tankDegreesImage;
	BufferedImage DisplayFile, DisplayTextGrid,DisplayStringGrid;
	BufferedImage PlaySoundFile, PlaySoundStop;
	BufferedImage LedOn, LedOff;
	BufferedImage Yellow_Color, Yellow_GyroDeg, Yellow_Button, Yellow_IRSeek, Yellow_MotorRotations_degrees, Yellow_Timer_Time, Yellow_Touch,Yellow_Ultrasonic;
	BufferedImage Red_VariableSet,Red_ConstanteRead,Red_MathAdd,Red_CompareEquals;
	BufferedImage Blue_ReverseMotor,Blue_RaweMotor;
	BufferedImage largeMotorOffImage, mediumOffImage, steerOffImage, tankOffImage;
	BufferedImage largeMotorOnImage, mediumOnImage, steerOnImage,tankOnImage;
	BufferedImage largeMotorTimeImage, mediumTimeImage, steerTimeImage, tankTimeImage ;
	BufferedImage loopLeftImage, loopTimeCompareImage, loopInfiniteImage,loopButtonCompareImage,loopColorCompareImage,loopGyroAngleCompareImage,loopGyroRateCompareImage,loopLightCompareImage;
	BufferedImage wait_color,interrupt,wait_button,wait_GyroAngle,wait_light,wait_GyroRate;
	
	HashMap<String, BufferedImage> ImageNameMap = new HashMap<String, BufferedImage>();
	
	
	public void LoadImages()
	{
		try {                

			System.out.println("Reading Images:");
			//Image image = ImageIO.read(  );
			unknownImage = ImageIO.read(getClass().getResource( "/resources/unknown.PNG" ));
			System.out.println(("res/unknown.png"));
			System.out.println("Reading unknown done");
			startImage = ImageIO.read(getClass().getResource( "/resources/start.png" ));
			ImageNameMap.put(EV3Program.START_BLOCK,  startImage);
			System.out.println(("resources/start.png"));
			myBlockImage = ImageIO.read(getClass().getResource( "/resources/myblock.PNG" ));
			ImageNameMap.put(EV3Program.MY_BLOCK_SUFFIX, myBlockImage);
			System.out.println(("resources/myblock.png"));

			largeMotorRotationsImage = ImageIO.read(getClass().getResource( "/resources/large_rotations.png" ));
			ImageNameMap.put(EV3Program.LARGE_DISTANCE_ROTATIONS, largeMotorRotationsImage);
			largeMotorOnImage = ImageIO.read(getClass().getResource( "/resources/large_on.PNG" ));
			ImageNameMap.put(EV3Program.LARGE_START, largeMotorOnImage);
			largeMotorOffImage = ImageIO.read(getClass().getResource( "/resources/large_off.PNG" ));
			ImageNameMap.put(EV3Program.LARGE_STOP, largeMotorOffImage);
			largeMotorDegreesImage = ImageIO.read(getClass().getResource( "/resources/large_degrees.PNG" ));
			ImageNameMap.put(EV3Program.LARGE_DISTANCE_DEGREES, largeMotorRotationsImage);
			largeMotorTimeImage = ImageIO.read(getClass().getResource( "/resources/large_time.PNG" ));
			ImageNameMap.put(EV3Program.LARGE_TIME, largeMotorTimeImage);
			System.out.println(("/resources/large_rotations.PNG"));

			mediumMotorRotationsImage = ImageIO.read(getClass().getResource( "/resources/medium_rotations.png" ));
			ImageNameMap.put(EV3Program.MEDIUM_ROTATIONS, mediumMotorRotationsImage);
			System.out.println(("res/medium_rotations.png"));
			mediumOnImage = ImageIO.read(getClass().getResource( "/resources/medium_on.PNG" ));
			ImageNameMap.put(EV3Program.MEDIUM_START, mediumOnImage);
			System.out.println(("res/medium_on.png"));
			mediumOffImage = ImageIO.read(getClass().getResource( "/resources/medium_off.PNG" ));
			ImageNameMap.put(EV3Program.MEDIUM_STOP, mediumOffImage);
			System.out.println(("res/medium_off.png"));
			mediumDegreesImage = ImageIO.read(getClass().getResource( "/resources/medium_degrees.PNG" ));
			ImageNameMap.put(EV3Program.MEDIUM_DEGREES, mediumDegreesImage);
			System.out.println(("res/medium_degrees.png"));
			mediumTimeImage = ImageIO.read(getClass().getResource( "/resources/medium_time.PNG" ));
			ImageNameMap.put(EV3Program.MEDIUM_TIME, mediumTimeImage);
			System.out.println(("res/medium_degrees.png"));

			steerRotationsImage = ImageIO.read(getClass().getResource( "/resources/steer.png" ));
			ImageNameMap.put(EV3Program.MOVE_DISTANCE_ROTATIONS, steerRotationsImage);
			steerOnImage = ImageIO.read(getClass().getResource( "/resources/steer_on.PNG" ));
			ImageNameMap.put(EV3Program.MOVE_START, steerOnImage);
			steerOffImage = ImageIO.read(getClass().getResource( "/resources/steer_off.PNG" ));
			ImageNameMap.put(EV3Program.MOVE_STOP, steerOffImage);
			steerTimeImage = ImageIO.read(getClass().getResource( "/resources/steer_time.PNG" ));
			ImageNameMap.put(EV3Program.MOVE_TIME, steerTimeImage);
			steerDegreesImage = ImageIO.read(getClass().getResource( "/resources/steer_degrees.PNG" ));
			ImageNameMap.put(EV3Program.MOVE_DEGREES, steerDegreesImage);			


			tankRotationsImage = ImageIO.read(getClass().getResource( "/resources/tank.png" ));
			ImageNameMap.put(EV3Program.TANK_DISTANCE_ROTATIONS, tankRotationsImage);
			tankOnImage = ImageIO.read(getClass().getResource( "/resources/tank_on.PNG" ));
			ImageNameMap.put(EV3Program.TANK_START, tankOnImage);
			tankOffImage = ImageIO.read(getClass().getResource( "/resources/tank_off.PNG" ));
			ImageNameMap.put(EV3Program.TANK_STOP, tankOffImage);
			tankTimeImage = ImageIO.read(getClass().getResource( "/resources/tank_time.PNG" ));
			ImageNameMap.put(EV3Program.TANK_TIME, tankTimeImage);
			tankDegreesImage = ImageIO.read(getClass().getResource( "/resources/tank_degrees.PNG" ));
			ImageNameMap.put(EV3Program.TANK_DEGREES, tankDegreesImage);			

			DisplayFile = ImageIO.read(getClass().getResource( "/resources/display_file.PNG" ));
			ImageNameMap.put(EV3Program.DISPLAY_FILE, DisplayFile);		
			DisplayTextGrid = ImageIO.read(getClass().getResource( "/resources/display_text_grid.PNG" ));
			ImageNameMap.put(EV3Program.DISPLAY_TEXT_GRID, DisplayTextGrid);		

			DisplayStringGrid = ImageIO.read(getClass().getResource( "/resources/display_text_grid.PNG" ));
			ImageNameMap.put("DisplayStringGrid", DisplayStringGrid);	

			PlaySoundFile = ImageIO.read(getClass().getResource( "/resources/playsound_file.PNG" ));
			ImageNameMap.put(EV3Program.PLAYSOUND_FILE, PlaySoundFile);		
			PlaySoundStop = ImageIO.read(getClass().getResource( "/resources/playsound_stop.PNG" ));
			ImageNameMap.put(EV3Program.PLAYSOUND_STOP, PlaySoundStop);			

			LedOn = ImageIO.read(getClass().getResource( "/resources/led_on.PNG" ));
			ImageNameMap.put(EV3Program.LED_ON, LedOn);		
			LedOff = ImageIO.read(getClass().getResource( "/resources/led_off.PNG" ));
			ImageNameMap.put(EV3Program.LED_OFF, LedOff);			
	
			PlaySoundFile = ImageIO.read(getClass().getResource( "/resources/playsound_file.PNG" ));
			ImageNameMap.put(EV3Program.PLAYSOUND_FILE, PlaySoundFile);		
			PlaySoundStop = ImageIO.read(getClass().getResource( "/resources/playsound_stop.PNG" ));
			ImageNameMap.put(EV3Program.PLAYSOUND_STOP, PlaySoundStop);			
			
			loopLeftImage = ImageIO.read(getClass().getResource( "/resources/loop_left.PNG" ));
			loopInfiniteImage = ImageIO.read(getClass().getResource( "/resources/loop_right_time.PNG" ));
			ImageNameMap.put("StopNever", loopInfiniteImage);	

			loopTimeCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_infinite.PNG" ));
			ImageNameMap.put(EV3Program.LOOP_TIME_COMPARE, loopTimeCompareImage);	
			loopButtonCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_buttons.PNG" ));
			ImageNameMap.put("ButtonCompare"+LOOP, loopButtonCompareImage);	
			loopColorCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_color.PNG" ));
			ImageNameMap.put("ColorCompare"+LOOP, loopColorCompareImage);
			loopGyroAngleCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_angle.PNG" ));
			ImageNameMap.put("GyroAngleCompare"+LOOP, loopGyroAngleCompareImage);	
			loopGyroRateCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_rate.PNG" ));
			ImageNameMap.put("GyroRateCompare"+LOOP, loopGyroRateCompareImage);	
			loopLightCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_light.PNG" ));
			ImageNameMap.put("ColorReflectedIntensityCompare"+LOOP, loopLightCompareImage);	
			BufferedImage loopTouchCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_touch.PNG" ));
			ImageNameMap.put("TouchCompare"+LOOP, loopTouchCompareImage);	
			BufferedImage loopMotorRotationCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_motor_rotation.PNG" ));
			ImageNameMap.put("RotationDegreesCompare"+LOOP, loopMotorRotationCompareImage);	
			BufferedImage loopUltrasonicCMImage = ImageIO.read(getClass().getResource( "/resources/loop_right_ultrasonic.PNG" ));
			ImageNameMap.put("DistanceCMCompare"+LOOP, loopUltrasonicCMImage);	
			BufferedImage loopMotorRotationRotationsCompareImage = ImageIO.read(getClass().getResource( "/resources/loop_right_rotations_rotations.PNG" ));
			ImageNameMap.put("RotationRotationsCompare"+LOOP, loopMotorRotationRotationsCompareImage);
			BufferedImage loopCountImage = ImageIO.read(getClass().getResource( "/resources/loop_right_count.PNG" ));
			ImageNameMap.put("StopAfterNumberIterations"+LOOP, loopCountImage);	
			BufferedImage loopUltrasonicINImage = ImageIO.read(getClass().getResource( "/resources/loop_right_distance_in.PNG" ));
			ImageNameMap.put("DistanceInchesCompare"+LOOP, loopUltrasonicINImage);
			

			wait_color = ImageIO.read(getClass().getResource( "/resources/wait_color_compare.PNG" ));
			ImageNameMap.put("ColorCompare"+WAIT, wait_color);	
			wait_button = ImageIO.read(getClass().getResource( "/resources/wait_button.PNG" ));
			ImageNameMap.put("ButtonCompare"+WAIT, wait_button);	
			wait_GyroAngle = ImageIO.read(getClass().getResource( "/resources/wait_gyro_angle.PNG" ));
			ImageNameMap.put("GyroAngleCompare"+WAIT, wait_GyroAngle);	
			wait_light = ImageIO.read(getClass().getResource( "/resources/wait_gyro_angle.PNG" ));
			ImageNameMap.put("ColorReflectedIntensityCompare"+WAIT, wait_light);	
			wait_GyroRate = ImageIO.read(getClass().getResource( "/resources/wait_gyro_angle.PNG" ));
			ImageNameMap.put("DeltaWaitGyroRate"+WAIT, wait_GyroRate);	
			BufferedImage wait_touch = ImageIO.read(getClass().getResource( "/resources/wait_touch.PNG" ));
			ImageNameMap.put("TouchCompare"+WAIT,wait_touch);	
			BufferedImage wait_motor_rotation = ImageIO.read(getClass().getResource( "/resources/wait_motor_rotation.PNG" ));
			ImageNameMap.put("RotationDegreesCompare"+WAIT, wait_motor_rotation);	
			BufferedImage wait_ultrasonic_cm = ImageIO.read(getClass().getResource( "/resources/wait_ultrasonic_cm.PNG" ));
			ImageNameMap.put("DistanceCMCompare"+WAIT,wait_ultrasonic_cm);	
			BufferedImage wait_motor_rotation_rotation = ImageIO.read(getClass().getResource( "/resources/wait_motor_rotation.PNG" ));
			ImageNameMap.put("RotationRotationsCompare"+WAIT, wait_motor_rotation_rotation);	
			BufferedImage wait_time = ImageIO.read(getClass().getResource( "/resources/wait_time.PNG" ));
			ImageNameMap.put("TimeCompare"+WAIT,wait_time);	
			BufferedImage wait_distance_inches = ImageIO.read(getClass().getResource( "/resources/wait_ultrasonic_in.PNG" ));
			ImageNameMap.put("DistanceInchesCompare"+WAIT, wait_distance_inches);	
			
			
			interrupt = ImageIO.read(getClass().getResource( "/resources/interrupt.PNG" ));
			ImageNameMap.put(TOGGLE_INTERRUPT, interrupt);	
			
			Yellow_GyroDeg = ImageIO.read(getClass().getResource( "/resources/gyro_degrees.PNG" ));
			ImageNameMap.put(EV3Program.YELLOW_GYRO_DEGREES, Yellow_GyroDeg);			
			Yellow_Button = ImageIO.read(getClass().getResource( "/resources/button_value.PNG" ));
			ImageNameMap.put(EV3Program.YELLOW_BUTTON, Yellow_Button);			
			Yellow_Color = ImageIO.read(getClass().getResource( "/resources/color_value.PNG" ));
			ImageNameMap.put(EV3Program.YELLOW_COLOR_VALUE, Yellow_Color);	
			Yellow_IRSeek = ImageIO.read(getClass().getResource( "/resources/infrared.PNG" ));
			ImageNameMap.put("IRSeeker", Yellow_IRSeek);			
			Yellow_MotorRotations_degrees = ImageIO.read(getClass().getResource( "/resources/motor_rotation_degrees.PNG" ));
			ImageNameMap.put("RotationValue", Yellow_MotorRotations_degrees);		
			Yellow_Touch = ImageIO.read(getClass().getResource( "/resources/touch_sensor.PNG" ));
			ImageNameMap.put("TouchValue", Yellow_Touch);		
			Yellow_Timer_Time = ImageIO.read(getClass().getResource( "/resources/timer_time.PNG" ));
			ImageNameMap.put("TimerValue", Yellow_Timer_Time);
			Yellow_Ultrasonic = ImageIO.read(getClass().getResource( "/resources/ultrasonic_cm.PNG" ));
			ImageNameMap.put("DistanceCM", Yellow_Ultrasonic);			

			Red_VariableSet = ImageIO.read(getClass().getResource( "/resources/variable_setSingle.PNG" ));
			ImageNameMap.put("GlobalSetSingle", Red_VariableSet);	
			BufferedImage RedGetString  = ImageIO.read(getClass().getResource( "/resources/var_get_string.PNG" ));
			ImageNameMap.put("GlobalGetString", RedGetString);
			BufferedImage RedGetSingle  = ImageIO.read(getClass().getResource( "/resources/var_get_number.PNG" ));
			ImageNameMap.put("GlobalGetSingle", RedGetSingle);
			BufferedImage RedGetBoolean  = ImageIO.read(getClass().getResource( "/resources/var_get_bool.PNG" ));
			ImageNameMap.put("GlobalGetBoolean", RedGetBoolean);
			BufferedImage RedSetString  = ImageIO.read(getClass().getResource( "/resources/var_set_string.PNG" ));
			ImageNameMap.put("GlobalSetString", RedSetString);
			BufferedImage RedSetBoolean  = ImageIO.read(getClass().getResource( "/resources/var_set_bool.PNG" ));
			ImageNameMap.put("GlobalSetBoolean", RedSetBoolean);

			

			Red_ConstanteRead = ImageIO.read(getClass().getResource( "/resources/variable_setSingle.PNG" ));
			ImageNameMap.put("GlobalConstSingle", Red_ConstanteRead);		
			BufferedImage Red_ConstanteBoolean = ImageIO.read(getClass().getResource( "/resources/const_bool.PNG" ));
			ImageNameMap.put("GlobalConstBoolean", Red_ConstanteBoolean);		
			BufferedImage Red_ConstanteString = ImageIO.read(getClass().getResource( "/resources/const_string.PNG" ));
			ImageNameMap.put("GlobalConstString", Red_ConstanteString);		
			
			Red_MathAdd = ImageIO.read(getClass().getResource( "/resources/arithmetic_add.PNG" ));
			ImageNameMap.put("Arithmetic_Add", Red_MathAdd);	
			BufferedImage RedMult  = ImageIO.read(getClass().getResource( "/resources/arithmetic_multiply.PNG" ));
			ImageNameMap.put("Arithmetic_Multiply", RedMult);	
			BufferedImage RedDiv  = ImageIO.read(getClass().getResource( "/resources/arithmetic_divide.PNG" ));
			ImageNameMap.put("Arithmetic_Divide", RedDiv);
			BufferedImage RedSub  = ImageIO.read(getClass().getResource( "/resources/arithmetic_subtract.PNG" ));
			ImageNameMap.put("Arithmetic_Subtract", RedSub);
			BufferedImage RedAbs  = ImageIO.read(getClass().getResource( "/resources/arithmetic_abs.PNG" ));
			ImageNameMap.put("Arithmetic_AbsoluteValue", RedAbs);
			BufferedImage RedSqrt  = ImageIO.read(getClass().getResource( "/resources/arithmetic_sqrt.PNG" ));
			ImageNameMap.put("Arithmetic_SquareRoot", RedSqrt);
			BufferedImage RedPow  = ImageIO.read(getClass().getResource( "/resources/arithmetic_pow.PNG" ));
			ImageNameMap.put("Arithmetic_Power", RedPow);

			

			Red_CompareEquals = ImageIO.read(getClass().getResource( "/resources/compare_equals.PNG" ));
			ImageNameMap.put("Comparison_Equal", Red_CompareEquals);	
			BufferedImage Red_CompareNotEquals = ImageIO.read(getClass().getResource( "/resources/compare_notequals.PNG" ));
			ImageNameMap.put("Comparison_NotEqual", Red_CompareNotEquals);	
			BufferedImage Red_CompareLess = ImageIO.read(getClass().getResource( "/resources/compare_less.PNG" ));
			ImageNameMap.put("Comparison_Less", Red_CompareLess);	
			BufferedImage Red_CompareGreater = ImageIO.read(getClass().getResource( "/resources/compare_greater.PNG" ));
			ImageNameMap.put("Comparison_Greater", Red_CompareGreater);	
			BufferedImage Red_CompareGreaterEqual = ImageIO.read(getClass().getResource( "/resources/compare_greaterequals.PNG" ));
			ImageNameMap.put("Comparison_GreaterEqual", Red_CompareGreaterEqual);	
			BufferedImage Red_CompareLessEqual = ImageIO.read(getClass().getResource( "/resources/compare_lessequals.PNG" ));
			ImageNameMap.put("Comparison_LessEqual", Red_CompareLessEqual);	
			

			Blue_ReverseMotor = ImageIO.read(getClass().getResource( "/resources/reverse_motor.PNG" ));
			ImageNameMap.put("InvertMotor", Blue_ReverseMotor);	
			Blue_RaweMotor = ImageIO.read(getClass().getResource( "/resources/unregulated_motor.PNG" ));
			ImageNameMap.put("RawMotorOn", Blue_RaweMotor);	
			
//			
		//	loopInfiniteImage = ImageIO.read(getClass().getResource( "/resources/loop_right_time.PNG" ));
		//	ImageNameMap.put(EV3Program.LOOP_INFINITE, loopInfiniteImage);	

			System.out.println("Successfully read images\n");

		} catch (Exception ex) {
			// handle exception...
			System.out.println("Faild to read images!!!!!!!!!!!!!!!!!!!!!\n"+ ex.toString());
		}
	}


	public BufferedImage scaleImage(BufferedImage baseImage, float scale)
	{
		int imgWidth = baseImage.getWidth();
		int imgHeight = baseImage.getHeight();

		BufferedImage newImage = new BufferedImage( (int)(imgWidth*scale), (int)(imgHeight*scale), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = newImage.createGraphics();
		try {
			g.setBackground(Color.white);
			g.clearRect(0, 0, (int)(imgWidth*scale), (int)(imgHeight*scale));
			g.drawImage(baseImage, 0, 0,  (int)(imgWidth*scale), (int)(imgHeight*scale), null);
		} finally {
			g.dispose();
		}
		return newImage;
	}

	
	boolean imageAvailable(String TargetName)
	{
		return ImageNameMap.containsKey(TargetName);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	float panOffsetX, panOffsetY;

	float strechValue;
	public void setStretch(float strectchValue) {
		this.strechValue = strectchValue;
		
	}

	Point MouseDownStart;
	public void setPanBase(Point p)
	{
		MouseDownStart=p;
	}

	int PanOffsetX, PanOffsetY;
	public void updatePanOffset(Point p) {
		PanOffsetX+= p.x-MouseDownStart.x;
		PanOffsetY+= p.y-MouseDownStart.y;
		
		MouseDownStart.x = p.x;
		MouseDownStart.y = p.y;
		System.out.println("panning "+ PanOffsetX+" "+PanOffsetY);
	}


	public int getPanXOffset() {
		// TODO Auto-generated method stub
		return PanOffsetX;
	}


	public int getPanYOffset() {
		// TODO Auto-generated method stub
		return PanOffsetY;
	}

	private static final int PAN_OFFSET = 20;
	public void Pan(int key) {
		if(key==KeyEvent.VK_LEFT)
		{
			this.PanOffsetX-=PAN_OFFSET;
		}
		if(key==KeyEvent.VK_RIGHT)
		{
			this.PanOffsetX+=PAN_OFFSET;
		}
		if(key==KeyEvent.VK_UP)
		{
			this.PanOffsetY-=PAN_OFFSET;
		}
		if(key==KeyEvent.VK_DOWN)
		{
			this.PanOffsetY+=PAN_OFFSET;
		}
	}


}
