import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;

// If using, use for educational purposes only and don't remove the following line
// Tyler Adams - Friday June 7th, 2013
// 3D Connect Four (6 2D faces) with matrices

//	:::::TASK:::::
// make paint component shorter

// :::::NOTES:::::
// added cool cross polygon
// added font
// for 4<6 loop, commented out rotate(0)
// eliminated rotate(0) for second set of faces
// FIXED GLITCH: on a different face, reds only display after a random number of invisible reds have been placed
// may do 2 seperate textfiles one for all the front facing ones, another for all the side facing ones
// paint may be updating faster than everything
// not display the opposite face (back face) when you're on the front face
// bring back panel to the front or ignore...

// :::::::::::BUG FIXES::::::::::::::::
// THURSDAY BUG: not displaying reds because of incorrect placement of polygon counter and point counter for while loops
// GLITCH: something wrong with angle 2 manipulation at end of for loop because it is placing wrong
// had +angle issues at the bottom, fixed after re-thinking theory
// use sangle theory to generate seperate angles for different red/blue sprites
// added sangle stuff isn't working
// angle 2 + 90 and then angle 2 + 270 and then angle 2=0
// dont think i need an array of arraylists for sprites/polygons
// convert to multiple classes because all boards have equal values at the moment
public class integrate extends JPanel{
	static boolean skip =false;				// boolean to allow a certain sprite to be skipped
	static boolean skip2=false;				// second version of above boolean for different sprites
	//static int countdrac=0;
	boolean win=false;
	static Matrix CTM; // global CTM to be used in paint method
	private static JFrame frame = new JFrame("Connect 4 by Tyler Adams");
	static ArrayList frontsprite = new ArrayList(0);			// only one cube sprite
	static ArrayList sidesprite = new ArrayList(0);			// only one cube sprite
	static ArrayList redsprite[] = new ArrayList[6]; 	// seperate sprites for each instance
	static ArrayList bluesprite[] = new ArrayList[6];
	static ArrayList tempred[] = new ArrayList[6]; 	// temporary storage sprites for each instance for only showing one side
	static ArrayList tempblue[] = new ArrayList[6];

	public static connect[] instance = new connect[6]; 	// seperate game instances
	static int iCurrent = 0;							// keep track of which instance
	static integrate panel;								// container for the jframe
	static boolean selector= false;						// in case I want to make the selector disappear
	static int multiplier = 60;							// scale value
	static int sx=585;									// selector x position
	static int sy=280;									// selector y position
	static int sprx = 600;								// sprite shifter for x/y/z values
	static int spry = 500;					
	static int sprz = 0;
	static double xtheta = 0;							 // for x axis rotation
	static double ytheta = 0;							 // for y axis rotation

	public static void main(String[] args) throws IOException {
		panel = new integrate();								// create the container that holds this class
		frame.setSize(1000, 800);
		frame.setLocation(0, 0);
		frame.add(panel);										// add the container to the screen
		frame.setVisible(true);									// allow the screen to be visible
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.init();											// add a key listener to the frame, can't do this in main method so must be called elsewhere


		for (int x = 0; x < 6; x++) { // initialize instanced arrays
			instance[x] = new connect(); 
			redsprite[x] = new ArrayList(0);
			bluesprite[x] = new ArrayList(0);
		}

		FileReader fr = new FileReader("3dboardFront.txt");			// read board files for 3-d imaging
		BufferedReader br = new BufferedReader(fr);
		StringTokenizer st;
		String read=br.readLine();
		while (read != null) {									// while there are still lines to be read
			ArrayList polygon = new ArrayList(0);
			st = new StringTokenizer(read, " ");
			point current;
			//	st.nextToken(); // need one token in the beginning (polygon)
			while (st.hasMoreTokens()) {						// while there are still characters in this line
				///*
				current = new point(Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()) ,Double.parseDouble(st.nextToken()) );	// read a point
				//*/
				polygon.add(current);							// add this point to a polygon
			}
			frontsprite.add(polygon);								// add the polygon to the frontsprite
			read = br.readLine();								// get a new line
		}
		br.close();												
		fr.close();												// stop reading from a file


