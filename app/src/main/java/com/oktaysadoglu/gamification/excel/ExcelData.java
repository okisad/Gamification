package com.oktaysadoglu.gamification.excel;

import android.content.Context;

import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.model.BaseWord;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by oktaysadoglu on 10/12/15.
 */
public class ExcelData {

    private Context mContext;

    public ExcelData(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<BaseWord> getWords(){

        ArrayList<BaseWord> words = new ArrayList<>();

        InputStream inputStream = mContext.getResources().openRawResource(R.raw.detailedfreq);

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

            HSSFSheet sheet = workbook.getSheetAt(0);

            Iterator rowIterator = sheet.rowIterator();

            while (rowIterator.hasNext()){

                Row row = (Row) rowIterator.next();

                String word = row.getCell(0).getStringCellValue().trim();

                if(row.getCell(1) == null){

                    continue;

                }

                String mean = row.getCell(1).getStringCellValue().trim();

                words.add(new BaseWord(word,mean));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return words;
    }

}
