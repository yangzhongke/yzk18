package com.yzk18.docs;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <div lang="zh-cn">从指定单元格范围构建图表的构建器。</div>
 */
public class ChartFromCellRangeBuilder extends XDDFChartBuilder{
    private CellRangeAddress categoriesCellRange;
    private XSSFSheet sheet;

    //use LinkedHashSet to keep data stored sequentially
    private Map<String,CellRangeAddress> dataCellRanges =new LinkedHashMap<>();

    /**
     * <div lang="zh-cn">创建构建器实例</div>
     * @param charType <div lang="zh-cn">图表类型</div>
     * @param sheet <div lang="zh-cn"></div>
     */
    public ChartFromCellRangeBuilder(ChartTypes charType,XSSFSheet sheet)
    {
        super(charType);
        this.sheet = sheet;
    }

    /**
     * <div lang="zh-cn">设定类别的单元格范围。</div>
     * @param range <div lang="zh-cn"></div>
     * @return
     */
    public ChartFromCellRangeBuilder setCategoriesCellRange(CellRangeAddress range) {
        this.categoriesCellRange = range;
        return this;
    }

    /**
     * <div lang="zh-cn">添加数据库。</div>
     * @param title <div lang="zh-cn">标题</div>
     * @param range <div lang="zh-cn">对应数据的单元格范围</div>
     * @return
     */
    public ChartFromCellRangeBuilder putCellRanges(String title, CellRangeAddress range)
    {
        dataCellRanges.put(title,range);
        return this;
    }

    /**
     * <div lang="zh-cn">把数据绘制到XDDFChart</div>
     * @param chart
     */
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
