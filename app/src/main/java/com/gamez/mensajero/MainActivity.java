package com.gamez.mensajero;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Atributos
    private TabHost tbH;
    private EditText numero, mensaje;
    private TextView conversacion;
    private Button enviarBtn;
    private Mensaje varMensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obteniendo los elementos de la Vista
        numero = (EditText) findViewById(R.id.numero);
        mensaje = (EditText) findViewById(R.id.mensaje);
        conversacion = (TextView) findViewById(R.id.conversacion);

        tbH = (TabHost) findViewById(R.id.tbH);
        tbH.setup();
        TabHost.TabSpec tb1 = tbH.newTabSpec("tab1");
        TabHost.TabSpec tb2 = tbH.newTabSpec("tab2");
        TabHost.TabSpec tb3 = tbH.newTabSpec("tab3");

        // Definiendo el texto de los tabs en ejecucion
        tb1.setIndicator("Enviar");
        tb2.setIndicator("Contactos");
        tb3.setIndicator("Historial");
        // Asignando el contenido de los tabs
        tb1.setContent(R.id.tab1);
        tb2.setContent(R.id.tab2);
        tb3.setContent(R.id.tab3);
        // Se anexan los tabs definidos a la variable del TabHost
        tbH.addTab(tb1);
        tbH.addTab(tb2);
        tbH.addTab(tb3);

        // Definimos el elemento Boton enviar y declaramos que se espera la accion del mismo
        enviarBtn = (Button) findViewById(R.id.enviarBtn);
        enviarBtn.setOnClickListener(this);

        varMensajes = new Mensaje(this);

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

        BroadcastReceiver receiver = new BroadcastReceiver(){
            public void onReceive(Context context, Intent intent){
                processReceive(context, intent);
            }
        };
        registerReceiver(receiver, filter);

    }

    public void onClick(View v){
        if (v.getId() == enviarBtn.getId()){
            varMensajes.EnviarMensaje(mensaje.getText().toString(), numero.getText().toString());
            conversacion.setText(conversacion.getText().toString()+"\n\t\t\t\t\tYo:\n\t\t\t\t\t"+mensaje.getText().toString());
        }
    }

    public void processReceive(Context context, Intent intent){
        Bundle bundle = intent.getExtras();//con la variable de tipo bundle tipo extra obtenemos los mensajes
        Object[] objArr = (Object[])bundle.get("pdus");//pdus es el formato en que viajan los mensajes de texto
        String remitente="";
        String cuerpo="";
        String numeroComparar=numero.getText().toString();
        //se debe llevar el nro de telefono desde +58045555555 hasta el usado 041456555555
        numeroComparar="+58"+numeroComparar.substring(1);//extraigo de la cadena del numero desde la posicion 2
        for (int i = 0; i < objArr.length; i++) {//ciclo para cada mensaje de texto encontrado
            SmsMessage mensaje = SmsMessage.createFromPdu((byte[]) objArr[i]);//convierte lo recibido en un objeto tipo mensaje. la raya es porque ya la clase esta obsoleta
            remitente=mensaje.getOriginatingAddress();//obtengo el numero de telefono
            cuerpo=mensaje.getMessageBody();
            if (remitente.compareTo(numeroComparar)==0){//comparar cadenas en java. 0 me entrega si son iguales
                conversacion.setText(conversacion.getText().toString()+"\n"+remitente+":\n"+cuerpo);
            }
        }
    }


}
