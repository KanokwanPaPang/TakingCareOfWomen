package org.usablelabs.duedo;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LineChartActivity extends BaseActivity {

    private ArrayList<Sex> sex;
    private ArrayList<Integer> month;
    ArrayList<String> xAxis;
    ArrayList<Integer> yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        setTitle("กราฟการมีเพศสัมพันธ์แต่ละเดือน");
        setDrawer(true);
        //BarChart chart = (BarChart) findViewById(R.id.chart);

        sex = new ArrayList<Sex>(Sex.getAll());
        month = new ArrayList<>();
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);
        month.add(0);

        yes = new ArrayList<>();
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);
        yes.add(0);

        getXAxisValues();

        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        // creating list of entry

        LineDataSet dataset = new LineDataSet(getDataSet(), "จำนวณ");
        dataset.setColor(ColorTemplate.rgb("#2E2EFE"));
        dataset.setCircleColor(ColorTemplate.rgb("#000000"));
        LineData data = new LineData(xAxis, dataset);

        lineChart.setData(data);
        lineChart.setDescription("");


        /*chart.setDescription("");
        chart.animateXY(2000, 2000);
        chart.invalidate();*/
    }

    private ArrayList<Entry> getDataSet() {
        ArrayList<Entry> dataSets = new ArrayList<>();

        int num = 0;
        for (int i=0;i<yes.size();i++){
            if(yes.get(i)>0) {
                dataSets.add(new Entry(yes.get(i), num));
                num++;
            }
        }
        return dataSets;
    }

    private void getXAxisValues() {
        xAxis = new ArrayList<>();
        for (int i=0;i<sex.size();i++){

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = null;
            try {
                startDate = df.parse(sex.get(i).date);
                String newDateString = df.format(startDate);
                System.out.println(newDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            startDate.getMonth();
            month.set(startDate.getMonth(), month.get(startDate.getMonth()) + 1);

            yes.set(startDate.getMonth(), yes.get(startDate.getMonth()) + 1);

        }

        for(int i=0;i<12;i++){
            String value = "";
            if(month.get(i)>0){
                switch (i){
                    case 0:
                        value = "JAN";
                        break;
                    case 1:
                        value = "FAB";
                        break;
                    case 2:
                        value = "MAR";
                        break;
                    case 3:
                        value = "APR";
                        break;
                    case 4:
                        value = "MAY";
                        break;
                    case 5:
                        value = "JUN";
                        break;
                    case 6:
                        value = "JUL";
                        break;
                    case 7:
                        value = "AUG";
                        break;
                    case 8:
                        value = "SEP";
                        break;
                    case 9:
                        value = "OCT";
                        break;
                    case 10:
                        value = "NOV";
                        break;
                    case 11:
                        value = "DEC";
                        break;
                }
                xAxis.add(value);
            }
        }

    }

}
