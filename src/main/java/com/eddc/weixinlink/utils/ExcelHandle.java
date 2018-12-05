package com.eddc.weixinlink.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelHandle {
	private static final String EXCEL_XLS = "xls";  
	private static final String EXCEL_XLSX = "xlsx";  
	 
	    public static Workbook getWorkbook(File file) throws IOException{
	        Workbook wb = null;
	        FileInputStream in = new FileInputStream(file);  
	        if(file.getName().endsWith(EXCEL_XLS)){  //Excel 2003  
	            wb = new HSSFWorkbook(in);
	            System.out.println("Excel file type:xls");
	        }else if(file.getName().endsWith(EXCEL_XLSX)){  // Excel 2007/2010  
			wb = new XSSFWorkbook(in);
	            System.out.println("Excel file type:xlsx");
	        }  
	        return wb;  
	    }  
}
