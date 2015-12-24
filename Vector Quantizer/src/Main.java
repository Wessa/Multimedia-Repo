import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		
		int[][] a = new int[8][8] ;
		
		int len , w ;
		len = 2 ;
		w = 4 ;
		
		ArrayList<int[][]> table = new ArrayList<int[][]>(8) ;
		
		for ( int i=0 ; i<8 ; i++ ){
			
			int[][] temp = new int[len][w] ;
			table.add(temp) ;
			
		}
		
		for ( int i=0 , k=1 ; i<8 ; i++ ){
			for ( int j=0 ; j<8 ; j++ , k++){
				a[i][j] = k ;
			}
		}
		
		int t = 0 , i = 0 ;
		
		while ( i<table.size() ){
			
			for ( int j=0 ; j<len ; j++ ){
				
				t = 0 ;
			
				while ( t < 8/w ){
					
					for ( int k=t*w ; k<(w*(t+1)) ; k++ ){
						
						System.out.println(k + " " + t + " " + j + " " + i );
						table.get(i + t)[j][k%w] = a[j][k] ;
						
					}
					
					t ++ ;
					
				}
				//break ;
			}
			i += 8/w ;
			
		}
		
		for ( int j=0 ; j<table.size() ; j++ ){
			
			for ( int k=0 ; k<table.get(j).length ; k++ ){
				
				for ( int l=0 ; l<table.get(j)[0].length ; l++ ){
					
					System.out.print ( table.get(j)[k][l] + " " );
					
				}
				
				System.out.println();
			}
			System.out.println("==============================");
		}
		
	}
	
	public static void compress(){
		
	}

}
