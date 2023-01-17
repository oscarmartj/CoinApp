package es.upm.etsiinf.dam.coinapp.services.updates.job;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import es.upm.etsiinf.dam.coinapp.services.notificaciones.NotificationService;

public class UpdateScheduleJob {
    private static final int UPDATE_JOB_ID=998;

    public void scheduleJob(Context context){
        ComponentName serviceComponent = new ComponentName(context, UpdateService.class);
        JobInfo.Builder builder = new JobInfo.Builder(UPDATE_JOB_ID, serviceComponent);
        long periodicTime = TimeUnit.MINUTES.toMillis(15);
        builder.setPeriodic(periodicTime);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);


        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }
}
