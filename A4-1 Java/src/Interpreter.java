import java.io.*;

import org.antlr.runtime.*;

import java.util.*;
  
class Interpreter {
  
	static Map <String,TableData> res;
	

public static void main (String [] args) throws Exception {
    
    try {
      
      CatalogReader foo = new CatalogReader ("C:/Users/cicean/workspace/Comp430A41/src/Catalog.xml");
      Map <String, TableData> res = foo.getCatalog ();
      System.out.println (foo.printCatalog (res));
      
      InputStreamReader converter = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(converter);
      
      System.out.format ("\nSQL>");
      String soFar = in.readLine () + "\n";
      
      // loop forever, or until someone asks to quit
      while (true) {
        
        // keep on reading from standard in until we hit a ";"
        while (soFar.indexOf (';') == -1) {
          soFar += (in.readLine () + "\n");
        }
        
        // split the string
        String toParse = soFar.substring (0, soFar.indexOf (';') + 1);
        soFar = soFar.substring (soFar.indexOf (';') + 1, soFar.length ());
        toParse = toParse.toLowerCase ();
        
        // parse it
        ANTLRStringStream parserIn = new ANTLRStringStream (toParse);
        SQLLexer lexer = new SQLLexer (parserIn);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser (tokens);
        
        // if we got a quit
        if (parser.parse () == false) {
          break; 
        }
        
        // print the results
        System.out.println ("RESULT OF PARSING");
        System.out.println ("Expressions in SELECT:");
        ArrayList <Expression> mySelect = parser.getSELECT ();
        for (Expression e : mySelect)
          System.out.println ("\t" + e.print ());
        
        System.out.println ("Tables in FROM:");
        Map <String, String> myFrom = parser.getFROM ();
        System.out.println ("\t" + myFrom);
        
        System.out.println ("WHERE clause:");
        Expression where = parser.getWHERE ();
        if (where != null)
          System.out.println ("\t" + where.print ());
        
        String att = parser.getGROUPBY();
        if(att != null)
        {
           System.out.println ("GROUPING atts:");
          System.out.println ("\t" + att);
	     }
        
        System.out.println("\n\n Check T-SQL Expression Validation \n");

        if ((new CheckSQLGrammar(myFrom, mySelect, att, where)).validateQuery()){
        	System.out.println("Query expression is correct");
        }
        
	  	System.out.println("\n\n End \n");
              
        System.out.format ("\nSQL>");
              
      } 
    } catch (Exception e)
    {
      System.out.println("Error! Exception: " + e); 
    } 
  }

  
}
