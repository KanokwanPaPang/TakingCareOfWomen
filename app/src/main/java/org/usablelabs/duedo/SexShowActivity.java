package org.usablelabs.duedo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

public class SexShowActivity extends BaseActivity {

    private Sex    task = null;
    private TextView titleView;
    private TextView contentView;

    private static final int MenuItem_EditID = 1;
    private static final int MenuItem_DeleteID = 2;
    private static final int EDIT_TASK = 14;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_show);
        setDrawer(true);
        setTitle("ข้อมูลการมีเพศสัมพันธ์");

        titleView = (TextView) findViewById(R.id.titleView);
        contentView = (TextView) findViewById(R.id.contentView);

        id = getIntent().getLongExtra("id", 0);
        setView(id);
    }

    private void setView(long id) {
        if (id > 0)
            task = Sex.load(Sex.class, id);
        if (task != null) {
            titleView.setText(task.date);

            String protect = "";

            int num = 0;
            if(task.v1){
                if(num>0) {
                    protect += "ไม่ป้องกัน";
                }else{
                    protect += "ไม่ป้องกัน";
                }
                num++;
            }

            if(task.v2){
                if(num>0) {
                    protect += ", ใส่ถุงยาง";
                }else{
                    protect += "ใส่ถุงยาง";
                }
                num++;
            }

            if(task.v3){
                if(num>0) {
                    protect += ", กินยาคุมกำเนิด";
                }else{
                    protect += "กินยาคุมกำเนิด";
                }
                num++;
            }

            if(task.v4){
                if(num>0) {
                    protect += ", หลั่งนอก";
                }else{
                    protect += "หลั่งนอก";
                }
                num++;
            }

            if(task.v5){
                if(num>0) {
                    protect += ", ใส่ห่วง";
                }else{
                    protect += "ใส่ห่วง";
                }
                num++;
            }

            contentView.setText(protect);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addMenuItem(menu, MenuItem_EditID, R.string.edit, buildDrawable(MaterialDesignIconic.Icon.gmi_edit));
        addMenuItem(menu, MenuItem_DeleteID, R.string.delete, buildDrawable(MaterialDesignIconic.Icon.gmi_delete));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MenuItem_EditID:
                Log.e("Edit","Edit");
                Intent intent = new Intent(this, Form2SexActivity.class);
                intent.putExtra("id", task.getId());
                startActivity(intent);
                this.finish();
                break;
            case MenuItem_DeleteID:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(android.R.string.dialog_alert_title);
                alert.setMessage(R.string.are_you_sure);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task.delete();
                        setResult(Activity.RESULT_OK, new Intent().putExtra("refreshNeeded", true));
                        finish();
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case EDIT_TASK:
                    if (extras != null && extras.getLong("id", 0) > 0) {
                        setView(task.getId());
                        setResult(Activity.RESULT_OK, new Intent().putExtra("refreshNeeded", true));
                    }
                    break;
            }
        }
    }*/
}
