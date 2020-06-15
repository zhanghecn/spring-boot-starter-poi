package com.zhanghe.poi.autoconfig.entity;

import com.zhanghe.poi.util.excel.sheet.AbstractSheetInfoType;
import com.zhanghe.poi.util.excel.sheet.SheetHandler;
import com.zhanghe.poi.util.excel.sheet.SheetHandlerInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * sheet封装迭代器
 * 可以迭代sheet 中每一行转换的对象
 * @param <T>
 */
public class SheetHandlerWrap<T> implements Iterable<T>{

    /**
     * 操作sheet 转换 实体类的适配器
     */
   private SheetHandler sheetHandlerAdapter;

    /**
     * 来自那个上传的文件
     */
   private MultipartFile file;
    /**
     * sheet信息
     */
   private SheetHandlerInfo sheetHandlerInfo;

   //Sheet页的头部信息
   private Map<String,Integer> headers;

   public SheetHandlerWrap(AbstractSheetInfoType sheetHandlerAdapter){
       this.sheetHandlerInfo = sheetHandlerAdapter.getSheetInfo();
       this.sheetHandlerAdapter = (SheetHandler) sheetHandlerAdapter;
       this.headers = SheetHandler.getSheetHeaders(sheetHandlerInfo);
   }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Map<String, Integer> getHeaders() {
        return headers;
    }

    public SheetHandler getSheetHandlerAdapter() {
        return sheetHandlerAdapter;
    }

    public SheetHandlerInfo getSheetHandlerInfo() {
        return sheetHandlerInfo;
    }

    /**
     * 迭代每一行的操作类
     */
    public  class RowObjectIterator  implements Iterator<T>{
       private volatile   int rowIndex;


        public RowObjectIterator(int rowIndex) {
            this.rowIndex = rowIndex;

        }

        @Override
       public boolean hasNext() {
            int lastRowNum = SheetHandlerWrap.this.sheetHandlerInfo.getRowNum();
            return lastRowNum>=rowIndex;
       }

       @Override
       public T next() {
           Assert.isTrue(hasNext(),"excel行超出");
           SheetHandler sheetHandlerAdapter = SheetHandlerWrap.this.sheetHandlerAdapter;
           SheetHandlerInfo sheetHandlerInfo = SheetHandlerWrap.this.sheetHandlerInfo;
           Sheet sheet = sheetHandlerInfo.getSheet();
           T object = (T) sheetHandlerAdapter.getObject(sheet, this.rowIndex);
           this.rowIndex++;
           return object;
       }
   }

    /**
     * 创造行的迭代器
     * @return
     */
    @Override
    public Iterator<T> iterator() {
        int startRow = sheetHandlerInfo.getStartRow();
        return new RowObjectIterator(startRow);
    }

    /**
     * 循环迭代
     * @param action
     */
    @Override
    public void forEach(Consumer<? super T> action) {
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()){
            T next = iterator.next();
            action.accept(next);
        }
   }

    @Override
    public Spliterator<T> spliterator() {
        return null;
    }
}
