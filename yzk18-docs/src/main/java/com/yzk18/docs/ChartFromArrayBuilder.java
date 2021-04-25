package com.yzk18.docs;

import com.yzk18.commons.CommonHelpers;
import org.apache.poi.xddf.usermodel.chart.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <div lang="zh-cn">用于从数组构建图表的构建器。</div>
 * @param <T>
 */
public class ChartFromArrayBuilder<T extends  Number> extends  XDDFChartBuilder{
    protected String[] categoryNames;

    //use LinkedHashMap to keep data stored sequentially
    private LinkedHashMap<String,T[]> data =new LinkedHashMap<>();

    /**
     *
     * @param charType <div lang="zh-cn">图表类型</div>
     */
    public ChartFromArrayBuilder(ChartTypes charType)
    {
        super(charType);
    }

    /**
     * <div lang="zh-cn">指定显示的类别名称</div>
     * @param categoryNames
     * @return
     */
    public ChartFromArrayBuilder<T> setCategoryNames(String[] categoryNames) {
        this.categoryNames = categoryNames;
        return this;
    }

    /**
     * <div lang="zh-cn">放入要显示的数据。</div>
     * @param title
     * @param values
     * @return
     */
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

    /**
     * <div lang="zh-cn">把数据绘制到XDDFChart</div>
     * @param chart
     */
    @Override
    public void build(XDDFChart chart) {
        if(CommonHelpers.isEmpty(categoryNames))
        {
            throw new RuntimeException("categoryNames can be empty.");
        }
        super.build(chart);
    }
}
