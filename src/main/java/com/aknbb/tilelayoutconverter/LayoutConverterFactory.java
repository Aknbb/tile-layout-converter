package com.aknbb.tilelayoutconverter;

import com.aknbb.tilelayoutconverter.LayoutConverters.GWCLayoutConverter;
import com.aknbb.tilelayoutconverter.LayoutConverters.ILayoutConverter;
import com.aknbb.tilelayoutconverter.LayoutConverters.TMSLayoutConverter;
import com.aknbb.tilelayoutconverter.LayoutConverters.XYZLayoutConverter;

class LayoutConverterFactory {
    static ILayoutConverter getLayoutConverter(Constants.LayoutTypes layoutType) {
        switch (layoutType) {
            case GWC:
                return new GWCLayoutConverter();
            case XYZ:
                return new XYZLayoutConverter();
            default:
                return new TMSLayoutConverter();
        }
    }
}
