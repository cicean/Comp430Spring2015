
class Attribute {
 
  private String name;
  private String attType;
  
  public String getName () {
    return name; 
  }
  
  public String getType () {
    return attType;
  }
  
  public Attribute (String inType, String inName) {
    name = inName;
    attType = inType;
  }
  
  public void setName(String name){this.name=name;}
  
  public String toString()
  {
  	  return "(type: "+this.attType+" name: "+this.name+")";
  	  }
  
  public void print (){
	  System.out.println(attType + "    "+name);
  }
  
}