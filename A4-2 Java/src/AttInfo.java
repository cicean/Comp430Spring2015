
/**
 /* This little class holds the catalog info about an attribute.
 * 
 * Everything is pretty self explanatory, except for the "seqNumber"
 * field, which stores whether this is the first, second, third, etc., attribute
 * in the table to which this attribute belongs.
 */

 class AttInfo {

	private int valueCount;
	private String dataType;
	private int seqNumber;
	private String attName;
	private String alias;
	private String tableName;
	private double outputCount;

	/**
	 * @return the outputCount
	 */
	public double getOutputCount() {
		return outputCount;
	}

	/**
	 * @param outputCount the outputCount to set
	 */
	public void setOutputCount(double outputCount) {
		this.outputCount = outputCount;
	}

	public AttInfo (int numDistinctVals, String myType, int whichAtt, String attName) {
		valueCount = numDistinctVals;
		dataType = myType;
		seqNumber = whichAtt;
		this.attName = attName;
	}

	public int getNumDistinctVals () {
		return valueCount; 
	}

	public String getDataType () {
		return dataType; 
	}

	public int getAttSequenceNumber () {
		return seqNumber; 
	}

	/**
	 * @return the attName
	 */
	public String getAttName() {
		return attName;
	}
	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}
	
	
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	String print () {
		return "alias: "+alias+" tablename: "+tableName+" attName:"+attName + " vals: " + valueCount + "; type: " + dataType + "; attnum: " + seqNumber;  
	}
}