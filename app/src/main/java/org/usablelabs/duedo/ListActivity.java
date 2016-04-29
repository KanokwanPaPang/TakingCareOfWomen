package org.usablelabs.duedo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListActivity extends BaseActivity {

    private static final int SHOW_TASK = 2;

    private ArrayList<Task> tasks;

    private ListView listView;
    private TextView emptyLabel, txt_detail, txt_detail1;
    private TasksAdapter adapter;
    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private CaldroidFragment dialogCaldroidFragment;
    public static SharedPreferences sharedpreferences;
    public static final String APP_PREFERENCES = "Period";
    public int predictDate = 0;
    public int predictLeght = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setDrawer(false);
        setTitle(R.string.mycal);

        sharedpreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        predictDate = Integer.parseInt(sharedpreferences.getString("numberPicker", "0"));
        predictLeght = Integer.parseInt(sharedpreferences.getString("numberPicker2", "0"));

        listView = (ListView) findViewById(R.id.listView);
        emptyLabel = (TextView) findViewById(R.id.emptyLabel);
        txt_detail = (TextView) findViewById(R.id.txt_detail);
        txt_detail1 = (TextView) findViewById(R.id.txt_detail1);


        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
//		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {

            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            switch (requestCode) {
                case NEW_TASK:
                    if (extras != null && extras.getLong("id", 0) > 0)
                        setView();
                    break;
                case SHOW_TASK:
                    if (extras != null && extras.getBoolean("refreshNeeded", false))
                        setView();
                    break;
            }
        }
    }

    private void setView() {
        tasks = new ArrayList<Task>(Task.getAll());
        if (tasks.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyLabel.setVisibility(View.VISIBLE);
        } else {
            emptyLabel.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new TasksAdapter(this, tasks);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ListActivity.this, PeriodShowActivity.class);
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
        adapter = new TasksAdapter(this, tasks);
        listView.setAdapter(adapter);

        setCustomResourceForDates();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "List Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.usablelabs.duedo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);

        setCustomResourceForDates2();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();
        tasks = new ArrayList<Task>(Task.getAll());

        Date nextpreiod_start = null;
        Date nextpreiod_end = null;
        Date nextegg_start = null;
        Date nextegg_end = null;

        for (int i = 0; i < tasks.size(); i++) {

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = df.parse(tasks.get(i).title);
                endDate = df.parse(tasks.get(i).content);
                String newDateString = df.format(startDate);
                System.out.println(newDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            cal.setTime(startDate);
            Date pinkDate = cal.getTime();
            ColorDrawable pink = new ColorDrawable(getResources().getColor(R.color.period_current));
            caldroidFragment.setBackgroundDrawableForDate(pink, pinkDate);
            caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);

            int diffInDays = 0;
            if (startDate != null) {
                diffInDays = (int) ((endDate.getTime() - startDate.getTime())
                        / (1000 * 60 * 60 * 24));
            }

            for (int j = 0; j < diffInDays; j++) {
                cal.add(Calendar.DATE, 1);
                pinkDate = cal.getTime();
                caldroidFragment.setBackgroundDrawableForDate(pink, pinkDate);
                caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);
            }

            // 9
            if (i == 0) {
                cal.setTime(startDate);
                cal.add(Calendar.DATE, 9);

                pinkDate = cal.getTime();
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.coloryellow));
                caldroidFragment.setBackgroundDrawableForDate(green, pinkDate);
                caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);

                for (int j = 1; j < 6; j++) {
                    cal.add(Calendar.DATE, 1);
                    pinkDate = cal.getTime();
                    caldroidFragment.setBackgroundDrawableForDate(green, pinkDate);
                    caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);
                }
            }

            // predict
            if (i == 0) {
                cal.setTime(startDate);
                cal.add(Calendar.DATE, predictDate);

                pinkDate = cal.getTime();
                nextpreiod_start = pinkDate;
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.colorgreen));
                caldroidFragment.setBackgroundDrawableForDate(green, pinkDate);
                caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);


                for (int j = 1; j < predictLeght; j++) {
                    cal.add(Calendar.DATE, 1);
                    pinkDate = cal.getTime();
                    caldroidFragment.setBackgroundDrawableForDate(green, pinkDate);
                    caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);
                    nextpreiod_end = pinkDate;
                }
            }

            // 9
            if (i == 0) {
                cal.setTime(startDate);
                cal.add(Calendar.DATE, predictDate);
                cal.add(Calendar.DATE, 9);

                pinkDate = cal.getTime();
                nextegg_start = pinkDate;
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.coloryellow));
                caldroidFragment.setBackgroundDrawableForDate(green, pinkDate);
                caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);

                for (int j = 1; j < 6; j++) {
                    cal.add(Calendar.DATE, 1);
                    pinkDate = cal.getTime();
                    caldroidFragment.setBackgroundDrawableForDate(green, pinkDate);
                    caldroidFragment.setTextColorForDate(R.color.colorAccent, pinkDate);
                    nextegg_end = pinkDate;
                }
            }

            //DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String txt = "ประจำเดือนครั้งต่อไป : " + df.format(nextpreiod_start) + " - " + df.format(nextpreiod_end);
            String txt1 = "ตกไข่ครั้งต่อไป : " + df.format(nextegg_start) + " - " + df.format(nextegg_end);

            txt_detail.setText(txt);
            txt_detail1.setText(txt1);
        }

        caldroidFragment.refreshView();

    }

    private void setCustomResourceForDates2() {
        Calendar cal = Calendar.getInstance();
        tasks = new ArrayList<Task>(Task.getAll());

        for (int i = 0; i < tasks.size(); i++) {

            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = df.parse(tasks.get(i).title);
                endDate = df.parse(tasks.get(i).content);
                String newDateString = df.format(startDate);
                System.out.println(newDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            cal.setTime(startDate);
            Date blueDate = cal.getTime();
            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.colorwhite));
            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);

            int diffInDays = 0;
            if (startDate != null) {
                diffInDays = (int) ((endDate.getTime() - startDate.getTime())
                        / (1000 * 60 * 60 * 24));
            }

            for (int j = 0; j < diffInDays; j++) {
                cal.add(Calendar.DATE, 1);
                blueDate = cal.getTime();
                caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
                caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);
            }

            // 9
            if (i == 0) {
                cal.setTime(startDate);
                cal.add(Calendar.DATE, 9);

                blueDate = cal.getTime();
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.colorwhite));
                caldroidFragment.setBackgroundDrawableForDate(green, blueDate);
                caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);

                for (int j = 1; j < 6; j++) {
                    cal.add(Calendar.DATE, 1);
                    blueDate = cal.getTime();
                    caldroidFragment.setBackgroundDrawableForDate(green, blueDate);
                    caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);
                }
            }

            // predict
            if (i == 0) {
                cal.setTime(startDate);
                cal.add(Calendar.DATE, predictDate);

                blueDate = cal.getTime();
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.colorwhite));
                caldroidFragment.setBackgroundDrawableForDate(green, blueDate);
                caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);


                for (int j = 1; j < predictLeght; j++) {
                    cal.add(Calendar.DATE, 1);
                    blueDate = cal.getTime();
                    caldroidFragment.setBackgroundDrawableForDate(green, blueDate);
                    caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);
                }
            }

            // 9
            if (i == 0) {
                cal.setTime(startDate);
                cal.add(Calendar.DATE, predictDate);
                cal.add(Calendar.DATE, 9);

                blueDate = cal.getTime();
                ColorDrawable green = new ColorDrawable(getResources().getColor(R.color.colorwhite));
                caldroidFragment.setBackgroundDrawableForDate(green, blueDate);
                caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);

                for (int j = 1; j < 6; j++) {
                    cal.add(Calendar.DATE, 1);
                    blueDate = cal.getTime();
                    caldroidFragment.setBackgroundDrawableForDate(green, blueDate);
                    caldroidFragment.setTextColorForDate(R.color.colorblack, blueDate);
                }
            }

        }

        caldroidFragment.refreshView();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "List Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://org.usablelabs.duedo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}

class TasksAdapter extends ArrayAdapter<Task> {

    String olddate;
    private ArrayList<Task> alltasks;

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
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
        tv.setText(alltasks.get(position).title);

        TextView tv2 = (TextView) convertView.findViewById(R.id.txt_content);
        tv2.setText(alltasks.get(position).content);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
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
        if(olddate!=null) {
            int diffInDays = (int) ((startDate.getTime() - endDate.getTime())
                    / (1000 * 60 * 60 * 24));
            DateDiff = diffInDays+"";
        }

        TextView tv3 = (TextView) convertView.findViewById(R.id.txt_cycle);
        tv3.setText(DateDiff);

        return convertView;
    }

}
