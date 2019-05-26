package com.example.slacker_app;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttHelper {

    public MqttAndroidClient mqttAndroidClient;

    final String serverUri = "192.168.1.200:1883";

    final String clientId = "ExampleAndroidClient";

    final String username = "xxxxxxx";
    final String password = "yyyyyyyyyy";

    private IMqttActionListener mqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            exception.printStackTrace();
        }
    };

    public MqttHelper(Context context){
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
    }

    public void setCallbackAndConnect(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
        connect();
    }

    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, mqttActionListener);
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    public void subscribeToTopic(String topic, int qos) {
        if(mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.subscribe(topic, qos, null, mqttActionListener);

            } catch (MqttException ex) {
                System.err.println("Exception whilst subscribing");
                ex.printStackTrace();
            }
        }
    }


    public void publishMessage(String topic, String message){
        if(mqttAndroidClient.isConnected()){
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            try {
                mqttAndroidClient.publish(topic, mqttMessage);
            }
            catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}