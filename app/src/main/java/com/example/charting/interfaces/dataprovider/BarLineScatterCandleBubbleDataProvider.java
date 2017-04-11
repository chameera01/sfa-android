package com.example.charting.interfaces.dataprovider;

import com.example.charting.components.YAxis.AxisDependency;
import com.example.charting.data.BarLineScatterCandleBubbleData;
import com.example.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
