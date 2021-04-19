package com.yzk18.docs;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChartFromCellRangeBuilder extends XDDFChartBuilder{
    private CellRangeAddress categoriesCellRange;
    private XSSFSheet sheet;

    //use LinkedHashSet to keep data stored sequentially
    private Map<String,CellRangeAddress> dataCellRanges =new LinkedHashMap<>();

    public ChartFromCellRangeBuilder(ChartTypes charType,XSSFSheet sheet)
    {
        super(charType);
        this.sheet = sheet;
    }

    public ChartFromCellRangeBuilder setCategoriesCellRange(CellRangeAddress range) {
        this.categoriesCellRange = range;
        return this;
    }

    public ChartFromCellRangeBuilder putCellRanges(String title, CellRangeAddress range)
    {
        dataCellRanges.put(title,range);
        return this;
    }

    @Override
    public void build(XDDFChart chart) {
        if(categoriesCellRange==null)
        {
            throw new RuntimeException("categoriesCellRange cannot be null");
        }
        if(this.sheet==null)
        {
            throw new RuntimeException("sheet cannot be null");
        }
        super.build(chart);
    }

    @Override
    protected void fillData(XDDFChartData chartData) {
        XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory
                .fromStringCellRange(this.sheet, this.categoriesCellRange);
        for(Map.Entry<String,CellRangeAddress> entry : this.dataCellRanges.entrySet())
        {
            String title = entry.getKey();
            CellRangeAddress cellRange = entry.getValue();
            XDDFNumericalDataSource<Double> valuesDataSource = XDDFDataSourcesFactory.fromNumericCellRange(this.sheet, cellRange);
            XDDFChartData.Series series = chartData.addSeries(categoriesData, valuesDataSource);
            series.setTitle(title,null);
        }
    }
}
