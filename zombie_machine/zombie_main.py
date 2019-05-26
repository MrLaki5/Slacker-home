import paho.mqtt.client as mqtt
import logging
import json
import mac_getter
import youtube
import mpv_play_video

with open("config.json") as f:
    config_file = json.load(f)

print(config_file)
logging.basicConfig(level=logging.INFO)
mqtt_port = config_file['MQTT_PORT']
mqtt_ip = config_file['MQTT_IP']
client_id = mac_getter.get_mac().lower()

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    logging.info("Connected with result code " + str(rc))
    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.publish("devices/status/activate", client_id)
    client.subscribe("devices/" + client_id + "/command")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    message = msg.payload.decode("UTF-8")
    logging.info("Message on channel: " + msg.topic)
    if msg.topic == "devices/" + client_id + "/command":
        message_json = json.loads(message)
        if message_json["command"] == "youtube_latest":
            if "channel" in message_json and message_json["channel"] != "":
                video_url = youtube.get_latest_video(message_json["channel"])
                if "player" in message_json:
                    if message_json["player"] == "chrome":
                        youtube.open_in_browser(video_url)
                    else:
                        mpv_play_video.play_in_mpv(video_url, True, False)


client = mqtt.Client(client_id=client_id, clean_session=True)
client.on_connect = on_connect
client.on_message = on_message
client.will_set("devices/status/deactivate", client_id)
client.connect(mqtt_ip, mqtt_port, 60)
# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_forever()