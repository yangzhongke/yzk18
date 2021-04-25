package com.yzk18.docs;

import org.apache.poi.xddf.usermodel.chart.*;
import java.util.function.BiConsumer;

/**
 * <div lang="zh-cn">创建图表的构建器</div>
 */
public abstract class XDDFChartBuilder {

    protected ChartTypes charType;
    protected String categoryAxisTitle;
    protected String valueAxisTitle;

    private BiConsumer<XDDFChartData, XDDFChart> configuratorConsumer;

    public XDDFChartBuilder(ChartTypes charType)
    {
        if(charType==ChartTypes.SURFACE||charType==ChartTypes.SURFACE3D)
        {
            throw new IllegalArgumentException("SURFACE and SURFACE3D are not supported.");
        }
        this.charType = charType;
    }

    /**
     * <div lang="zh-cn">设置“类别”轴的标题。</div>
     * @param categoryAxisTitle
     * @return
     */
    public XDDFChartBuilder setCategoryAxisTitle(String categoryAxisTitle)
    {
        this.categoryAxisTitle = categoryAxisTitle;
        return this;
    }

    /**
     * <div lang="zh-cn">设置“数值”轴的标题。</div>
     * @param valueAxisTitle
     * @return
     */
    public XDDFChartBuilder setValueAxisTitle(String valueAxisTitle)
    {
        this.valueAxisTitle = valueAxisTitle;
        return this;
    }

    /**
     * <div lang="zh-cn">设置对图表进行配置的回调。</div>
     * @param consumer
     * @return
     */
    public XDDFChartBuilder setConfigurator(BiConsumer<XDDFChartData, XDDFChart> consumer)
    {
        this.configuratorConsumer = consumer;
        return this;
    }

    /**
     * <div lang="zh-cn">把数据填充到chart图表中</div>
     * @param chart
     */
    public void build(XDDFChart chart)
    {
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
        fillData(chartData);
        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.LEFT);
        if(configuratorConsumer !=null)
        {
            configuratorConsumer.accept(chartData,chart);
        }
        chart.plot(chartData);
    }

    /**
     * <div lang="zh-cn">填充数据</div>
     * @param chartData
     */
    protected abstract void fillData(XDDFChartData chartData);
}
