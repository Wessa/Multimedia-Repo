import javax.swing.JOptionPane;


public class Main {

	public static void main(String[] args) {
		
		String input = JOptionPane.showInputDialog("Enter Data You Want To Compress") ;
	
		JOptionPane.showMessageDialog(null, "Data Compressed is :" + "\n" + Compress (input) ) ;
		
		JOptionPane.showMessageDialog(null, "Data After deCompression is :" + "\n" + deCompress ( Compress (input) ) ) ;
		
	}
	
	public static String Compress ( String Text ){
		
		int length , i = 0 , back ;
		
		String res = "" ;
		
		while ( i<Text.length() ){
			
			length = 1 ;
			
			while ( true ){
				
				back = Search ( Text , i , length ) ;
				
				if ( back == -1 ){
					
					if ( length == 1 ){
						
						res += "0 0 " + Text.charAt( i ) + "|" ;
					}
					else {
						
						back = Search ( Text , i , --length ) ;
											
						if ( (i + length) >= Text.length() )
							res += back + " " + length + " null|" ;
							
						else 
							res += back + " " + length + " " 
								 + Text.charAt(i + length) + "|" ;
							
						length ++ ;
					}
					
					break ;
				}
				
				length ++ ;
				
			} //end while 
			
			i += length ;
			
		} //end while
		
		return res ;
		
	} //end function Compress
	
	public static int Search ( String text , int pointer , int length ) {
		
		int res = -1 ;
		
		if ( pointer + length > text.length() ){
			
			return -1 ;
		}
		
		String subText = (String) text.substring( pointer , pointer + length ) ;
		String temp = text.substring ( 0 , pointer) ;
		
		res = temp.lastIndexOf(subText) ;
		
		/* This Loop perform as lastIndexOf Function */
		  
		/*for ( int i=0 ; i < pointer && (i + length) <= pointer ; i++ ) {
			
			temp = (String) text.substring( i , i + length ) ;
			
			if ( temp.equals( subText ) ) {
				
				res = i ;
			}
		}*/
		
		if ( res != -1 )
			return pointer - res ;
		
		else 
			return -1 ;
		
	} //end function Search
	
	public static String deCompress ( String cText ){
		
		String res = "" ;
		int i = 0 , pointer , length , j ;
		String nextChar ;
		
		while ( i<cText.length() ){
			
			pointer = -1 ;
			length = -1 ;
			
			j = i ;
			
			while ( cText.charAt(j) != '|' ){
				
				if ( cText.charAt(j) == ' ' && pointer == -1 ){
					
					pointer = Integer.parseInt ( cText.substring (i , j) ) ;
					i += ( j - i + 1 ) ;
				}
				else if ( cText.charAt(j) == ' ' && length == -1 ){
					
					length = Integer.parseInt ( cText.substring (i , j) ) ;
					i += ( j - i + 1 ) ;
				}
				
				j ++ ;
				
			} //end while
			
			nextChar = cText.substring (i , j) ;
			i += ( j - i + 1 ) ;
			
			if ( nextChar.equals("null") )
				nextChar = "" ;
			
			for ( int q=res.length() - pointer , s=0 ; s<length ; s++ , q++ ){
				
				res += res.charAt( q ) ;
			}
			
			res += nextChar ;
			
		} //end while
		
		return res ;
		
	} // end function deCompress 

}
