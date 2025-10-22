package com.example.physicsmate.ui.home.maths.IntegralCalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.physicsmate.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import static com.example.physicsmate.misc.Custom_methods.*;

public class DerIntCalcOfflineGraph extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.line_chart_universal, container, false);

        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("No arguments passed to DerIntCalcOfflineGraph fragment");
        }

        String input = args.getString("input");
        String DerInt = args.getString("DerInt");
        float xmin = args.getFloat("minX", 0.1f);
        float xmax = args.getFloat("maxX", 50.0f);

        LineChart derCalcOffGraph = view.findViewById(R.id.lineChart);

        mChartProperties(derCalcOffGraph);
        ArrayList<Entry> inputLineArray = new ArrayList<>();
        ArrayList<Entry> derLineArray = new ArrayList<>();


        addArrayListEntries(input, xmin, xmax, inputLineArray);
        //________________________________________________________________________

        addArrayListEntries(DerInt, xmin, xmax, derLineArray);
        //________________________________________________________________________


        LineDataSet dataSet1 = new LineDataSet(inputLineArray, "Function");
        LineDataSet dataSet2 = new LineDataSet(derLineArray, "Derivative");
        dataSet1.setDrawCircles(false);
        dataSet1.setColor(Color.RED);
        dataSet1.setValueTextColor(Color.WHITE);
        dataSet2.setDrawCircles(false);
        dataSet2.setColor(Color.WHITE);
        dataSet2.setValueTextColor(Color.WHITE);

        // Create a LineData object with the dataSet
        LineData lineData = new LineData(dataSet1, dataSet2);
        lineData.setValueTextColor(Color.WHITE);

        // Set the data to the chart
        derCalcOffGraph.setData(lineData);

        // Invalidate the chart to refresh the view
        derCalcOffGraph.invalidate();

        return view;
    }
}
