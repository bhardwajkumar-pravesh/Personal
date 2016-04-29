

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
set CLASSPATH=%CLASSPATH%;C:\Users\prabhard\Desktop\ExcelDemosWithPOI\ExcelDemosWithPOI\lib\poi-3.9-20121203.jar;C:\Users\prabhard\Desktop\ExcelDemosWithPOI\ExcelDemosWithPOI\lib\dom4j-1.6.1.jar;C:\Users\prabhard\Desktop\ExcelDemosWithPOI\ExcelDemosWithPOI\lib\poi-ooxml-3.9-20121203.jar;C:\Users\prabhard\Desktop\ExcelDemosWithPOI\ExcelDemosWithPOI\lib\poi-ooxml-schemas-3.9-20121203.jar;C:\Users\prabhard\Desktop\ExcelDemosWithPOI\ExcelDemosWithPOI\lib\xmlbeans-2.3.0.jar;.
*/

public class RunningHrs
{
	public static void main(String[] args) 
	{
		
		List list = new ArrayList();
		try
		{
			FileInputStream file = new FileInputStream(new File(args[0]));
			
			long totHh=0,totMm=0,totSs=0,totalRowDuration=0;
			
			//Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			//Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			//Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) 
			{
				Row row = rowIterator.next();
				//For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();
				Date dateFrom =null;
				Date timeFrom=/null;
				Date toDate =null;
				Date toTime=null;
				int cellCtr=1;
				while (cellIterator.hasNext()) 
				{
					Cell cell = cellIterator.next();
					//Check the cell type and format accordingly
					Date value = null;
					switch (cell.getCellType()) 
					{
						case Cell.CELL_TYPE_NUMERIC:
							value= cell.getDateCellValue();
							break;
						case Cell.CELL_TYPE_STRING:
							//System.out.print(cell.getStringCellValue()+ "\t");
							break;
					}
					if (cellCtr ==2 && value !=null)
						dateFrom=value;
					if (cellCtr ==3 && value !=null)
						timeFrom=value;
					if (cellCtr ==4 && value !=null)
						toDate=value;
					if (cellCtr ==5 && value !=null)
						toTime=value;
				
					if(dateFrom != null && timeFrom != null && cellCtr==4){
						dateFrom.setHours(timeFrom.getHours());
						dateFrom.setMinutes(timeFrom.getMinutes());
						dateFrom.setSeconds(timeFrom.getSeconds());
						//System.out.print(dateFrom + "\t");
					}
					
					if(toDate != null && toTime != null && cellCtr==5){
						toDate.setHours(toTime.getHours());
						toDate.setMinutes(toTime.getMinutes());
						toDate.setSeconds(toTime.getSeconds());
						//System.out.print(toDate + "\t");
						
					}
					
					if (cellCtr==5 && dateFrom!= null && toDate !=null){
						long rowDuration = Math.abs(dateFrom.getTime() - toDate.getTime());
						//System.out.print("\n row duration  : "+ (rowDuration));
						long hh=rowDuration/3600000;
						rowDuration=rowDuration%3600000;
						long mm= rowDuration/60000;
						long ss= rowDuration%60000;
						totHh=totHh+hh;
						totMm=totMm+mm;
						totSs=totSs+ss;
						list.add(((hh<10)?"0"+hh:hh)+":"+((mm<10)?"0"+mm:mm)+":"+((ss<10)?"0"+ss:ss));
						//System.out.print(", hh:"+hh+", mm:"+mm+", ss:"+ss+"");
					}
					cellCtr++;
				}
				
					System.out.println();
				
			}//end while
			
						totalRowDuration=(totHh*3600)+(totMm*60)+totSs;
						//System.out.println("\n totalRowDuration  : "+ (totalRowDuration));
						long hh=totalRowDuration/3600;
						totalRowDuration=totalRowDuration%3600;
						long mm= totalRowDuration/60;
						long ss= totalRowDuration%60;
						list.add(((hh<10)?"0"+hh:hh)+":"+((mm<10)?"0"+mm:mm)+":"+((ss<10)?"0"+ss:ss));// total number of hh, mm, ss
						//System.out.println(list);
			
			file.close();
			
			
		
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		/** Updating existing excel */
		try{
			FileInputStream file = new FileInputStream(new File(args[0]));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			
			if(rowIterator.hasNext())
				rowIterator.next();
			int i=0;
			while (rowIterator.hasNext()) 
			{
				Row row = rowIterator.next();
				Cell cell = row.createCell(5);
				cell.setCellValue(list.get(i)+"");
				i++;
			}
			Row row =sheet.createRow(list.size());
			Cell cell = row.createCell(5);
			cell.setCellValue(list.get(i)+"");
			
		 file.close();
		 FileOutputStream out = new FileOutputStream(new File(args[0]));
		 workbook.write(out);
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("Running Hours Calculation done !!! \n Please open "+ args[0] + " file to print");
	}
}
