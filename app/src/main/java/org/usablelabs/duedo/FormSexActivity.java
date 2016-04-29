package org.usablelabs.duedo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FormSexActivity extends BaseActivity {

    private static final int SHOW_TASK = 2;

    private ArrayList<Sex> sex;

    private ListView listView;
    private TextView emptyLabel;
    private SexAdapter adapter;
    private Button btn_add;
    private int index_protect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sex);
        setDrawer(true);
        setTitle("การมีเพศสัมพันธ์");

        listView = (ListView) findViewById(R.id.listView);
        emptyLabel = (TextView) findViewById(R.id.emptyLabel);
        //setView();

        btn_add = (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormSexActivity.this,SexDetialActivity.class);
                startActivity(intent);
            }
        });

        setView();
    }

    private void setView() {
        sex = new ArrayList<Sex>(Sex.getAll());
        if (sex.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyLabel.setVisibility(View.VISIBLE);
        } else {
            emptyLabel.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new SexAdapter(this, sex);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(FormSexActivity.this, SexShowActivity.class);
                    intent.putExtra("id", sex.get(position).getId());
                    startActivityForResult(intent, SHOW_TASK);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        index_protect = 0;
        Log.e("LOG", "onrestrat");
        sex = new ArrayList<Sex>(Sex.getAll());
        adapter = new SexAdapter(this, sex);
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

    class SexAdapter extends ArrayAdapter<Sex> {

        String olddate;
        private ArrayList<Sex> alltasks;

        public SexAdapter(Context context, ArrayList<Sex> tasks) {
            super(context, 0, tasks);
            alltasks = tasks;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Task task = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_sex, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.txt_date);
            tv.setText(alltasks.get(position).date);

            String protect = "";

            if(alltasks.get(position).v1){
                protect += "ไม่ป้องกัน";
                index_protect++;
            }

            if(alltasks.get(position).v2){
                if(index_protect==0) {
                    protect += "ใส่ถุงยาง";
                }else{
                    protect += ", ใส่ถุงยาง";
                }
                index_protect++;
            }

            if(alltasks.get(position).v3){
                if(index_protect==0)
                    protect += "กินยาคุมกำเนิด";
                else
                    protect += ", กินยาคุมกำเนิด";
                index_protect++;
            }

            if(alltasks.get(position).v4){
                if(index_protect==0)
                    protect += "หลั่งนอก";
                else
                    protect += ", หลั่งนอก";
                index_protect++;
            }

            if(alltasks.get(position).v5){
                if(index_protect==0)
                    protect += "ใส่ห่วง";
                else
                    protect += ", ใส่ห่วง";
                index_protect++;
            }

            index_protect = 0;

            TextView tv2 = (TextView) convertView.findViewById(R.id.txt_protect);
            tv2.setText(protect);

            return convertView;
        }
    }
}
