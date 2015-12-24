import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class Main {

	public static void main(String[] args) {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory ( new File ( System.getProperty("user.home") ) );
		int result = fileChooser.showOpenDialog ( fileChooser );
		
		ArrayList<String> dictionary = new ArrayList<String>() ;
		
		for ( int i=0 ; i<=127 ; i++ )
			dictionary.add( Character.toString ( (char)i ) ) ;
		
		String data = "" ;
		
		if ( result == JFileChooser.APPROVE_OPTION ) {
	
		    File selectedFile = fileChooser.getSelectedFile();
		    File destFile = new File ( "Destination.txt" ) ;
		   
				try {
					
					FileReader fr = new FileReader( selectedFile );
					BufferedReader br = new BufferedReader(fr);
					String line;
					
					while ( (line = br.readLine()) != null ) 
						data += line ;
					
					FileWriter fw = new FileWriter( destFile );
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write( compress ( dictionary , data ) );
					bw.close();
					
					dictionary = new ArrayList<String>() ;
					
					for ( int i=0 ; i<=127 ; i++ )
						dictionary.add( Character.toString ( (char)i ) ) ;
					
					fw = new FileWriter( selectedFile );
					bw = new BufferedWriter( fw ) ;
					bw.write( deCompress ( dictionary , compress ( dictionary , data ) ) );
					bw.close();
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		
		}

	}
	
	/* search for a word in the dictionary given the data string
	 and current index and length of the word */
	
	public static int search( ArrayList<String> dictionary, String data,
			int current, int length ) {

		if ( current + length > data.length() ) // checks if the found word was the 							
			return -2;						    // last word in the string and return -2  
												// as if the word was not found 
												// the function will return -1 
												// see line 86
		
		// make substring of string data from current till current + length 
		// which is the required word to search for 
		String sub = data.substring( current, current + length ) ;

		return dictionary.indexOf( sub ) ; // return -1 if not found in dictionary 
	}

	public static String compress( ArrayList<String> dictionary, String data ) {

		String res = "" ;
		
		// i is the current index , length is the length of word ,
		// pointer holds the value of function search when a word is passed for it 
		int i = 0, length, pointer ;
		
		// loop while i ( current index ) less than data.length 
		while (i < data.length()) {
			
			// set length to 1 again as i will search for a new word
			length = 1;

			while (true) {
				
				pointer = search( dictionary, data, i, length );
				
				// checks if my word is not found
				if (pointer == -1) {

					pointer = search( dictionary, data, i, --length ) ;
					res += Integer.toString( pointer ) + "|" ;

					dictionary.add( data.substring( i, i + length + 1) ) ;

					break;
				} 
				// checks if my word was the last word
				else if (pointer == -2) {

					pointer = search(dictionary, data, i, length - 1) ;
					res += Integer.toString( pointer ) + "|" ;

					break ;
				}

				length ++ ;
			}
			// increment current index by the length of the found word
			i += length ;
		}

		return res ;
	}

	public static String deCompress(ArrayList<String> dictionary, String cData) {
		
		// res holds the value of function decompress
		// lasword holds the value for lastword decompressed
		String res = "", lastWord = "";

		int i = 0 , j , pointer ;

		while ( i < cData.length() ){

			j = i ;
			
			// loop till find a delimiter that differs between 
			// each pointer in compressed text
			while ( cData.charAt(j) != '|' )
				j++ ;
			
			// gets the value of pointers written in the compressed text
			pointer = Integer.parseInt( cData.substring(i, j) );
			
			// if value of pointer not found in the dictionary so 
			// i will add to lastword decompressed the first char of it
			if ( pointer >= dictionary.size() ) {

				lastWord += lastWord.charAt(0) ;
				dictionary.add( lastWord ) ;

				res += lastWord ;
			} 
			// else lasword is the word at the pointer in the dictionary
			else {
				
				// check if this word is the first decompressd word
				if ( !lastWord.equals("") )
					dictionary.add( lastWord + dictionary.get(pointer).charAt(0) );

				lastWord = dictionary.get(pointer);
				res += lastWord;
			}
			// increment i by "j - i" ( msheet ad a 3ashan a5ud el pointer ) 
			// + 1 ( delimiter ) 
			i += j - i + 1;

		} // end while

		return res;
	}
}


