package org.usablelabs.duedo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;


public class SexDetialActivity extends BaseActivity {

    private Sex db = null;

    private EditText titleEdit; // start
    CheckBox check1,check2,check3,check4,check5;

    private static final int MenuItem_SaveID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sex_detial);
        setDrawer(true);
        setTitle(R.string.sex_add);

        titleEdit = (EditText) findViewById(R.id.sexdateEdit);

        check1 = (CheckBox)findViewById(R.id.checkBox1);
        check2 = (CheckBox)findViewById(R.id.checkBox2);
        check3 = (CheckBox)findViewById(R.id.checkBox3);
        check4 = (CheckBox)findViewById(R.id.checkBox4);
        check5 = (CheckBox)findViewById(R.id.checkBox5);
    }

    public void onStart() {
        super.onStart();
        EditText dateEdit = (EditText) findViewById(R.id.sexdateEdit);
        dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        addMenuItem(menu, MenuItem_SaveID, R.string.save, buildDrawable(MaterialDesignIconic.Icon.gmi_save));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            case MenuItem_SaveID:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save() {
        if (titleEdit.getText().length() > 0) {
            if (db == null)
                db = new Sex();
            db.date = titleEdit.getText().toString();
            db.v1 = check1.isChecked();
            Log.e("c1",check1.isChecked()+"");
            db.v2 = check2.isChecked();
            Log.e("c1",check2.isChecked()+"");
            db.v3 = check3.isChecked();
            Log.e("c1",check3.isChecked()+"");
            db.v4 = check4.isChecked();
            Log.e("c1",check4.isChecked()+"");
            db.v5 = check5.isChecked();
            Log.e("c1",check5.isChecked()+"");
            db.saveWithTimestamp();
            setResult(Activity.RESULT_OK, new Intent().putExtra("id", db.getId()));
            this.finish();

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(android.R.string.dialog_alert_title);
            alert.setMessage(R.string.title_is_required);
            alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
    }
}

