package com.andy0010.hbase.baseapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @Title: BaseHbaseHelper.java
 * @Package com.andy0010.hbase.baseapi
 * @Description: hbase-client基础服务类
 * @author 
 * @date 2016年5月2日 上午10:17:47
 * @version V1.0
 */
public class BaseHbaseHelper {
	
	@Autowired
	private HTableDescriptor descriptor;
	
	@Autowired
	private HColumnDescriptor hColumnDescriptor;
	
	@Autowired
	private HBaseAdmin hbaseAdmin;
	
	@Autowired
	private HTable hTable;
	
	@Autowired(required=false)
	private List<Put> puts;
	
	@Autowired
	private Delete delete;
	
	@Autowired
	private Get get;
	
	@Autowired
	private Scan scan;
	
	@Autowired
	private BaseHbaseInput baseHbaseInput;
	
	public static void main(String[] args) throws IOException{
		ApplicationContext context = new ClassPathXmlApplicationContext("baseHbase.xml");
		BaseHbaseHelper helper = (BaseHbaseHelper) context.getBean("baseHbaseHelper");
		helper.dropTable();
		helper.createTable();
//		helper.putData();
//		helper.getData();
//		helper.singleGetData();
//		helper.deleteData();
//		helper.scanTable();
//		helper.compareAndSet();
	}
	
	/**
	 * 
	 * @Title: createTable
	 * @Description: 创建表
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void createTable() throws IOException {
		/**
		 * descriptor添加列族
		 */
	    descriptor.addFamily(hColumnDescriptor);
	    
	    System.out.println("Create table " + baseHbaseInput.getTable());
	    
	    try {
	      hbaseAdmin.createTable(descriptor);
	    } catch(TableExistsException e) {
	      System.out.println("Table " + baseHbaseInput.getTable() + " exists");
	    }
	    
