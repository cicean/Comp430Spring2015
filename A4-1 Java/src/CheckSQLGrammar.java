import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;



public class CheckSQLGrammar {
      Map<String,String>myFrom;
      ArrayList<Expression>mySelect;
      String att;
      Expression where;
      Map<String, TableData>res;
      
      public CheckSQLGrammar(Map <String, String> myFrom, ArrayList<Expression>mySelect,String att,Expression where)
      {
    	  this.myFrom = myFrom;
    	  this.mySelect = mySelect;
    	  this.att = att;
    	  this.where= where;
    	  this.res = Interpreter.res;
      }
      
     private boolean inValidFromClause (Map <String, String> fromClause)
     {
    	  Set <String> tableNames = res.keySet();
    	  Iterator <String> aliases = fromClause.keySet().iterator();
    	  while(aliases.hasNext()){
    		  String tableName = fromClause.get(aliases.next().toString());
    		  if(!(tableNames.contains(tableName)))
    		  {
    			  System.out.println("Error:Table "+ tableName + " do not exist in the TableData");
    			  return false;
    		  }
    	  }
		return false;
    	  
     }
     
     
     private boolean isValideinGroupByClause(String att, Map <String, String> fromClause, ArrayList<Expression>mySelect)
     {
    	 String alias = att.substring(0,att.indexOf("."));
    	 String attName = att.substring(att.indexOf(".")+1);
    	 String tableName = fromClause.get(alias);
    	 if(tableName == null)
    	 {
    		 System.out.println("Error:Alias "+ alias + " do not exist in the TableData");
			  return false;
    	 }
    	 Map <String, AttInfo> attributeInfo =  res.get(tableName).getAttributes();
    	 if(attributeInfo == null)
    	 {
    		 System.out.println("Error:Table "+ tableName + " do not exist in the TableData");
			  return false;
    	 }
    	 if(!attributeInfo.containsKey(attName))
    	 {
    		 System.out.println("Error:Attribute "+ attName + " do not exist in the TableData");
			  return false;
    	 }
    	 for (Expression exp : mySelect)
    	 {
    		 if(isBinaryOperation(exp.getType()))
    		 {
    			 System.out.println("Error:"+ exp.print() + " expression is incorrect in the select clause when GroupBy");
   			  return false;
    		 }else if(isUnaryOperation(exp.getType()))
    		 {
    		 }else if (!(exp.getType().equals("identifier")&& exp.getValue().equals(att)))
    		 {
    			 System.out.println("Error:"+ exp.print() + " expression is incorrect in the select clause when GroupBy");
      			  return false;
    		 }
    		 else{}
    	 }
    	 return true;
     }
      
      private String getAttributeType(String att,  Map <String, String> fromClause)
      {
    	  String attributeType;
    	  String alias = att.substring(0, att.indexOf("."));
    	  String tableName = fromClause.get(alias);
    	  if(tableName == null)
     	 {
     		 System.out.println("Error:Alias "+ alias + " do not exist in the any table in the FROM clause");
 			  return null;
     	 }
    	  String attName = att.substring(att.indexOf(".")+1);
    	  Map <String, AttInfo> attributeInfo = res.get(tableName).getAttributes();
    	  
     	 if(attributeInfo == null)
     	 {
     		 System.out.println("Error:Table "+ tableName + " do not exist in the TableData");
 			  return null;
     	 }
     	 if(!attributeInfo.containsKey(attName))
     	 {
     		 System.out.println("Error:Attribute "+ attName + " do not exist in the TableData");
 			  return null;
     	 }
     	 else
     		 
     		attributeType = attributeInfo.get(attName).getDataType();
     	 
    	  return attributeType;
      }
      
      private boolean isUnaryOperation (String expType){
    	  for(String operation : Expression.unaryTypes){
    		  if(operation.equals(expType))
    			  return true;
    	  }
    	  return false;
      }
      
      private boolean isBinaryOperation (String expType){
    	  for(String operation : Expression.binaryTypes){
    		  if(operation.equals(expType))
    			  return true;
    	  }
    	  return false;
      }
      
      private ResultValue checkCompatibility(ResultValue _resValue1,ResultValue _resValue2,String _type)
      {
    	  
    	  if(_resValue1.isResult()&&_resValue2.isResult())
    	  {
    		  if(_resValue1.getType()==1)
        		  return(new IntegerCompatibility().compatibility(_resValue1, _resValue2, _type));
    		  else
    			  return(new StringCompatibility().compatibility(_resValue1, _resValue2, _type));
    	  }
    	  else
    		  return(new ResultValue(-1, false));
      }
      
