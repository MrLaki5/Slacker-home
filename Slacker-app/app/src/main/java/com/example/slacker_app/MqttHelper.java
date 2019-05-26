package com.example.slacker_app;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHelper {

    public MqttAsyncClient mqttAndroidClient;

    final String serverUri = "tcp://192.168.1.200:1883";

    final String clientId = "55e6421e-7c5b-49d6-8319-a9c19dbffad6";

    final String username = "xxxxxxx";
    final String password = "yyyyyyyyyy";

    private MqttClientPersistence presistance = null;

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
        try {
            presistance = new MemoryPersistence();
            mqttAndroidClient = new MqttAsyncClient(serverUri, clientId, presistance);
        } catch (MqttException e) {
            e.printStackTrace();
        }
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
        mqttConnectOptions.setKeepAliveInterval(1);
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, mqttActionListener);
        } catch (MqttException ex){
            ex.printStackTrace();
        }
    }


    public boolean subscribeToTopic(String topic, int qos) {
        if(mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.subscribe(topic, qos, null, mqttActionListener);
                return true;
            } catch (MqttException ex) {
                System.err.println("Exception whilst subscribing");
                ex.printStackTrace();
            }
        }
        return false;
    }


    public boolean publishMessage(String topic, String message){
        if(mqttAndroidClient.isConnected()){
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            try {
                mqttAndroidClient.publish(topic, mqttMessage);
                return true;
            }
            catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void stop(){
        if (mqttAndroidClient != null){
            try {
                mqttAndroidClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}