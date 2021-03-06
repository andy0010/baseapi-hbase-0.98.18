package com.andy0010.hbase.baseapi;

/**
 * 
 * @Title: BaseHbaseInput.java
 * @Package com.andy0010.hbase.baseapi
 * @Description: base api for hbase
 * @author abdy0010
 * @date 2016年5月1日 下午1:14:51
 * @version V1.0
 */
public class BaseHbaseInput{
	
	private String table;
	private String cf;
	private String qualifier;
	private String rowKey;
	private String value;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