      private ResultValue validateTypeExpression(Expression exp, Map <String, String> fromClause)
      {
    	  if(exp.getType().equals("and")||exp.getType().equals("or"))
    	  {
    		  ResultValue resValue1 = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
    		  ResultValue resValue2 = validateTypeExpression(exp.getRightSubexpression(),fromClause);
    		  if(resValue1.isResult() && resValue2.isResult())
    		  {
    			  return(new ResultValue(-1, true));
    		  }
    		  else{
    			  return(new ResultValue(-1, false));
    		  }
    	  }
    	  
    	  if(isUnaryOperation(exp.getType()))
    	  {
    		  ResultValue rv = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
    		  if(!rv.isResult())
    			  System.out.println("Error: Invalid expression computation in: " + exp.print());	
    		  if(exp.getType().equals("not"))
    		  {
    			  return rv;
    		  }else if(rv.getType() == 0)
    		  {
    			  System.out.println("Error: Invalid expression computation in: " + exp.print());
				  return new ResultValue(-1, false);
    		  }
    		  else
    			return rv;  
    	  }
    	 
    	  String expType = exp.getType();
    	  String retType;
    		  
    	  if(isBinaryOperation(expType))
    	  {
    		  ResultValue resValue1 = null;
    		  ResultValue resValue2 = null;
    		  resValue1 = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
    		  resValue2 = validateTypeExpression(exp.getRightSubexpression(),fromClause);
    		  
    		  if((resValue1 != null) && (resValue2 !=null))
    		  {
    			  ResultValue rv = checkCompatibility(resValue1, resValue2, expType);
    			  if(!rv.isResult())
    				  System.out.println("Error: Invalid expression computation in: " + exp.print());
    			  return rv;
    		  }
    	  }
    	  
    	  
    	  if(exp.getType().equals("identifier"))
    	 {
    		  retType = getAttributeType(exp.getValue(), fromClause);
    		  if(retType == null)
    		  {
    			  return new ResultValue(-1, false);
    		  }if(retType.equals("Str"))
    			  return (new ResultValue(0, true));
    		  else
    			  return (new ResultValue(1, true));
    	  }
    	  
    	  if(exp.getType().equals("literal string"))
    		  return (new ResultValue(0, true));
    	  else
    		  return (new ResultValue(1, true));
    	  
      }
      
      public boolean validateQuery()
      {
    	  if(!inValidFromClause(myFrom))
    	  {
    		  System.out.println("Invalid 'FROM' Clause query: Referenced table Name do not present in database");
	        	return false;
    	  }
    	  
    	  if((where != null)&&!(validateTypeExpression(where,myFrom).isResult())){
	        	System.out.println("Invalid Expression in WHERE  :" + where.print());
	        	return false;
	        }
    	  
    	  if((att!= null)&& !isValideinGroupByClause(att,myFrom,mySelect))
    	  {
    		  System.out.println("Invalide 'GROUP¡¡BY' Clause");
	        	return false;
    	  }
    	  
    	  for(Expression selectExp : mySelect)
    	  {
    		  if(!(validateTypeExpression(selectExp,myFrom).isResult()))
    		  {
    			  System.out.println("Invalid Expression in SELECT  :" + selectExp.print());
	        		return false;
    		  }
    	  }
    	  return true;
      }
      
   
      
}


class ResultValue {
	int type;
	boolean result;
	
	public ResultValue(int type, boolean result){
		this.type = type;
		this.result = result;			
	}

	public int getType() {
		return type;
	}

	public boolean isResult() {
		return result;
	}	
}


class IntegerCompatibility {
	public ResultValue compatibility (ResultValue _resValue1, ResultValue _resValue2, String type){
		//Result value of the right expression is String type
		if(_resValue2.getType()==0){
			for(String incompatibility : Expression.incompatibleTypes)
				  if(type.equals(incompatibility)){
					  return (new ResultValue(-1, false));
				  }
		  }		
		return (new ResultValue(1, true));		
	}
}

class StringCompatibility {
	public ResultValue compatibility (ResultValue _resValue1, ResultValue _resValue2, String type){
		
		//Result value of the right expression is String type
		if(_resValue2.getType()==0){
			switch(type){
		  	case "minus":
		  	case "times":
		  	case "divided by"  : 
		  		return (new ResultValue(-1, false));
		  }		
		}
		//Result value of the right expression is Integer type
		else{
			for(String incompatibility : Expression.incompatibleTypes)
				  if(type.equals(incompatibility)){
					  return (new ResultValue(-1, false));
				  }
		}		
		return (new ResultValue(0, true));
	}
}

