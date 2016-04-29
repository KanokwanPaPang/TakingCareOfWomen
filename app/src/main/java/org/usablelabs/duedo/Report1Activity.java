package org.usablelabs.duedo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Report1Activity  extends BaseActivity {

    private static final int SHOW_TASK = 2;

    private ArrayList<Task> tasks;

    private ListView listView;
    private TextView emptyLabel;
    private TasksReport1Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report1);
        setDrawer(true);
        setTitle(R.string.myperiod);

        listView = (ListView) findViewById(R.id.listView);
        emptyLabel = (TextView) findViewById(R.id.emptyLabel);
        setView();
    }

    private void setView() {
        tasks = new ArrayList<Task>(Task.getAll());
        if (tasks.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyLabel.setVisibility(View.VISIBLE);
        } else {
            emptyLabel.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new TasksReport1Adapter(this, tasks);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Report1Activity.this, PeriodShowActivity.class);
                    intent.putExtra("id", tasks.get(position).getId());
                    startActivityForResult(intent, SHOW_TASK);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        tasks = new ArrayList<Task>(Task.getAll());
        adapter = new TasksReport1Adapter(this, tasks);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class TasksReport1Adapter extends ArrayAdapter<Task> {

        String olddate;
        private ArrayList<Task> alltasks;

        public TasksReport1Adapter(Context context, ArrayList<Task> tasks) {
            super(context, 0, tasks);
            alltasks = tasks;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Task task = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_main, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.txt_title);

            TextView tv2 = (TextView) convertView.findViewById(R.id.txt_content);


            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate = null;
            try {
                startDate = df.parse(alltasks.get(position).title);
                String newDateString = df.format(startDate);
                System.out.println(newDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                olddate = alltasks.get(position + 1).title;
            }catch (IndexOutOfBoundsException ex){
                olddate = null;
            }

            Date endDate = null;
            if(olddate!=null) {
                try {
                    endDate = df.parse(olddate);
                    String newDateString = df.format(endDate);
                    System.out.println(newDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String DateDiff = "-";
            int diffInDays = 0;
            if(olddate!=null) {
                diffInDays = (int) ((startDate.getTime() - endDate.getTime())
                        / (1000 * 60 * 60 * 24));
                diffInDays = diffInDays-15;
                DateDiff = diffInDays+"";
            }

            TextView tv3 = (TextView) convertView.findViewById(R.id.txt_cycle);

            tv.setText(dff.format(startDate));
            if(position != (alltasks.size()-1)){
                Date date2 = new Date(startDate.getTime() + (9 * (1000 * 60 * 60 * 24)));
                Date date4 = new Date(date2.getTime() + (6 * (1000 * 60 * 60 * 24)));
                tv2.setText(dff.format(date2)+" - "+dff.format(date4));

                Date date3 = new Date(date4.getTime() + (-1 * (1000 * 60 * 60 * 24)));
                tv3.setText(dff.format(date3));
            }else{
                tv2.setText("-");
                tv3.setText("-");
            }


            return convertView;
        }
    }
}
