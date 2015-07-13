


public class Expression {
  
  /**
   * This is an exhaustive list of expression types
   */
  static public final String [] validTypes = {
	"plus", "minus", "times", 
    "divided by",  "or", "and", "not", "literal string", "literal float",
    "literal int", "identifier", "unary minus",
    "sum", "avg", "equals", "greater than", "less than",
    "error" //For debug
    };
  
  /**
   * This is an exhaustive list of the unary expression types
   */
  static public final String [] unaryTypes = {"not", "unary minus", "sum", "avg"};
  
  /**
   * This is an exhaustive list of the binary expression types
   */
  static public final String [] binaryTypes = {"plus", "minus", "times",
    "divided by", "or", "and", "equals", "greater than", "less than"};
  
  /**
   * This is an exhaustive list of the value types
   */
  public final String [] valueTypes = {"literal string", "literal float",
    "literal int", "identifier"};
  
  /**
   * This is the type of the expression
   */
  private String myType;
  
  /**
   * This is the literal value contained in the expression; only non-null
   * if myType is "literal" or "identifier"
   */
  private String myValue;
  
  /**
   * Left subexpression, or subexpression for unary type
   */
  public Expression leftSubexpression;
  
  /**
   * Right subexpression, not available for unary type
   */
  public Expression rightSubexpression;
  
  public Expression getLeftSubexpression() {
		return leftSubexpression;
	  }

		public Expression getRightSubexpression() {
			return rightSubexpression;
		}
  
  
  private final String NOTEXIST = "Some abbr, attr, tablename or type is not in catalog!";

  
  
  /**
   * Print
   * @return
   */
  public String toString () {
    
    String toMe;
    
    // see if it is a literal type
    for (int i = 0; i < valueTypes.length; i++) {
      if (myType.equals (valueTypes[i])) {
        toMe = myValue;
        return toMe;
      } 
    }
    
    // see if it is a unary type
    for (int i = 0; i < unaryTypes.length; i++) {
      if (myType.equals (unaryTypes[i])) {
        toMe = "(" + myType + " " + leftSubexpression.toString () + ")";
        return toMe;
      }
    }
    
    // lastly, do a binary type
    for (int i = 0; i < binaryTypes.length; i++) {
      if (myType.equals (binaryTypes[i])) {
        toMe = "(" + leftSubexpression.toString () + " " + myType + " " + rightSubexpression.toString () + ")";
        return toMe;
      }
    }
    throw new RuntimeException ("got a bad type in the expression when printing");
  }
  
  // prints the expression
  public String print () {
    
    String toMe;
    
    // see if it is a literal type
    for (int i = 0; i < valueTypes.length; i++) {
      if (myType.equals (valueTypes[i])) {
        toMe = myValue;
        return toMe;
      } 
    }
    
    // see if it is a unary type
    for (int i = 0; i < unaryTypes.length; i++) {
      if (myType.equals (unaryTypes[i])) {
        toMe = "(" + myType + " " + leftSubexpression.print () + ")";
        return toMe;
      }
    }
    
    // lastly, do a binary type
    for (int i = 0; i < binaryTypes.length; i++) {
      if (myType.equals (binaryTypes[i])) {
        toMe = "(" + leftSubexpression.print () + " " + myType + " " + rightSubexpression.print () + ")";
        return toMe;
      }
    }
    throw new RuntimeException ("got a bad type in the expression when printing");
  }
  
  /**
   * Create a new expression of type specified type
   * @param expressionType
   */
  public Expression (String expressionType) {
    
    for (int i = 0; i < validTypes.length; i++)
      if (expressionType.equals (validTypes[i])) {
        myType = expressionType;
    	return;
      }
    
    throw new RuntimeException ("you tried to create an invalid expr type");
  }
  
  /**
   * 
   * @return The type of the expression
   */
  public String getType () {
    return myType;
  }
  
  /**
   *
   * @return The value of the expression, if it is a literal (in which
   * case the literal values encoded as a string is returned), or it is an
   * identifier (in which case the name if the identifier is returned)
   */
  public String getValue(){
	  for (int i = 0; i < valueTypes.length; i++)
	      if (myType.equals (valueTypes[i])) return myValue;
	    
	  return this.NOTEXIST;
  }
 
  /**
   * This sets the value of the expression, if it is a literal or an 
   * identifier
   * @param toMe
   */
  public void setValue (String toMe) {
    for (int i = 0; i < valueTypes.length; i++)
      if (myType.equals (valueTypes[i])) {
        myValue = toMe;
        return;
      }
    throw new RuntimeException ("Expression:setValue: You can't set a value for that expr type!");
  }
 
  /**
   * The subexpression, which is only possible if this is a 
   * unary operation (such as "unary minus" or "not")
   * @return "error" type expression if error occurs
   */
  public Expression getSubexpression(){
	  for (int i = 0; i < unaryTypes.length; i++)
	      if (myType.equals (unaryTypes[i]))
	    	  return this.leftSubexpression;
	  
	  return new Expression("error");	  
  }
  
  /**
   * This sets the subexpression, which is only possible if this is a 
   * unary operation (such as "unary minus" or "not")
   * @param newChild
   */
  public void setSubexpression (Expression newChild) {
    
    for (int i = 0; i < unaryTypes.length; i++)
      if (myType.equals (unaryTypes[i])) {
        leftSubexpression = newChild;
        return;
      }
    
    throw new RuntimeException ("you can't set the subexpression of an " +
                                "expression that is not unary!");
  }
 
  /**
   * Either the left or the right subexpression, which is only 
   * possible if this is a binary operation... whichOne should either be
   * the string "left" or the string "right"
   * @param whichOne
   * @return "error" type expression if error occurs
   */
  public Expression getSubexpression(String whichOne){
	  for (int i = 0; i < binaryTypes.length; i++) {
	      if (myType.equals (binaryTypes[i])) {
	        if (whichOne.equals ("left")||whichOne.equals ("l")||whichOne.equals ("L"))
	          return leftSubexpression;
	        else if (whichOne.equals ("right")||whichOne.equals ("r")||whichOne.equals ("R"))
	          return rightSubexpression;
	        else
	          throw new RuntimeException ("Expression.getSubexpression: whichOne must be left/l or right/r");
	      }
	    }
	  return new Expression("error");
  }
  
  /**
   * This sets the left and the right subexpression
   * @param left
   * @param right
   */
  public void setSubexpression (Expression left, Expression right) {
    
    for (int i = 0; i < binaryTypes.length; i++) {
      if (myType.equals (binaryTypes[i])) {
        leftSubexpression = left;
        rightSubexpression = right;
        return;
      }
    }
    
    throw new RuntimeException ("you can't get the l/r subexpression of " +
                                "an expression that is not binry!");
  }
}
