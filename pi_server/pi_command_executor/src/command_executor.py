import paho.mqtt.client as mqtt
import os
import magic_package
import logging

logging.basicConfig(level=logging.INFO)
mqtt_port = int(os.environ['MQTT_PORT'])
wake_mac = os.environ['WAKE_MAC']


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    logging.info("Connected with result code " + str(rc))
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("devices/wake_on_lan")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    message = msg.payload.decode("UTF-8")
    logging.info("message")
    if msg.topic == "devices/wake_on_lan":
        magic_package.send_package(wake_mac)
        print(message)


client = mqtt.Client(client_id="20e6421e-7c5b-49d6-8319-a9c19dbffad6", clean_session=True)
client.on_connect = on_connect
client.on_message = on_message
client.connect("localhost", mqtt_port, 60)
logging.info("connecting1")
# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()