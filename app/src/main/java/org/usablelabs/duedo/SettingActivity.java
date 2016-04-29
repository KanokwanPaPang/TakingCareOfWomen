package org.usablelabs.duedo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

public class SettingActivity extends BaseActivity {

    private EditText numberPicker,numberPicker2;
    private Button btn_save,btn_back;
    private static final int MenuItem_SaveID = 1;

    public static SharedPreferences sharedpreferences;
    public static final String APP_PREFERENCES = "Period";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setDrawer(true);
        setTitle("ตั้งค่า");

        numberPicker = (EditText)findViewById(R.id.numberPicker);
        numberPicker2 = (EditText)findViewById(R.id.numberPicker2);

        sharedpreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        numberPicker.setText(sharedpreferences.getString("numberPicker","0"));
        numberPicker2.setText(sharedpreferences.getString("numberPicker2","0"));

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
                onBackPressed();
                break;
            case MenuItem_SaveID:
                save();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("numberPicker", numberPicker.getText().toString());
        editor.putString("numberPicker2", numberPicker2.getText().toString());
        editor.commit();

        SettingActivity.this.finish();
    }
}
