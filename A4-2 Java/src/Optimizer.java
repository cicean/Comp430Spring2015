import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 */


public class Optimizer{
	Map <String, String> myFrom;
	ArrayList <Expression> mySelect;
	String att;
	Expression where;
	Map <String, TableData> res;
	ArrayList<ResultValue> selTypes;
	String outputFile;
	String compiler;
	String outputLocation;
	
	public Optimizer(Map <String, String> myFrom,ArrayList <Expression> mySelect,String att,Expression where,
			ArrayList<ResultValue> selTypes){
		this.myFrom = myFrom;
		this.mySelect = mySelect;
		this.att =  att;
		this.where = where;	
		this.res = Interpreter.res;
		this.selTypes = selTypes;
		outputFile= "out.tbl";
		compiler = "g++";
		outputLocation = "cppDir/";
		
	}
	
	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * @param compiler the compiler to set
	 */
	public void setCompiler(String compiler) {
		this.compiler = compiler;
	}

	/**
	 * @param outputLocation the outputLocation to set
	 */
	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}
	


	/**
	 * Function to execute the query with only one table in FROM clause
	 */
	public void doSelection(){
		ArrayList <Attribute> tableAttribute = new ArrayList<Attribute>();
		Iterator<String> aliases = myFrom.keySet().iterator();
		String tableName = null;
		while(aliases.hasNext()){
			String alias = aliases.next().toString();
			tableName = myFrom.get(alias);
			tableAttribute = CommonMethods.getTableAttributeInfo(alias,tableName,true);
		}		
		ArrayList <Attribute> selectExpTypes = CommonMethods.makeTypeOutAttributes(selTypes);
		HashMap <String, String> exprs =  CommonMethods.makeSelectExpression(selectExpTypes,true,null,null);
		
		String selection = "(Int)1 == (Int) 1";
		if(where!= null){
			selection = CommonMethods.parseExpression(where, myFrom,true);
//			int ind1 = selection.indexOf('(');
//			int ind2 = selection.lastIndexOf(')');
			//if((ind1 ==0) &&(ind2 == selection.length()-1))
				//selection = selection.substring(1, selection.length()-1);
		}
		
//		String selection = "c_custkey < Int (10)";
	    String tableUsed = tableName+".tbl";
	    
	    System.out.println("---------------------");
	    for (Attribute att : tableAttribute){
	    	att.print();
	    }
	    System.out.println("---------------------");

	    for (Attribute att : selectExpTypes){
	    	att.print();
	    }
	    System.out.println("---------------------");

	    System.out.println(exprs);
	    
	    System.out.println("---------------------");
	    
	    System.out.println(selection);
	    
	    System.out.println("---------------------");

	    System.out.println(tableUsed+"\n"+outputFile+"\n"+compiler+"\n" + outputLocation);
	    
	    System.out.println("---------------------");

	    
	    
	    System.out.println(tableAttribute+"   \n"+selectExpTypes+"\n"+exprs);
	    // run the selection operation
	    try {
	      Selection foo = new Selection (tableAttribute,
	    	selectExpTypes, selection, exprs, tableUsed, 
	    	outputFile, compiler, outputLocation );

	    } catch (Exception e)  {
	      throw new RuntimeException (e);
	    }
	}
	

	/**
	 * Function to execute the query with more than one table in FROM clause
	 */
	
	public void doJoin() {
		//CommonMethods.executeRATree(myFrom, mySelect, where);		
		//CommonMethods.createRATree(myFrom, mySelect, where);
	}
	
	/**
	 * 
	 */
	public void execution() {
		CommonMethods.fromClause = myFrom;
		CommonMethods.executeRATree(CommonMethods.analysisRATree(myFrom, mySelect, where, att));
//		CommonMethods.createRATree(myFrom, mySelect, where ,att);
//		CommonMethods.analysisRATree(myFrom, mySelect, where, att);
		
	}	
}