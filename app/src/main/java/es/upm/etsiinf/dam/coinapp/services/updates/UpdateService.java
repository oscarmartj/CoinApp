package es.upm.etsiinf.dam.coinapp.services.updates;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class UpdateService extends JobService {
    @Override
    public boolean onStartJob (JobParameters jobParameters) {
        return false;
    }

    @Override
    public boolean onStopJob (JobParameters jobParameters) {
        return false;
    }
}