		FileReader fr2 = new FileReader("3dboardSide.txt");			// read2 board files for 3-d imaging
		BufferedReader br2 = new BufferedReader(fr2);
		StringTokenizer st2;
		String read2=br2.readLine();
		while (read2 != null) {									// while there are still lines to be read2
			ArrayList polygon = new ArrayList(0);
			st2 = new StringTokenizer(read2, " ");
			point current;
			//	st2.nextToken(); // need one token in the beginning (polygon)
			while (st2.hasMoreTokens()) {						// while there are still characters in this line
				///*
				current = new point(Double.parseDouble(st2.nextToken()),Double.parseDouble(st2.nextToken()) ,Double.parseDouble(st2.nextToken()) );	// read2 a point
				//*/
				polygon.add(current);							// add this point to a polygon
			}
			sidesprite.add(polygon);								// add the polygon to the frontsprite
			read2 = br2.readLine();								// get a new line
		}
		br2.close();												
		fr2.close();												// stop reading from a file

	}

	public static void rotate(int axis) {
		double[][] RO = new double[4][4];
		if (axis == 0) {
			RO[0][0] = 1;
			RO[1][1] = Math.cos(xtheta);
			RO[2][1] = -Math.sin(xtheta);
			RO[1][2] = Math.sin(xtheta);
			RO[2][2] = Math.cos(xtheta);
			RO[3][3] = 1;
		}
		if (axis == 1) {
			RO[1][1] = 1;
			RO[0][0] = Math.cos(ytheta);
			RO[2][0] = -Math.sin(ytheta);
			RO[0][2] = Math.sin(ytheta);
			RO[2][2] = Math.cos(ytheta);
			RO[3][3] = 1;
		}
		CTM = CTM.multiply(RO);
	}

	public static void scale() {
		double[][] SC = new double[4][4];
		SC[0][0] = multiplier;
		SC[1][1] = multiplier;
		SC[2][2] = multiplier;
		SC[3][3] = 1;
		CTM = CTM.multiply(SC);
	}

	public static void translate() {
		double[][] TR = new double[4][4];
		TR[0][0] = 1;
		TR[3][0] = sprx;
		TR[1][1] = 1;
		TR[3][1] = spry;
		TR[2][2] = 1;
		TR[3][2] = sprz;
		TR[3][3] = 1;
		CTM = CTM.multiply(TR);
	}

	public void init() {
		frame.addKeyListener(new KeyInputHandler());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);			// paint on this container
		
		Font font = new Font("MSReferenceSpecialty",Font.PLAIN,18);
		g.setFont(font);

		if(instance[iCurrent].won==true){
			g.drawString("Player "+instance[iCurrent].winplayer+ " has won instance "+(iCurrent+1)+"!", sprx-90, 180);
			
		}
		
		
		CTM = new Matrix();					// create a new CTM
		rotate(0);							// allow rotation with axis 0
		rotate(1);							// allow rotation with axis 1
		scale();							// multiply the points to make them visible
		translate();						// shift the points to make them visible
		int polynum = 0;					// counter to count the number of polygons in a frontsprite
		/*	
		 	System.out.println("ABOUT TO START PRINTING frontsprite TEXT: ");
			System.out.println("");
			System.out.println("");
			System.out.println("");
			System.out.println("");
		 */

		while (frontsprite.size() > polynum) {	// while the total amount of polygons in a frontsprite have not been read yet (because polynum will increase after one has been read)
			int rcounter=0; 				// counter for text file rotate fixes

			int pctr = 0;					// counter to count the number of points in a polygon
			int wrap = 1;
			while (((ArrayList) frontsprite.get(polynum)).size() > pctr) {	// while the total number of points in a polygon have not been read yet (because pctr will increase after one has been read)
				if (((ArrayList) frontsprite.get(polynum)).size() - 1 == pctr){ 	// if you're on the last point of the polygon
					wrap=pctr*-1;													// set the wrapper to go back to the beginning while still incrementing pctr
				}
				double x1 = (double) (((point) ((ArrayList) frontsprite.get(polynum)).get(pctr)).returnx());	// get the first x value from the current point of the current polygon
				double y1 = (double) (((point) ((ArrayList) frontsprite.get(polynum)).get(pctr)).returny()); // get the first y value from the current point of the current polygon
				double z1 = (double) (((point) ((ArrayList) frontsprite.get(polynum)).get(pctr)).returnz()); // get the first z value from the current point of the current polygon
				double x2 = (double) (((point) ((ArrayList) frontsprite.get(polynum)).get(pctr + wrap)).returnx()); // get the second x value from the current point of the current polygon
				double y2 = (double) (((point) ((ArrayList) frontsprite.get(polynum)).get(pctr + wrap)).returny()); // get the second y value from the current point of the current polygon
				double z2 = (double) (((point) ((ArrayList) frontsprite.get(polynum)).get(pctr + wrap)).returnz()); // get the second z value from the current point of the current polygon

				Matrix oldv1 = new Matrix(x1, y1, z1);				// create a matrix containing the first set of old points
				Matrix oldv2 = new Matrix(x2, y2, z2);				// create a matrix containing the second set of old points
				/*if(rcounter==0){
					xtheta+=Math.PI/2;
					System.out.println("xtheta + 90: "+xtheta);
				}
				if(rcounter==29){
					System.out.println("xtheta - 90: "+xtheta);
					//rcounter=9999;
				}
				System.out.println("Increment check: "+rcounter);
				rcounter++;
				 */
				Matrix newv1 = oldv1.multiply(CTM.array());		// multiply these sets of old points to get new points
				Matrix newv2 = oldv2.multiply(CTM.array());

				g.drawLine((int)(newv1.array()[0][0]),(int)(newv1.array()[0][1]),(int)(newv2.array()[0][0]),(int)(newv2.array()[0][1])); // draw the new co-ordinates x1,y1,x2,y2
				/*
				DecimalFormat dtime = new DecimalFormat("#.##"); 				
				x1= (newv1.array()[0][0]-sprx);
				x1=x1/60;
				x1= Double.valueOf(dtime.format(x1));

				y1= (newv1.array()[0][1]-spry);
				y1=y1/60;
				y1= Double.valueOf(dtime.format(y1));

				z1= (newv1.array()[0][2]);
				z1=z1/60;
				z1= Double.valueOf(dtime.format(z1));

				x2= (newv2.array()[0][0]-sprx);
				x2=x2/60;
				x2= Double.valueOf(dtime.format(x2));

				y2= (newv2.array()[0][1]-spry);
				y2=y2/60;
				y2= Double.valueOf(dtime.format(y2));

				z2= (newv2.array()[0][2]);
				z2=z2/60;
				z2= Double.valueOf(dtime.format(z2));



				//newv1.array()[0][0]-sprx/60
				//System.out.println((x1)+" "+(int)newv1.array()[0][1]/60+" " + (int)newv1.array()[0][2]/60 +" "+(int)newv2.array()[0][0]/60+" "+(int)newv2.array()[0][1]/60+" "+(int)newv2.array()[0][2]/60);

				// every even one isn't correct
				countdrac++;
				if(countdrac%2==1){
					System.out.println(x1+" "+y1+" " + z1 +" "+x2+" "+y2+" "+z2);
				}
				 */
				pctr++; // increase the number of points that have been read

			}

			polynum++; // increase the number of polygons that have been read
		}

		polynum=0;
		CTM = new Matrix();					// create a new CTM
		//rotate(0);							// allow rotation with axis 0
		rotate(1);							// allow rotation with axis 1
		scale();							// multiply the points to make them visible
		translate();						// shift the points to make them visible

		// may have to add more matrices

		while (sidesprite.size() > polynum) {	// while the total amount of polygons in a sidesprite have not been read yet (because polynum will increase after one has been read)
			int rcounter=0; 				// counter for text file rotate fixes

			int pctr = 0;					// counter to count the number of points in a polygon
			int wrap = 1;
			while (((ArrayList) sidesprite.get(polynum)).size() > pctr) {	// while the total number of points in a polygon have not been read yet (because pctr will increase after one has been read)
				if (((ArrayList) sidesprite.get(polynum)).size() - 1 == pctr){
					wrap=pctr*-1;													// set the wrapper to go back to the beginning while still incrementing pctr
				}
				double x1 = (double) (((point) ((ArrayList) sidesprite.get(polynum)).get(pctr)).returnx());	// get the first x value from the current point of the current polygon
				double y1 = (double) (((point) ((ArrayList) sidesprite.get(polynum)).get(pctr)).returny()); // get the first y value from the current point of the current polygon
				double z1 = (double) (((point) ((ArrayList) sidesprite.get(polynum)).get(pctr)).returnz()); // get the first z value from the current point of the current polygon
				double x2 = (double) (((point) ((ArrayList) sidesprite.get(polynum)).get(pctr + wrap)).returnx()); // get the second x value from the current point of the current polygon
				double y2 = (double) (((point) ((ArrayList) sidesprite.get(polynum)).get(pctr + wrap)).returny()); // get the second y value from the current point of the current polygon
				double z2 = (double) (((point) ((ArrayList) sidesprite.get(polynum)).get(pctr + wrap)).returnz()); // get the second z value from the current point of the current polygon

				Matrix oldv1 = new Matrix(x1, y1, z1);				// create a matrix containing the first set of old points
				Matrix oldv2 = new Matrix(x2, y2, z2);				// create a matrix containing the second set of old points
				/*if(rcounter==0){
					xtheta+=Math.PI/2;
					System.out.println("xtheta + 90: "+xtheta);
				}
				if(rcounter==29){
					System.out.println("xtheta - 90: "+xtheta);
					//rcounter=9999;
				}
				System.out.println("Increment check: "+rcounter);
				rcounter++;
				 */
				Matrix newv1 = oldv1.multiply(CTM.array());		// multiply these sets of old points to get new points
				Matrix newv2 = oldv2.multiply(CTM.array());

				g.drawLine((int)(newv1.array()[0][0]),(int)(newv1.array()[0][1]),(int)(newv2.array()[0][0]),(int)(newv2.array()[0][1])); // draw the new co-ordinates x1,y1,x2,y2

				/*
				DecimalFormat dtime = new DecimalFormat("#.##"); 
				x1= (newv1.array()[0][0]-sprx);
				x1=x1/60;
				x1= Double.valueOf(dtime.format(x1));

				y1= (newv1.array()[0][1]-spry);
				y1=y1/60;
				y1= Double.valueOf(dtime.format(y1));

				z1= (newv1.array()[0][2]);
				z1=z1/60;
				z1= Double.valueOf(dtime.format(z1));

				x2= (newv2.array()[0][0]-sprx);
				x2=x2/60;
				x2= Double.valueOf(dtime.format(x2));

				y2= (newv2.array()[0][1]-spry);
				y2=y2/60;
				y2= Double.valueOf(dtime.format(y2));

				z2= (newv2.array()[0][2]);
				z2=z2/60;
				z2= Double.valueOf(dtime.format(z2));



				//newv1.array()[0][0]-sprx/60
				//System.out.println((x1)+" "+(int)newv1.array()[0][1]/60+" " + (int)newv1.array()[0][2]/60 +" "+(int)newv2.array()[0][0]/60+" "+(int)newv2.array()[0][1]/60+" "+(int)newv2.array()[0][2]/60);

				// every even one isn't correct
				countdrac++;
				if(countdrac%2==1){
					System.out.println(x1+" "+y1+" " + z1 +" "+x2+" "+y2+" "+z2);
				}
				 */
				pctr++; // increase the number of points that have been read

			}

			polynum++; // increase the number of polygons that have been read
		}


		if(selector=true){					// if the selector is visible
			g.setColor(Color.DARK_GRAY);	// display the selector
			g.drawRect(sx, sy, 30, 30);
		}
		//int otherpolynum = 0;

		for( int x=0; x<4;x++){	// for all the front faces
			int otherpolynum = 0;	// set the number of polygons read to be 0
			int otherpctr = 0;		// set the number of points read to be 0
			CTM = new Matrix(); // have to REDO MATRICES with new xthetas
			rotate(0);
			rotate(1);
			scale();
			translate();
			if(iCurrent==0 && x==2){ 			// the following if statements make the game more visible by skipping the dispay of certain faces that would cause distortion
				skip=true;
			}else if(iCurrent==2 && x==0){		// if the current board is on instance 3 , do not display the first instance as it would cause distortion
				skip=true;
			}else if(iCurrent==1 && x==3){
				skip=true;
			}else if(iCurrent==3 && x==1){
				skip=true;
			}else{ 								// if none of the current faces are causing distortion, continue reading sprites
				skip=false;
				redsprite[x] = instance[x].getred();	
				bluesprite[x] = instance[x].getblue();
			}

			if(skip==false){					// only read sprites if there is no distortion
				while (redsprite[x].size() > otherpolynum) {
					g.setColor(Color.RED);
					otherpctr = 0;
					int otherwrap = 1;
					while (((ArrayList) redsprite[x].get(otherpolynum)).size() > otherpctr) {
						if (((ArrayList) redsprite[x].get(otherpolynum)).size() - 1 == otherpctr){
							otherwrap=otherpctr*-1;													// set the wrapper to go back to the beginning while still incrementing pctr
						}
						double  x1 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr)).returnx());
						double  y1 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr)).returny());
						double  z1 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr)).returnz());
						double  x2 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnx());
						double  y2 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returny());
						double 	z2 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnz());

						Matrix oldv1 = new Matrix(x1, y1, z1);
						Matrix oldv2 = new Matrix(x2, y2, z2);
						Matrix newv1 = oldv1.multiply(CTM.array());
						Matrix newv2 = oldv2.multiply(CTM.array());


						/*
						int []xval = new int[2];
						xval[0]=(int)(newv1.array()[0][0]);
						xval[1]=(int)(newv2.array()[0][0]);
						int []yval = new int[2];
						yval[0]=(int)(newv1.array()[0][1]);
						yval[1]=(int)(newv2.array()[0][1]);
						//g.drawPolygon(xval, yval, 2);
						g.fillPolygon(xval, yval, 2);
						 */
						g.drawLine((int)(newv1.array()[0][0]),(int)(newv1.array()[0][1]),(int)(newv2.array()[0][0]),(int)(newv2.array()[0][1])); //x1,y1,x2,y2
						
						//g.drawRoundRect((int)newv1.array()[0][0], (int)newv1.array()[0][1], 20, 20, 20, 20);
						//g.drawOval((int)(newv1.array()[0][0]), (int)(newv1.array()[0][1]), 10, 10);
						//	g.drawRoundRect((int)(newv1.array()[0][0]), (int)(newv2.array()[0][0]), 20, 20, true);
						++otherpctr;
					}
					++otherpolynum;
					g.setColor(Color.BLACK);

				}


				otherpolynum = 0;
				//otherpctr=0;// added
				while (bluesprite[x].size() > otherpolynum) {
					g.setColor(Color.BLUE);
					otherpctr = 0;
					int otherwrap = 1;
					while (((ArrayList) bluesprite[x].get(otherpolynum)).size() > otherpctr) {
						if (((ArrayList) bluesprite[x].get(otherpolynum)).size() - 1 == otherpctr){
							otherwrap=otherpctr*-1;													// set the wrapper to go back to the beginning while still incrementing pctr
						}
						double  x1 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr)).returnx());
						double  y1 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr)).returny() ) ;
						double  z1 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr)).returnz());
						double  x2 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnx());
						double  y2 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returny());
						double 	z2 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnz());

						Matrix oldv1 = new Matrix(x1, y1, z1);
						Matrix oldv2 = new Matrix(x2, y2, z2);
						Matrix newv1 = oldv1.multiply(CTM.array());
						Matrix newv2 = oldv2.multiply(CTM.array());

						/*
						int []xval = new int[2];
						xval[0]=(int)x1;
						xval[1]=(int)x2;
						int []yval = new int[2];
						yval[0]=(int)y1;
						yval[1]=(int)y2;
						g.drawPolygon(xval, yval, 2);
						 */
						g.drawLine((int)(newv1.array()[0][0]),(int)(newv1.array()[0][1]),(int)(newv2.array()[0][0]),(int)(newv2.array()[0][1])); //x1,y1,x2,y2

						++otherpctr;
					}
					++otherpolynum;
					g.setColor(Color.BLACK);

				}
			} //end if skip
			xtheta+=Math.PI/2;// rotate red and blue pieces by 90 degrees
		}
		
		ytheta+=Math.PI/2;	// increase the y axis angle to show the side faces

		for( int x=4; x<6;x++){ // for all the side faces
			int otherpolynum=0;
			CTM = new Matrix();
			//rotate(0); // only want to rotate on one axis
			rotate(1); 		
			scale();
			translate();
			if(iCurrent==4 && x==5){			// the following if statements make the game more visible by skipping the dispay of certain faces that would cause distortion
				skip2=true;
			}else if(iCurrent==5 && x==4){
				skip2=true;
			}else{								// if none of the current faces are causing distortion, continue reading sprites
				skip2=false;
				redsprite[x] = instance[x].getred();	
				bluesprite[x] = instance[x].getblue();
			}
			if(skip2==false){ 					// only read sprites if there is no distortion
				while (redsprite[x].size() > otherpolynum) {
					g.setColor(Color.RED);
					int otherpctr = 0;
					int otherwrap = 1;
					while (((ArrayList) redsprite[x].get(otherpolynum)).size() > otherpctr) {
						if (((ArrayList) redsprite[x].get(otherpolynum)).size() - 1 == otherpctr){
							otherwrap=otherpctr*-1;													// set the wrapper to go back to the beginning while still incrementing pctr
						}
						double  x1 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr)).returnx());
						double  y1 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr)).returny());
						double  z1 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr)).returnz());
						double  x2 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnx());
						double  y2 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returny());
						double 	z2 = (double) (((point) ((ArrayList) redsprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnz());

						Matrix oldv1 = new Matrix(x1, y1, z1);
						Matrix oldv2 = new Matrix(x2, y2, z2);
						Matrix newv1 = oldv1.multiply(CTM.array());
						Matrix newv2 = oldv2.multiply(CTM.array());

						//System.out.println("RED VALUES X1: "+x1+" y1: "+y1+" z1: "+z1+" X2: "+x2+" y2: "+y2+" z2: "+z2);

						/*
						int []xval = new int[2];
						xval[0]=(int)x1;
						xval[1]=(int)x2;
						int []yval = new int[2];
						yval[0]=(int)y1;
						yval[1]=(int)y2;
						g.drawPolygon(xval, yval, 2);
						 */
						g.drawLine((int)(newv1.array()[0][0]),(int)(newv1.array()[0][1]),(int)(newv2.array()[0][0]),(int)(newv2.array()[0][1])); //x1,y1,x2,y2

						++otherpctr;
					}
					++otherpolynum;
					g.setColor(Color.BLACK);

				}


				otherpolynum = 0;
				while (bluesprite[x].size() > otherpolynum) {
					g.setColor(Color.BLUE);
					int otherpctr = 0;
					int otherwrap = 1;
					while (((ArrayList) bluesprite[x].get(otherpolynum)).size() > otherpctr) {
						if (((ArrayList) bluesprite[x].get(otherpolynum)).size() - 1 == otherpctr){
							otherwrap=otherpctr*-1;													// set the wrapper to go back to the beginning while still incrementing pctr
						}
						double  x1 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr)).returnx());
						double  y1 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr)).returny() ) ;
						double  z1 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr)).returnz());
						double  x2 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnx());
						double  y2 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returny());
						double 	z2 = (double) (((point) ((ArrayList) bluesprite[x].get(otherpolynum)).get(otherpctr + otherwrap)).returnz());

						Matrix oldv1 = new Matrix(x1, y1, z1);
						Matrix oldv2 = new Matrix(x2, y2, z2);
						Matrix newv1;
						Matrix newv2;
						newv1 = oldv1.multiply(CTM.array());
						newv2 = oldv2.multiply(CTM.array());

						/*
						int []xval = new int[2];
						xval[0]=(int)x1;
						xval[1]=(int)x2;
						int []yval = new int[2];
						yval[0]=(int)y1;
						yval[1]=(int)y2;
						g.drawPolygon(xval, yval, 2);
						 */
						g.drawLine((int)(newv1.array()[0][0]),(int)(newv1.array()[0][1]),(int)(newv2.array()[0][0]),(int)(newv2.array()[0][1])); //x1,y1,x2,y2

						++otherpctr;
					}
					++otherpolynum;
					g.setColor(Color.BLACK);

				}
			}// end skip2
			ytheta+=Math.PI; // flip the pieces to the opposite side to 
		}
		ytheta-=Math.PI/2;	// negate all changes made to xtheta 2
	}
	public class KeyInputHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			/*	System.out.println("Key Pressed");
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" ");
			 */	
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {// moves selector left until it reaches a certain constraint set by the user
				if(sx!=405) sx-=60;
				//System.out.println("SX: "+sx);
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// moves selector right until it reaches a certain constraint set by the user
				if(sx!=765) sx+=60;
				//System.out.println("SX: "+sx);
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {// check to see that the board value are correct

				//System.out.println(instance[0].board[0][0] + " " + instance[1].board[0][0]+ " " +instance[2].board[0][0]+ " "+instance[3].board[0][0]+" "+instance[4].board[0][0] + " "+instance[5].board[0][0]);
				//System.out.println("TBOARD 00: "+tboard[0][0]);
				for(int current=0;current<6;current++){
					System.out.println("Current instance: "+current);
					System.out.println("");
					for(int r=0; r<6;r++){
						for(int c=0; c<7;c++){
							System.out.print(instance[current].board[r][c]+" ");
						}
						System.out.println(" ");
					}					
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {

				System.out.println("xangle: "+xtheta);
			}
			if (e.getKeyCode() == KeyEvent.VK_W){
				xtheta -= Math.PI/180;
			}
			if (e.getKeyCode() == KeyEvent.VK_S){
				xtheta += Math.PI/180;
			}
			if (e.getKeyCode() == KeyEvent.VK_A){
				ytheta-=Math.PI/180;
			}
			if (e.getKeyCode() == KeyEvent.VK_D){
				ytheta+=Math.PI/180;
			}


			if (e.getKeyCode() == KeyEvent.VK_I) {// shifts z axis of everything
				sprz -=5;
			}
			if (e.getKeyCode() == KeyEvent.VK_J) {// 
				sprx -=5;
			}
			if (e.getKeyCode() == KeyEvent.VK_K) {
				sprz +=5;
			}
			if (e.getKeyCode() == KeyEvent.VK_L) {// moves right
				sprx +=5;
			}

			if (e.getKeyCode() == KeyEvent.VK_T) {// change the current instance by decreasing the current instance number
				if(iCurrent!=0) {
					iCurrent--;
				}
				System.out.println("iCurrent: "+iCurrent);

			}
			if (e.getKeyCode() == KeyEvent.VK_Y) { // change the current instance by increasing the current instance number
				if(iCurrent!=5){
					iCurrent++;
				}
				System.out.println("iCurrent: "+iCurrent);
			}

			if(e.getKeyCode()== KeyEvent.VK_SHIFT){
				//System.out.println("iCurrent: "+iCurrent);
				instance[iCurrent].place(sx); // pass position of selector and attempt to place a counter
				instance[iCurrent].check(1);  // check if player one can win
				instance[iCurrent].check(2);  // check if player two can win
			}
			repaint();
		}
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	}
}
