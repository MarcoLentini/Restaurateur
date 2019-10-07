package com.example.restaurateur.Information;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.example.restaurateur.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

public class SetTimeTableActivity extends AppCompatActivity {
    TimePickerDialog picker;
    EditText eTextMmf,eTextMmt,eTextMaf,eTextMat;
    EditText eTextTmf,eTextTmt,eTextTaf,eTextTat;
    EditText eTextWmf,eTextWmt,eTextWaf,eTextWat;
    EditText eTextThmf,eTextThmt,eTextThaf,eTextThat;
    EditText eTextFmf,eTextFmt,eTextFaf,eTextFat;
    EditText eTextSamf,eTextSamt,eTextSaaf,eTextSaat;
    EditText eTextSumf,eTextSumt,eTextSuaf,eTextSuat;
    LinearLayout linLayMonday,linLayTuesday,linLayWednesday,linLayThursday,linLayFriday,linLaySaturday,linLaySunday;
    private static final String RestaurantDataFile = "RestaurantDataFile";
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String restaurantKey;
    private CheckBox closedMonday,closedTuesday,closedWednesday,closedThursday,closedFriday,closedSaturday,closedSunday;
    private CheckBox chooseMonday,chooseTuesday,chooseWednesday,chooseThursday,chooseFriday,chooseSaturday,chooseSunday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        String title;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String titile = getString(R.string.RestInfoTitle);
        getSupportActionBar().setTitle(titile);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
        }

