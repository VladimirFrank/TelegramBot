package ru.frank.dataParser;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelReader {

    private XSSFWorkbook excelBook;
    private XSSFSheet excelSheet;

    /**
     *  Method is seeking incoming String in excel file.
     * @param path - path to .xlxs file;
     * @param stringToFind - sought value;
     * @return String;
     * @throws IOException
     */
    public String findRowByValue(String path, String stringToFind) throws IOException{
        excelBook = new XSSFWorkbook(new FileInputStream(path));
        excelSheet = excelBook.getSheetAt(0);
        Iterator rowIterator = excelSheet.rowIterator();
        String result;

        while(rowIterator.hasNext()){
            XSSFRow row = (XSSFRow) rowIterator.next();
            Iterator cellIterator = row.cellIterator();
            while(cellIterator.hasNext()){
                XSSFCell cell = (XSSFCell) cellIterator.next();
                if(cell.getStringCellValue().contains(stringToFind)){
                    return getRowValuesAsString(row);
                }
            }
        }

        return "";
    }

    private String getRowValuesAsString(XSSFRow row){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator cellIterator = row.cellIterator();
        while(cellIterator.hasNext()){
            XSSFCell cell = (XSSFCell) cellIterator.next();
            stringBuilder.append(cell.getStringCellValue()).append(" | ");
        }
        return stringBuilder.toString();
    }
}