	    System.out.println("Success create table " + baseHbaseInput.getTable());
	}
	
	/**
	 * 
	 * @Title: putData
	 * @Description: 插入数据
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void putData() throws IOException {
	    System.out.println("Put data... ");
	   	    
	    for(int i=0; i < 5; i++){
	    	byte[] rowKey = (baseHbaseInput.getRowKey()+"_" + i).getBytes();
	    	Put put = new Put(rowKey);
	    	put.add(Bytes.toBytes(baseHbaseInput.getCf()), Bytes.toBytes(baseHbaseInput.getQualifier()), Bytes.toBytes(baseHbaseInput.getValue()));
	    	puts.add(put);
	    }
	    
	    hTable.put(puts);
	    hTable.flushCommits();
	    System.out.println("Success put data... ");
	}
	
	/**
	 * 
	 * @Title: getData
	 * @Description: 根据行键获取数据,批量
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void getData() throws IOException {
//		System.out.println(baseHbaseInput);
		for(int i=0; i < 5; i++){
			String rowKey = baseHbaseInput.getRowKey()+"_" + i;
			System.out.println("Get data " + rowKey);
			Get get = new Get(rowKey.getBytes());
			Result r = hTable.get(get);
	    	byte[] value = r.getValue(Bytes.toBytes(baseHbaseInput.getCf()), Bytes.toBytes(baseHbaseInput.getQualifier()));
	    	System.out.println("Get value: " + Bytes.toString(value));
		}
	}
	
	/**
	 * 
	 * @Title: singleGetData
	 * @Description: 根据行键获取数据
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void singleGetData() throws IOException {
		
			String rowKey = baseHbaseInput.getRowKey()+"_" + 1;
			System.out.println("Get data " + rowKey);
			Get get = new Get(rowKey.getBytes());
			Result r = hTable.get(get);
	    	byte[] value = r.getValue(Bytes.toBytes(baseHbaseInput.getCf()), Bytes.toBytes(baseHbaseInput.getQualifier()));
	    	System.out.println("Get value: " + Bytes.toString(value));
	}
	
	/**
	 * 
	 * @Title: scanTable
	 * @Description: 扫描全表
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void scanTable() throws IOException {
	    System.out.println("Scan table " + baseHbaseInput.getTable());
	    scan.addColumn(Bytes.toBytes(baseHbaseInput.getCf()), Bytes.toBytes(baseHbaseInput.getQualifier()));
	    
	    ResultScanner scanner = hTable.getScanner(scan);
	    for (Result res : scanner) {
	      System.out.println("Found row: " + res);
	    }
	}
	
	/**
	 * 
	 * @Title: deleteData
	 * @Description: 根据行键删除数据
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void deleteData() throws IOException {
	    System.out.println("Delete row " + baseHbaseInput.getRowKey());
	    hTable.delete(delete);
	}
	
	/**
	 * 
	 * @Title: dropTable
	 * @Description: 根据表明删除表
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void dropTable() throws IOException {
	    System.out.println("Drop table " + baseHbaseInput.getTable());

	    hbaseAdmin.disableTable(baseHbaseInput.getTable());
	    hbaseAdmin.deleteTable(baseHbaseInput.getTable());
	    
	    System.out.println("Success drop table " + baseHbaseInput.getTable());
	}
	
	/**
	 * 
	 * @Title: compareAndSet
	 * @Description: 使用原子性操作compare-and-set
	 * @param @throws IOException
	 * @return void 返回类型
	 */
	public void compareAndSet() throws IOException{
		byte[] row1 = "row1".getBytes();
    	Put put1 = new Put(row1);
    	put1.add("cf1".getBytes(), "qual1".getBytes(), "value1".getBytes());
    	/**
    	 * case:row1中列族为cf1的value为null
    	 * action:
    	 * 		满足时，向row1插入put1
    	 * 		不满足时，返回false
    	 */
		boolean res1 = hTable.checkAndPut("row1".getBytes(), "cf1".getBytes(), "qual1".getBytes(), null, put1);
		System.out.println("1. Put row_key:" + new String(row1) +  ", cf:" + "cf1"+ ", applied  " + res1);
		
		/**
    	 * case:row1中列族为cf1的value为null
    	 * action:
    	 * 		满足时，向row1插入put1
    	 * 		不满足时，返回false
    	 */
		boolean res2 = hTable.checkAndPut("row1".getBytes(), "cf1".getBytes(), "qual1".getBytes(), null, put1);
		System.out.println("2. Put row_key:" + new String(row1) +  ", cf:" + "cf1" + ", applied  " + res2);
		
		Put put2 = new Put(row1);
    	put2.add("cf1".getBytes(), "qual2".getBytes(), "value2".getBytes());
    	/**
    	 * case:row1中列族为cf1的value为value1
    	 * action:
    	 * 		满足时，向row1插入put2
    	 * 		不满足时，返回false
    	 */
		boolean res3 = hTable.checkAndPut(row1, "cf1".getBytes(), "qual1".getBytes(), "value1".getBytes(), put2);
		System.out.println("3. Put row_key:" + new String(row1) +  ", cf:" + "cf1" + ", applied  " + res3);
		
		byte[] row2 = "row2".getBytes();
		Put put3 = new Put(row2);
    	put3.add("cf1".getBytes(), "qual2".getBytes(), "value3".getBytes());
    	
    	/**
    	 * case:row1中列族为cf1的value为value1
    	 * action:
    	 * 		满足时，向row1插入put3
    	 * 		不满足时，返回false
    	 * 此处，由于checkAndPut的row key为row1,与put3的row key row2不符，抛异常
    	 */
		boolean res4 = hTable.checkAndPut(row1, "cf1".getBytes(), "qual1".getBytes(), "value1".getBytes(), put3);
		System.out.println("4. Put row_key:" + new String(row2) +  ", cf:" + "cf1" + ", applied  " + res4);
		
	}
}
