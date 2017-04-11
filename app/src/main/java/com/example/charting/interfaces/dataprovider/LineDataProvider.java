package com.example.charting.interfaces.dataprovider;

import com.example.charting.components.YAxis;
import com.example.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
