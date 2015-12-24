import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory ( new File ( System.getProperty("user.home") ) );
		int result = fileChooser.showOpenDialog ( fileChooser );
		
		if ( result == JFileChooser.APPROVE_OPTION ) {
			
			String img = fileChooser.getSelectedFile().getPath();
			compress ( img ) ;
			deCompress( img ) ;
			
		}
	}

	public static void compress ( String imgName ){
		
		ImageRW x = new ImageRW() ;
		
		int [][] Q ;
		int numberOfLevels = 0 , step = 0 , temp = 0 ;
		ArrayList<String> imgCoding = new ArrayList<String>() ; // holds Compressed image 
																// then save to imgCoding.txt
		
		Q = x.readImage ( imgName ) ;
		
		numberOfLevels = Integer.parseInt( JOptionPane.showInputDialog("Enter Number of levels") ) ;
		
		step = 256/numberOfLevels ;
		
		int [][] QQ = new int [numberOfLevels][2] ;     // holds the range values ( low & high )
		
		try {
			
			FileWriter fw = new FileWriter("Quantizer.txt") ;
			BufferedWriter bw = new BufferedWriter ( fw ) ;
			
			for ( int i=0 ; i<numberOfLevels ; i++ ){
				
				QQ[i][0] = temp ;
				QQ[i][1] = temp + step - 1 ;
				temp += step ;
				
				bw.write ( Integer.toString(i) + " " + Integer.toString( (QQ[i][0] + QQ[i][1])/2 ) );
				bw.write ( "\n" ) ;
			}
			
			bw.close();
			
			fw = new FileWriter("imgCoding.txt") ;
			bw = new BufferedWriter ( fw ) ;
			
			for ( int i=0 ; i<Q.length ; i++ ){
				
				for ( int j=0 ; j<Q[0].length ; j++ ){
					
					for ( int k=0 ; k<QQ.length ; k++ ){
						
						if ( Q[i][j] >= QQ[k][0] && Q[i][j] <= QQ[k][1] )
							bw.write ( Integer.toString(k) + " " );
				
					}
				}
		
				bw.write("\n");
			}
			
			bw.close();
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	public static void deCompress ( String imgName ){
		
		ImageRW x = new ImageRW() ;
		
		ArrayList<int[]> cImg = new ArrayList<int[]>() ; // holds Compressed image from imgCoding.txt
		ArrayList<int[]> Q = new ArrayList<int[]>() ;    // holds Quantizer table from Quantizer.txt
		
		int [][] original ;
		original = x.readImage(imgName);				 // holds Original Pixels of image
		
		double mse = 0 ;
	
		try {
			
			FileReader fr = new FileReader ( "Quantizer.txt" ) ;
			BufferedReader br = new BufferedReader ( fr ) ;
			
			String line = "";
			while ( (line = br.readLine()) != null ){
				
				String[] temp = line.split(" ") ;
				int[] temp1 = new int[temp.length];
				
				for ( int i=0 ; i<temp.length ; i++)
					temp1[i] = Integer.parseInt( temp[i] ) ;
				
				Q.add( temp1 ) ;
			}
			
			br.close();
			
			fr = new FileReader ( "imgCoding.txt" ) ;
			br = new BufferedReader ( fr ) ;
			
			while ( (line = br.readLine()) != null ){
				
				String[] temp = line.split(" ") ;
				int[] temp1 = new int[temp.length];
				
				for ( int i=0 ; i<temp.length ; i++)
					temp1[i] = Integer.parseInt( temp[i] ) ;
				
				cImg.add( temp1 ) ;
			}
			
			br.close();
			
			for ( int i=0 ; i<cImg.size() ; i++ ){
				
				for ( int j=0 ; j<cImg.get(0).length ; j++ ){
					
					for ( int k=0 ; k<Q.size() ; k++ ){
						
						if ( cImg.get(i)[j] == k ){
							
							mse += (original[i][j] - Q.get(k)[1] )*(original[i][j] - Q.get(k)[1]) ;
							original[i][j] = Q.get(k)[1] ;
						}
					}
				}
			}
			
			x.writeImage(original, "copy.jpg");
			
			mse /= (original.length)*(original[0].length) ;
 			
			JOptionPane.showMessageDialog(null, "MSE = " + Double.toString(mse)) ;
			
		} 
		catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
