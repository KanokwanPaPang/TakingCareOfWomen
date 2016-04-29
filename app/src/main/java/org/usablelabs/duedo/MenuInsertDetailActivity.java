package org.usablelabs.duedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuInsertDetailActivity extends BaseActivity{

    private Button btn_1,btn_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_insert_detail);
        setDrawer(true);
        setTitle("เพิ่มข้อมูลของฉัน");



        btn_1 = (Button)findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PeriodActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_2 = (Button)findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FormSexActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
