package com.json.comparision.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DocumentGeneratorService {
    
    public void writeToExcel(Map<String, Object> map, String file){
        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet();

        //Iterate over data and write to sheet
        if(null!=map && map.size()>0){

            final int[] index = {0};

            map.entrySet().stream().forEach(entry->{
                Row row = sheet.createRow(index[0]);

                Cell headerCell = row.createCell(0);
                headerCell.setCellValue(entry.getKey());
                int columnIndex = headerCell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
                CellStyle cellStyle = headerCell.getSheet().getWorkbook().createCellStyle();
                cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                cellStyle.setFillPattern(FillPatternType.SPARSE_DOTS);
                cellStyle.setBorderBottom(BorderStyle.DASHED);
                cellStyle.setBorderTop(BorderStyle.HAIR);
                cellStyle.setBorderRight(BorderStyle.HAIR);
                headerCell.setCellStyle(cellStyle);

                Cell dataCell = row.createCell(1);
                dataCell.setCellValue((String)entry.getValue());
                columnIndex = dataCell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
                if(((String) entry.getValue()).contains("Not Match")){
                    cellStyle = dataCell.getSheet().getWorkbook().createCellStyle();
                    cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SPARSE_DOTS);
                    cellStyle.setBorderBottom(BorderStyle.DASHED);
                    cellStyle.setBorderTop(BorderStyle.DASHED);
                    cellStyle.setBorderRight(BorderStyle.DASHED);
                    dataCell.setCellStyle(cellStyle);
                }

                index[0]++;
            });

            try
            {
                //Write the workbook in file system
                FileOutputStream out = new FileOutputStream(new File(file));
                workbook.write(out);
                out.close();
                System.out.println("Write to excel completed!!!");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
