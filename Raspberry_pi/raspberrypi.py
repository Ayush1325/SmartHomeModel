import sys
from subprocess import call
from pubnub.pnconfiguration import PNConfiguration
from pubnub.pubnub import PubNub
from pubnub.callbacks import SubscribeCallback
from time import sleep
import Adafruit_DHT as dht
import serial
import time

channel = "test"

pnconfig = PNConfiguration()
pnconfig.subscribe_key = "sub-c-8091636e-a1d2-11e7-a52c-d6a48d4f2d9f"
pnconfig.publish_key = "pub-c-7087e6f9-65a8-4608-93c9-7e1264fd5743"

pubnub = PubNub(pnconfig)

try:
  ser=serial.Serial("/dev/ttyACM0", 9600, timeout=4.0)
except:
  ser=serial.Serial("/dev/ttyACM1", 9600, timeout=4.0)


def arduino_get():
  read_ser = ser.readline()
  read_ser = str(read_ser)
  read_ser = read_ser[2:-5]
  return read_ser.split(" ")


def arduino_reading_format(reading_list):
  mHumidity = str(round(float(reading_list[0]), 1))
  mTemp = str(round(float(reading_list[1]), 1))
  mRain = bool(int(reading_list[2]))
  if int(reading_list[3]) > 900:
    mLdr = True
  else:
    mLdr = False
  return mHumidity, mTemp, mRain, mLdr


class Listener(SubscribeCallback):
  def message(self, pubnub, message):
    if "phone" in message.message:
      if "light" in message.message["phone"]:
        ser.write(str("l: " + message.message["phone"]["light"]).encode())
      elif "fan" in message.message["phone"]:
        ser.write(str("f: " + message.message["phone"]["fan"]).encode())
      elif "door" in message.message["phone"]:
        print(message.message["phone"]["door"])
      elif "system" in message.message["phone"]:
        string = "sudo " + message.message["phone"]["system"]
        print(string)
        call(string, shell=True)


def publish_message(pHumidity, pTemp, pRain, pLdr):
  try:
    pubnub.publish().channel(channel).message({"pi": {"temp": pTemp, "humidity": pHumidity, "ldr": pLdr, "rain": pRain}}).sync()
  except:
    raise


pubnub.add_listener(Listener())
pubnub.subscribe().channels(channel).execute()


while True:
    try:
      arduino_readings = arduino_get()
      print(arduino_readings)
      humidity, temp, rain, ldr = arduino_reading_format(arduino_readings)
      publish_message(humidity, temp, rain, ldr)
    except:
      continue

