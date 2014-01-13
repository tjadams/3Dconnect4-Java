import java.util.*;
// Tyler Adams - Friday June 7th, 2013
public class Matrix {
	private double [][] matrix;
	
	public Matrix(){
		matrix = new double[4][4];
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
	}
	
	public Matrix (double [][] tempmatrix){
		matrix = tempmatrix;
	}
	public Matrix (double x,double y, double z){
		double [][] temp = new double [1][4];
		temp [0][0] = x;
		temp [0][1] = y;
		temp [0][2] = z;
		temp [0][3] = 1;
		matrix = temp;
	}
	public Matrix (String [] tempmatrix){
		double [][] tempint = new double [tempmatrix.length][4];
		for (int x = 0;x< tempmatrix.length;x++){
			StringTokenizer st  = new StringTokenizer (tempmatrix[x]);
			int count =0;
			while (st.hasMoreTokens()){
				tempint[x][count] = Integer.parseInt(st.nextToken());
				count++;
			}
		}
		matrix = tempint;
	}
	public Matrix multiply(double [][] matrix2){
		double [][] finalmatrix = new double[matrix.length][matrix2[0].length];
		double temp;
		Matrix finalthing;
		if (matrix[0].length != matrix2.length){
			System.out.println("Matrix multiplication is impossible with these matrices");
		}
		else {
			for (int x =0;x< matrix.length;x++){
				for (int y = 0; y < matrix2[0].length;y++){
					temp = 0;
					for (int z = 0; z < matrix2.length; z++){
				//		System.out.print(matrix[x][z] + " " + matrix2 [z][y]);
						temp = temp + (matrix[x][z] * matrix2[z][y]);
				//		System.out.println(" ");
					}
				//	System.out.println(" ");
					finalmatrix[x][y] = temp;
				}
			}
		}
		finalthing = new Matrix(finalmatrix);
		return finalthing;
	}
	public double[][] array(){
		return matrix;
	}
	public void replace(Matrix replacer){
		matrix = replacer.array();
	}
}
