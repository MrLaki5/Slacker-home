package com.example.slacker_app;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    MqttHelper mqttHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button wakeUpButt = findViewById(R.id.wakeUpButton);
        wakeUpButt.setOnClickListener(wakeButtListener);
        Button lastestYTButt = findViewById(R.id.playLatestYT);
        lastestYTButt.setOnClickListener(playLatestYTListener);
    }

    @Override
    protected void onStart() {
        startMqtt();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mqttHelper != null){
            mqttHelper.stop();
        }
        super.onStop();
    }

    private View.OnClickListener wakeButtListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean result = mqttHelper.publishMessage("devices/wake_on_lan", "hello");
            if (result){
                Toast.makeText(MainActivity.this, "Wake sent", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener playLatestYTListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String channelName = ((EditText)findViewById(R.id.YTChannelName)).getText().toString();
            String message = "{\"command\": \"youtube_latest\", \"channel\": \"" + channelName + "\"}";
            boolean result = mqttHelper.publishMessage("devices/50:e5:49:1a:ee:e3/command", message);
            if (result){
                Toast.makeText(MainActivity.this, "Playing latest video of channel " + channelName, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallbackAndConnect(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                ((TextView) findViewById(R.id.connectionStatus)).setText("Connection: established");
            }

            @Override
            public void connectionLost(Throwable throwable) {
                ((TextView) findViewById(R.id.connectionStatus)).setText("Connection: not established");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}
