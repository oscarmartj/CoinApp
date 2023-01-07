package es.upm.etsiinf.dam.coinapp.services.notificaciones;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class NotificationScheduleJob {
    private static final int NOTIFICATION_JOB_ID=1;

    public void scheduleJob(Context context){
        Log.i("scheduleJob","entra aqui");
        ComponentName serviceComponent = new ComponentName(context,NotificationService.class);
        JobInfo.Builder builder = new JobInfo.Builder(NOTIFICATION_JOB_ID, serviceComponent);
        long periodicTime = TimeUnit.MINUTES.toMillis(1);
        builder.setPeriodic(periodicTime);
        builder.setOverrideDeadline(0);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);

        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
}
