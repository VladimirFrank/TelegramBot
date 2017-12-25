package ru.frank.dataParser;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {

    private XSSFWorkbook excelBook;
    private XSSFSheet excelSheet;

    public String findLineInExcelFile(String filePath, String lineToFind) throws IOException{
        return getFindedRowAsString(findRowByValue(filePath, lineToFind));
    }


    /**
     *
     * @param listOfStrings
     * @return
     */
    private String getFindedRowAsString(List <String> listOfStrings){
        if(listOfStrings.isEmpty()){
            return "";
        } else{
            StringBuilder sb = new StringBuilder();
            for(String element : listOfStrings){
                sb.append(element);
            }
            return sb.toString();
        }
    }


    /**
     *  Method is seeking incoming String in excel file.
     * @param path - path to .xlxs file;
     * @param stringToFind - sought value;
     * @return ArrayList <String> that contains stringToFind.contains() values;
     * @throws IOException
     */
    private ArrayList<String> findRowByValue(String path, String stringToFind) throws IOException{
        ArrayList <String> resultList = new ArrayList<String>();
        excelBook = new XSSFWorkbook(new FileInputStream(path));
        // Only on first sheet of file.
        excelSheet = excelBook.getSheetAt(0);
        Iterator rowIterator = excelSheet.rowIterator();
        while(rowIterator.hasNext()){
            XSSFRow row = (XSSFRow) rowIterator.next();
            Iterator cellIterator = row.cellIterator();
            while(cellIterator.hasNext()){
                XSSFCell cell = (XSSFCell) cellIterator.next();
                if(cell.getStringCellValue().contains(stringToFind)){
                    resultList.add(getRowValuesAsString(row));
                }
            }
        }

        return resultList;
    }

    /**
     * Method is return values from 1 row (of cells with any data) as a 1 string, split by '|'.
     * @param row;
     * @return Stringl
     */
    private String getRowValuesAsString(XSSFRow row){
        StringBuilder stringBuilder = new StringBuilder();
        Iterator cellIterator = row.cellIterator();
        stringBuilder.append("(");
        while(cellIterator.hasNext()){
            XSSFCell cell = (XSSFCell) cellIterator.next();
            stringBuilder.append(cell.getStringCellValue()).append(" | ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
