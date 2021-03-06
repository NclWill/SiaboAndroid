package itsd1.indogrosir.com.siabo.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import itsd1.indogrosir.com.siabo.R;
import itsd1.indogrosir.com.siabo.models.Plan;
import itsd1.indogrosir.com.siabo.rest.ApiClient;
import itsd1.indogrosir.com.siabo.rest.RestApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paulina on 1/23/2017.
 */
public class KalenderActivity extends AppCompatActivity implements CalendarPickerController
{
    public int id_user = 0, id_plan=0;
    private Bundle extras;
    private String token = "";
    AgendaCalendarView calendarView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        extras = new Bundle();
        extras = getIntent().getExtras();
        token = extras.getString("token");
        id_user = extras.getInt("id_user");

        setContentView(R.layout.activity_kalender);

        List<CalendarEvent> eventList = new ArrayList<>();
        getKalender(eventList);


        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        calendarView = (AgendaCalendarView) findViewById(R.id.agenda_calendar_view);
        calendarView.init(eventList, minDate, maxDate, Locale.getDefault(), KalenderActivity.this);
    }

    private void mockList(List<CalendarEvent> eventList, Response<Plan> response)
    {
        for (int i = 0; i < response.body().getPlan().size(); i++)
        {
            if(i%2==0)
            {
                Calendar startDate = Calendar.getInstance();
                Calendar endDate = Calendar.getInstance();

                Date mulai = response.body().getPlan().get(i).getTgl_plan_mulai();
                Date selesai = response.body().getPlan().get(i).getTgl_plan_selesai();

                /*//Alarm
                alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(KalenderActivity.this.getApplicationContext(), AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(KalenderActivity.this.getApplicationContext(),0, intent, 0);
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                String jammulai = (response.body().getPlan().get(i).getJam_mulai()).substring(0,2);
                String jamselesai = (response.body().getPlan().get(i).getJam_selesai().substring(0,2));
                startTime.setTimeInMillis(System.currentTimeMillis());
                startTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(jammulai));
                endTime.setTimeInMillis(System.currentTimeMillis());
                endTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt(jamselesai));
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), 1000 * 60 * 20, alarmIntent);
                Toast.makeText(getApplicationContext(),jammulai + " " + jamselesai, Toast.LENGTH_LONG).show();*/

                String judul = response.body().getPlan().get(i).getStore_name();
                String jam = response.body().getPlan().get(i).getJam_mulai() + " - " + response.body().getPlan().get(i).getJam_selesai();
                String isi = "Kode Toko : " + response.body().getPlan().get(i).getStore_code() + " Jam mulai : " + response.body().getPlan().get(i).getJam_mulai() + " Jam selesai : " + response.body().getPlan().get(i).getJam_selesai() ;

                startDate.setTime(mulai);
                endDate.setTime(selesai);

                if(startDate!=endDate)
                {
                    endDate.add(Calendar.DATE, 1);
                   // endTime.add(Calendar.HOUR,1);
                }
                BaseCalendarEvent event = new BaseCalendarEvent(jam, isi, judul, ContextCompat.getColor(KalenderActivity.this.getApplicationContext(), R.color.yellow), startDate, endDate, false);
                event.setId(response.body().getPlan().get(i).getId());
                eventList.add(event);
            }
            else
            {
                Calendar startTime1 = Calendar.getInstance();
                Calendar endTime1 = Calendar.getInstance();
                Date mulai1 = response.body().getPlan().get(i).getTgl_plan_mulai();
                Date selesai1 = response.body().getPlan().get(i).getTgl_plan_selesai();
                startTime1.setTime(mulai1);
                endTime1.setTime(selesai1);
                String judul = response.body().getPlan().get(i).getStore_name();
                String jam = response.body().getPlan().get(i).getJam_mulai() + " - " + response.body().getPlan().get(i).getJam_selesai();
                String isi = "Kode Toko : " + response.body().getPlan().get(i).getStore_code() + " Jam mulai : " + response.body().getPlan().get(i).getJam_mulai() + " Jam selesai : " + response.body().getPlan().get(i).getJam_selesai() ;
                if(startTime1!=endTime1)
                {
                    endTime1.add(Calendar.DATE, 1);
                }
                BaseCalendarEvent event2 = new BaseCalendarEvent(jam, isi, judul, ContextCompat.getColor(KalenderActivity.this.getApplicationContext(), R.color.blue_dark), startTime1, endTime1, false);
                eventList.add(event2);
                event2.setId(response.body().getPlan().get(i).getId());
            }
        }
    }

    void getKalender(final List<CalendarEvent> eventList)
    {
        RestApi apiService = ApiClient.getClient().create(RestApi.class);
        Call<Plan> call = apiService.getKalender(id_user, token);

        call.enqueue(new Callback<Plan>()
        {
            @Override
            public void onResponse(Call<Plan> call, Response<Plan> response)
            {
                try
                {
                    mockList(eventList, response);

                    Calendar minDate = Calendar.getInstance();
                    Calendar maxDate = Calendar.getInstance();

                    minDate.add(Calendar.MONTH, -2);
                    minDate.set(Calendar.DAY_OF_MONTH, 1);
                    maxDate.add(Calendar.YEAR, 1);

                    calendarView.init(eventList, minDate, maxDate, Locale.getDefault(), KalenderActivity.this);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Plan> call, Throwable t)
            {
                Log.d("Log", t.toString());
            }
        });
    }

    @Override
    public void onDaySelected(DayItem dayItem) {    }

    @Override
    public void onEventSelected(CalendarEvent event)
    {
        if(event.getId() != 0)
        {
            Bundle b = new Bundle();
            b.putString("token", token);
            b.putInt("id_plan", (int) event.getId());
            b.putInt("id_user", id_user);
            Intent i = new Intent(getApplicationContext(), KalenderDetails.class);
            i.putExtras(b);
            startActivity(i);
        }
    }

    @Override
    public void onScrollToDate(Calendar calendar) {    }
}