//Get Firestore instance
        db = FirebaseFirestore.getInstance();




        eTextMmf=findViewById(R.id.edit_text_monday_morning_from);
        setClickListenerDialog(eTextMmf);
        eTextMmt=findViewById(R.id.edit_text_monday_morning_to);
        setClickListenerDialog(eTextMmt);
        eTextMaf=findViewById(R.id.edit_text_monday_afternoon_from);
        setClickListenerDialog(eTextMaf);
        eTextMat=findViewById(R.id.edit_text_monday_afternoon_to);
        setClickListenerDialog(eTextMat);

        eTextTmf=findViewById(R.id.edit_text_tuesday_morning_from);
        setClickListenerDialog(eTextTmf);
        eTextTmt=findViewById(R.id.edit_text_tuesday_morning_to);
        setClickListenerDialog(eTextTmt);
        eTextTaf=findViewById(R.id.edit_text_tuesday_afternoon_from);
        setClickListenerDialog(eTextTaf);
        eTextTat=findViewById(R.id.edit_text_tuesday_afternoon_to);
        setClickListenerDialog(eTextTat);

        eTextWmf=findViewById(R.id.edit_text_wednesday_morning_from);
        setClickListenerDialog(eTextWmf);
        eTextWmt=findViewById(R.id.edit_text_wednesday_morning_to);
        setClickListenerDialog(eTextWmt);
        eTextWaf=findViewById(R.id.edit_text_wednesday_afternoon_from);
        setClickListenerDialog(eTextWaf);
        eTextWat=findViewById(R.id.edit_text_wednesday_afternoon_to);
        setClickListenerDialog(eTextWat);

       eTextThmf=findViewById(R.id.edit_text_thursday_morning_from);
        setClickListenerDialog(eTextThmf);
        eTextThmt=findViewById(R.id.edit_text_thursday_morning_to);
        setClickListenerDialog(eTextThmt);
        eTextThaf=findViewById(R.id.edit_text_thursday_afternoon_from);
        setClickListenerDialog(eTextThaf);
        eTextThat=findViewById(R.id.edit_text_thursday_afternoon_to);
        setClickListenerDialog(eTextThat);

        eTextFmf=findViewById(R.id.edit_text_friday_morning_from);
        setClickListenerDialog(eTextFmf);
        eTextFmt=findViewById(R.id.edit_text_friday_morning_to);
        setClickListenerDialog(eTextFmt);
        eTextFaf=findViewById(R.id.edit_text_friday_afternoon_from);
        setClickListenerDialog(eTextFaf);
        eTextFat=findViewById(R.id.edit_text_friday_afternoon_to);
        setClickListenerDialog(eTextFat);

        eTextSamf=findViewById(R.id.edit_text_saturday_morning_from);
        setClickListenerDialog(eTextSamf);
        eTextSamt=findViewById(R.id.edit_text_saturday_morning_to);
        setClickListenerDialog(eTextSamt);
        eTextSaaf=findViewById(R.id.edit_text_saturday_afternoon_from);
        setClickListenerDialog(eTextSaaf);
        eTextSaat=findViewById(R.id.edit_text_saturday_afternoon_to);
        setClickListenerDialog(eTextSaat);


        eTextSumf=findViewById(R.id.edit_text_sunday_morning_from);
        setClickListenerDialog(eTextSumf);
        eTextSumt=findViewById(R.id.edit_text_sunday_morning_to);
        setClickListenerDialog(eTextSumt);
        eTextSuaf=findViewById(R.id.edit_text_sunday_afternoon_from);
        setClickListenerDialog(eTextSuaf);
        eTextSuat=findViewById(R.id.edit_text_sunday_afternoon_to);
        setClickListenerDialog(eTextSuat);

        chooseMonday=findViewById(R.id.sameTimetableMonday);
        setCheckChoose(chooseMonday,eTextMmf,eTextMmt,eTextMaf,eTextMat);
        chooseTuesday=findViewById(R.id.sameTimetableTuesday);
        setCheckChoose(chooseTuesday,eTextTmf,eTextTmt,eTextTaf,eTextTat);
        chooseWednesday=findViewById(R.id.sameTimetableWednesday);
        setCheckChoose(chooseWednesday,eTextWmf,eTextWmt,eTextWaf,eTextWat);
        chooseThursday=findViewById(R.id.sameTimetableThursday);
        setCheckChoose(chooseThursday,eTextThmf,eTextThmt,eTextThaf,eTextThat);
        chooseFriday=findViewById(R.id.sameTimetableFriday);
        setCheckChoose(chooseFriday,eTextFmf,eTextFmt,eTextFaf,eTextFat);
        chooseSaturday=findViewById(R.id.sameTimetableSaturday);
        setCheckChoose(chooseSaturday,eTextSamf,eTextSamt,eTextSaaf,eTextSaat);
        chooseSunday=findViewById(R.id.sameTimetableSunday);
        setCheckChoose(chooseSunday,eTextSumf,eTextSumt,eTextSuaf,eTextSuat);

        closedMonday= findViewById(R.id.openMonday);
        linLayMonday=findViewById(R.id.linearLayoutMonday);
        setCheckListener(closedMonday,eTextMmf,eTextMmt,eTextMaf,eTextMat,linLayMonday,chooseMonday);
        closedTuesday= findViewById(R.id.openTuesday);
        linLayTuesday=findViewById(R.id.linearLayoutTuesday);
        setCheckListener(closedTuesday,eTextTmf,eTextTmt,eTextTaf,eTextTat, linLayTuesday,chooseTuesday);
        closedWednesday= findViewById(R.id.openWednesday);
        linLayWednesday=findViewById(R.id.linearLayoutWednesday);
        setCheckListener(closedWednesday,eTextMmf,eTextMmt,eTextMaf,eTextMat,linLayWednesday,chooseWednesday);
        closedThursday= findViewById(R.id.openThursday);
        linLayThursday=findViewById(R.id.linearLayoutThursday);
        setCheckListener(closedThursday,eTextMmf,eTextMmt,eTextMaf,eTextMat,linLayThursday,chooseThursday);
        closedFriday= findViewById(R.id.openFriday);
        linLayFriday=findViewById(R.id.linearLayoutFriday);
        setCheckListener(closedFriday,eTextMmf,eTextMmt,eTextMaf,eTextMat,linLayFriday,chooseFriday);
        linLaySaturday=findViewById(R.id.linearLayoutSaturday);
        closedSaturday= findViewById(R.id.openSaturday);
        setCheckListener(closedSaturday,eTextMmf,eTextMmt,eTextMaf,eTextMat,linLaySaturday,chooseSaturday);
        linLaySunday=findViewById(R.id.linearLayoutSunday);
        closedSunday= findViewById(R.id.openSunday);
        setCheckListener(closedSunday,eTextMmf,eTextMmt,eTextMaf,eTextMat,linLaySunday,chooseSunday);








    }

    private void setCheckChoose(CheckBox choose, EditText morningFrom, EditText morningTo, EditText afterFrom, EditText afterTo) {


        choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    eTextMmf.setText(morningFrom.getText().toString());
                    eTextMmt.setText(morningTo.getText().toString());
                    eTextMaf.setText(afterFrom.getText().toString());
                    eTextMat.setText(afterTo.getText().toString());
                    eTextTmf.setText(morningFrom.getText().toString());
                    eTextTmt.setText(morningTo.getText().toString());
                    eTextTaf.setText(afterFrom.getText().toString());
                    eTextTat.setText(afterTo.getText().toString());
                    eTextWmf.setText(morningFrom.getText().toString());
                    eTextWmt.setText(morningTo.getText().toString());
                    eTextWaf.setText(afterFrom.getText().toString());
                    eTextWat.setText(afterTo.getText().toString());
                    eTextThmf.setText(morningFrom.getText().toString());
                    eTextThmt.setText(morningTo.getText().toString());
                    eTextThaf.setText(afterFrom.getText().toString());
                    eTextThat.setText(afterTo.getText().toString());
                    eTextFmf.setText(morningFrom.getText().toString());
                    eTextFmt.setText(morningTo.getText().toString());
                    eTextFaf.setText(afterFrom.getText().toString());
                    eTextFat.setText(afterTo.getText().toString());
                    eTextSamf.setText(morningFrom.getText().toString());
                    eTextSamt.setText(morningTo.getText().toString());
                    eTextSaaf.setText(afterFrom.getText().toString());
                    eTextSaat.setText(afterTo.getText().toString());
                    eTextSumf.setText(morningFrom.getText().toString());
                    eTextSumt.setText(morningTo.getText().toString());
                    eTextSuaf.setText(afterFrom.getText().toString());
                    eTextSuat.setText(afterTo.getText().toString());
                }

            }
        });

    }


    public void setCheckListener(CheckBox ckBox, EditText morningFrom, EditText morningTo, EditText afterFrom, EditText afterTo, LinearLayout linLay, CheckBox choose) {


        ckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    morningFrom.setEnabled(false);
                    morningTo.setEnabled(false);
                    afterFrom.setEnabled(false);
                    afterTo.setEnabled(false);
                    linLay.setBackgroundColor(getColor(R.color.colorLightGray));
                    choose.setEnabled(false);

                }else{
                    morningFrom.setEnabled(true);
                    morningTo.setEnabled(true);
                    afterFrom.setEnabled(true);
                    afterTo.setEnabled(true);
                    linLay.setBackgroundColor(getColor(R.color.white));
                    choose.setEnabled(true);
                }

            }
        });

    }
    private void setClickListenerDialog(EditText eText)
    {
        eText.setInputType(InputType.TYPE_NULL);

        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour,minutes;
                String time= eText.getText().toString();
                if(!time.equals("")){
                    String[] time_part= time.split(":");
                    hour =Integer.parseInt(time_part[0]);
                    minutes =Integer.parseInt(time_part[1]);  }
                else{
                    final Calendar cldr = Calendar.getInstance();
                    hour = cldr.get(Calendar.HOUR_OF_DAY);
                    minutes = cldr.get(Calendar.MINUTE);

                }
                // time picker dialog
                    picker = new TimePickerDialog(SetTimeTableActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    eText.setText(String.format("%02d", sHour) + ":" + String.format("%02d",sMinute));

                                }
                            }, hour, minutes, true);
                    picker.show();
                }

        });
        eText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int hour,minutes;
                    String time= eText.getText().toString();
                    if(!time.equals("")){
                    String[] time_part= time.split(":");
                    hour =Integer.parseInt(time_part[0]);
                     minutes =Integer.parseInt(time_part[1]);  }
                    else{
                        final Calendar cldr = Calendar.getInstance();
                         hour = cldr.get(Calendar.HOUR_OF_DAY);
                         minutes = cldr.get(Calendar.MINUTE);

                    }
                    // time picker dialog
                    picker = new TimePickerDialog(SetTimeTableActivity.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                    eText.setText(String.format("%02d", sHour) + ":" + String.format("%02d",sMinute));

                                }
                            }, hour, minutes, true);
                    picker.show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ok_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.ok_action) {

            HashMap<String,Object> days= new HashMap<>();
            days.put("monday",setDayTime(closedMonday,eTextMmf,eTextMmt,eTextMaf,eTextMat));
            days.put("tuesday",setDayTime(closedTuesday,eTextTmf,eTextTmt,eTextTaf,eTextTat));
            days.put("wednesday",setDayTime(closedWednesday,eTextWmf,eTextWmt,eTextWaf,eTextWat));
            days.put("thursday",setDayTime(closedThursday,eTextThmf,eTextThmt,eTextThaf,eTextThat));
            days.put("friday",setDayTime(closedFriday,eTextFmf,eTextFmt,eTextFaf,eTextFat));
            days.put("saturday",setDayTime(closedSaturday,eTextSamf,eTextSamt,eTextSaaf,eTextSaat));
            days.put("sunday",setDayTime(closedSunday,eTextSumf,eTextSumt,eTextSuaf,eTextSuat));


            Bundle args = new Bundle();
            args.putSerializable("timetable", days);
            Intent time = new Intent(this, RegisterRest.class);
            time.putExtras(args);
            setResult(2,time);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private Object setDayTime(CheckBox closed, EditText morningFrom, EditText morningTo, EditText afterFrom, EditText afterTo) {
        HashMap<String,Object> day=new HashMap<>();
        if(closed.isChecked()){
            day.put("closed","true");
        }
        else{
            day.put("closed","false");
            day.put("morning_from",morningFrom.getText().toString());
            day.put("morning_to",morningTo.getText().toString());
            day.put("after_from",afterFrom.getText().toString());
            day.put("after_to",afterTo.getText().toString());
        }
        return day;
    }


}
