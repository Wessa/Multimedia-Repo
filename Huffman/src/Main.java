import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

public class Main {
	
	static Map <String , String> result = new HashMap <String , String>() ;

	public static void main(String[] args) {
		
		int num = 0 ;
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory ( new File ( System.getProperty("user.home") ) );
		int result = fileChooser.showOpenDialog ( fileChooser );
		
		Map <String , Integer> m = new HashMap <String , Integer>() ;
		
		String data = "" ;
		
		if ( result == JFileChooser.APPROVE_OPTION ) {
			
		    File selectedFile = fileChooser.getSelectedFile();
		    
			try {
				
				FileReader fr = new FileReader( selectedFile );
				BufferedReader br = new BufferedReader( fr ) ; 
				String line ;
						
				while ( (line = br.readLine()) != null ) 
					data += line ;
				
				for ( int i=0 ; i<data.length(); i++ ){
					
					String temp = Character.toString(data.charAt(i)) ;
					
					if ( m.containsKey( temp ) )
						m.put( temp  , m.get( temp ) + 1 ) ;
					
					else 
						m.put( temp , 1 ) ;
					
				}
				
				m = sortByValue ( m ) ;
				Recursion ( m ) ;
				System.out.println ( Main.result ) ;
				
				num = compress ( data ).length();
				num /= 31 ;
				
				write ( num , compress ( data ) ) ;
				System.out.println ( "Data From Compressed File : " + read ( num ) ) ;
				
				System.out.println ( "Data Compressed : " + compress ( data ) ) ;
				System.out.println ( "Original Data : " + deCompress( compress ( data ) ) ) ;
						
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
				
			}

		}
		
		
	}
	
	public static String read ( int num ){
		
		String res = "" ;
		int i = 0 ;
		
		try {
			
			FileInputStream fis = new FileInputStream("out.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
				
			while ( i <= num ){
		
				res += Integer.toBinaryString( ois.readInt() ) ;
				i ++ ;
			}
				
			ois.close();
			fis.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return res;
		
	}
	
	public static void write ( int num , String cData ){
			
		try {
			
			FileOutputStream fos = new FileOutputStream("out.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
				
			int i = 0 , j = 1 ;
			String temp = "" ;
			
			while ( j < num ){
				
				temp = cData.substring( i , i + 31*j ) ;
				oos.writeInt( Integer.parseInt( temp , 2 ) ) ;
				
				i += 31 ;
				j ++ ;
			}
			
			temp = cData.substring( i , i + ( cData.length() - i ) ) ;
			oos.writeInt( Integer.parseInt( temp , 2 ) ) ;
				
			oos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void Recursion ( Map <String, Integer> data ){
		
		if ( data.size() == 2 ){
			
			int s = 0 ;
			
			for ( Map.Entry<String , Integer> entry : data.entrySet() ) {
				
				if ( s == 0)
					result.put( entry.getKey() , "0" ) ;
				
				else 
					result.put( entry.getKey() , "1" ) ;
				
				s ++ ;
			}
			
			return ;
		}
		
		int count = 0;
		
		Map <String, Integer> temp = new HashMap <String , Integer>() ;
		
		String a = "" , b = "" ;
		
		for ( Map.Entry<String , Integer> entry : data.entrySet() ) {
			
			if ( count == data.size()-2 )
				a = entry.getKey() ;
			
			else if ( count == data.size()-1 )
				b = entry.getKey() ;
		
			count ++ ;
		}
		
		data.put( a + b , data.get(a) + data.get(b) ) ;
		data.remove(a) ;
		data.remove(b) ;
		
		data = sortByValue(data) ;
		
		Recursion ( data ) ;
		
		result.put ( a , result.get ( a + b ) + "0" ) ;
		result.put ( b , result.get ( a + b ) + "1" ) ;
		
		result.remove( a + b ) ;
			
	}

	public static String compress ( String data ){
		
		String res = "" ;
		
		for ( int i=0 ; i<data.length() ; i++ ){
			
			String temp = Character.toString(data.charAt(i)) ;
			
			res += result.get( temp ) ;
		}
		
		return res ;
	}
	
	public static String deCompress ( String cData ){
		
		String res = "" , temp = "" ;
		int i = 0 , j = 0;
		
		while ( i <= cData.length() ){
			
			temp = cData.substring( j , i ) ;
			
			if ( result.containsValue( temp ) ){
				
				for ( Map.Entry<String , String> entry : result.entrySet() ) {
					
					if ( entry.getValue().equals( temp ) )
						res += entry.getKey();
					
				}
				
				j = i ;
			}
			
			i ++ ;
		}
		
		return res ;
	}
	
	public static Map sortByValue(Map unsortMap) {
		
		List list = new LinkedList(unsortMap.entrySet());
	 
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
							.compareTo(((Map.Entry) (o1)).getValue());
			}
		});
	 
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
