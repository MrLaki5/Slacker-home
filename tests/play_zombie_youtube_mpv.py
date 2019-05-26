import paho.mqtt.client as mqtt
import json

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+ str(rc))
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.

    message = {}
    message["command"] = "youtube_latest"
    message["channel"] = "EmisijeRTVojvodine"
    message["player"] = "mpv"
    client.publish("devices/" + "50:E5:49:1A:EE:E3".lower() + "/command", json.dumps(message))
    client.disconnect()


client = mqtt.Client(client_id="11e6421e-7c5b-49d6-8319-a9c19dbffad6", clean_session=True)
client.on_connect = on_connect

client.connect("192.168.1.200", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()
