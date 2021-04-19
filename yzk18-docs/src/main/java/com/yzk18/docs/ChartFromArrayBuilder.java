package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import org.apache.poi.xddf.usermodel.chart.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChartFromArrayBuilder<T extends  Number> extends  XDDFChartBuilder{
    protected String[] categoryNames;

    //use LinkedHashMap to keep data stored sequentially
    private LinkedHashMap<String,T[]> data =new LinkedHashMap<>();

    public ChartFromArrayBuilder(ChartTypes charType)
    {
        super(charType);
    }
    public ChartFromArrayBuilder<T> setCategoryNames(String[] categoryNames) {
        this.categoryNames = categoryNames;
        return this;
    }

    public ChartFromArrayBuilder<T> putValues(String title, T[] values) {
        this.data.put(title,values);
        return this;
    }

    @Override
    protected void fillData(XDDFChartData chartData) {
        XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categoryNames);
        for(Map.Entry<String, T[]> entry : this.data.entrySet())
        {
            XDDFNumericalDataSource<T> valuesDataSource = XDDFDataSourcesFactory.fromArray(entry.getValue());
            XDDFChartData.Series series = chartData.addSeries(categoriesData, valuesDataSource);
            series.setTitle(entry.getKey(),null);
        }
    }

    @Override
    public void build(XDDFChart chart) {
        if(CommonHelpers.isEmpty(categoryNames))
        {
            throw new RuntimeException("categoryNames can be empty.");
        }
        super.build(chart);
    }
}
