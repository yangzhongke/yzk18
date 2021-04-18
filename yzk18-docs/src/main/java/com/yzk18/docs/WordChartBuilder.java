package com.yzk18.docs;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WordChartBuilder<T extends  Number> {
    private int width=300;
    private int height=150;
    private String[] categoryNames;
    private LinkedHashMap<String,T[]> data =new LinkedHashMap<>();
    private ChartTypes charType;
    private String categoryAxisTitle;
    private String valueAxisTitle;

    private BiConsumer<XDDFChartData,XWPFChart> configuratorConsumer;

    public WordChartBuilder(ChartTypes charType)
    {
        if(charType==ChartTypes.SURFACE||charType==ChartTypes.SURFACE3D)
        {
            throw new IllegalArgumentException("SURFACE and SURFACE3D are not supported.");
        }
        this.charType = charType;
    }

    public WordChartBuilder<T> setSize(int width,int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public WordChartBuilder<T> setCategoryNames(String[] categoryNames) {
        this.categoryNames = categoryNames;
        return this;
    }

    public WordChartBuilder<T> putValues(String title,T[] values) {
        this.data.putIfAbsent(title,values);
        return this;
    }

    public WordChartBuilder<T> setCategoryAxisTitle(String categoryAxisTitle) {
        this.categoryAxisTitle = categoryAxisTitle;
        return this;
    }

    public WordChartBuilder<T> setValueAxisTitle(String valueAxisTitle) {
        this.valueAxisTitle = valueAxisTitle;
        return this;
    }

    public WordChartBuilder<T> setConfigurator(BiConsumer<XDDFChartData, XWPFChart> consumer) {
        this.configuratorConsumer = consumer;
        return this;
    }

    public XWPFChart build(XWPFDocument doc)
    {
        XWPFChart chart = null;
        try {
            chart = doc.createChart(Units.toEMU(this.width),Units.toEMU(this.height));
        } catch (InvalidFormatException|IOException e) {
           throw new RuntimeException(e);
        }

        XDDFChartData chartData;

        if(this.charType==ChartTypes.PIE||this.charType==ChartTypes.PIE3D
                ||this.charType==ChartTypes.DOUGHNUT)
        {
            chartData = chart.createData(this.charType, null, null);
        }
        else
        {
            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis valueAxis = chart.createValueAxis(AxisPosition.LEFT);
            if(this.categoryAxisTitle!=null)
            {
                categoryAxis.setTitle(this.categoryAxisTitle);
            }
            if(this.valueAxisTitle!=null)
            {
                valueAxis.setTitle(this.valueAxisTitle);
            }
            chartData = chart.createData(this.charType, categoryAxis, valueAxis);
        }
        if(this.charType==ChartTypes.BAR)
        {
            ((XDDFBarChartData)chartData).setBarDirection(BarDirection.COL);
        }

        XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categoryNames);
        for(Map.Entry<String, T[]> entry : this.data.entrySet())
        {
            XDDFNumericalDataSource<T> valuesDataSource = XDDFDataSourcesFactory.fromArray(entry.getValue());
            XDDFChartData.Series series = chartData.addSeries(categoriesData, valuesDataSource);
            series.setTitle(entry.getKey(),null);
        }
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.LEFT);
        if(configuratorConsumer !=null)
        {
            configuratorConsumer.accept(chartData,chart);
        }
        chart.plot(chartData);
        return chart;
    }
}
