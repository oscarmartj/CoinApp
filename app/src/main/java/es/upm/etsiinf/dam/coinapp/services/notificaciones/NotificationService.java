package es.upm.etsiinf.dam.coinapp.services.notificaciones;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class NotificationService extends JobService {
    @Override
    public boolean onStartJob (JobParameters jobParameters) {
        // Aquí debes poner el código que envía la notificación

        // Indica al sistema que el trabajo ya se ha realizado y puede ser eliminado

        return false;
    }

    @Override
    public boolean onStopJob (JobParameters jobParameters) {
        // Este método se llamará si el sistema decide cancelar el trabajo antes de que se complete.
        // Deberías devolver "true" si quieres que el trabajo se vuelva a intentar más tarde, o "false" si no es necesario.
        return false;
    }
}
