package com.gamez.mensajero;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

/**
 * Created by Gamez on 05/05/2017.
 */

public class Mensaje {
    private SmsManager manager;
    private PendingIntent intentoEnviar;
    private Context contexto;

    public Mensaje(Context contexto){
        this.contexto = contexto;
        manager = SmsManager.getDefault();
    }

    public void EnviarMensaje(String mensaje, String numero){
        PendingIntent sendIntent = PendingIntent.getBroadcast(contexto,0,new Intent("SMS_SENT"),0);
        manager.sendTextMessage(numero, null, mensaje, sendIntent, null);
    }
}
