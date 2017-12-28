#!/usr/bin/env python3
import sys
from threading import Timer
from subprocess import call
from pubnub.pnconfiguration import PNConfiguration
from pubnub.pubnub import PubNub
from pubnub.callbacks import SubscribeCallback
from time import sleep
import Adafruit_DHT as dht
import serial
import time

channel = "test"
car_track = False
pir = False
fire = False
flood = False
quake = False

pnconfig = PNConfiguration()
pnconfig.subscribe_key = "your sub key"
pnconfig.publish_key = "your publish key"

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
    mRain = not bool(int(reading_list[2]))
    if int(reading_list[3]) > 400:
        mLdr = False
    else:
        mLdr = True
    mCar_sens = reading_list[4]
    mFlame = reading_list[5]
    mGas = reading_list[6]
    mPir = int(reading_list[7])
    mFlood = int(reading_list[8])
    mQuake = int(reading_list[9])
    return mHumidity, mTemp, mRain, mLdr, mCar_sens, mFlame, mGas, mPir, mFlood, mQuake


def update_car_tracker():
    global car_track
    car_track = False


def Fire(mFlame, mGas):
    global fire
    if not fire:
        if int(mFlame)<700 or int(mGas)>250:
            fire = True


def PirSens(mPir):
    global pir
    if not pir:
        if mPir == 1:
            pir = True


def Flood(mFlood):
    global flood
    if not flood:
        if mFlood == 1:
            flood = True


def Car(mCar_sens):
    global car_track
    if not car_track:
        if mCar_sens == "1":
            car_track = True
            ser.write(str("garage_door: " + 1).encode())
            car_reset = Timer(10.0, update_car_tracker)
            t = Timer(8.0, lambda: print("garage_door"))
            car_close = Timer(8.0, lambda: ser.write(str("garage_door: " + 0).encode()))
            t.start()
            car_close.start()
            car_reset.start()


def Quake(mQuake):
    global quake
    if not quake:
        if mQuake == 1:
            quake = True


class Listener(SubscribeCallback):
    def message(self, pubnub, message):
        if "phone" in message.message:
            if "light1" in message.message["phone"]:
                ser.write(str("l1: " + message.message["phone"]["light1"]).encode())
                print(message.message["phone"]["light1"])
            elif "light2" in message.message["phone"]:
                ser.write(str("l2: " + message.message["phone"]["light2"]).encode())
                print(message.message["phone"]["light2"])
            elif "fan1" in message.message["phone"]:
                ser.write(str("f1: " + message.message["phone"]["fan1"]).encode())
                print(message.message["phone"]["fan1"])
            elif "fan2" in message.message["phone"]:
                ser.write(str("f2: " + message.message["phone"]["fan2"]).encode())
                print(message.message["phone"]["fan2"])
            elif "main_door" in message.message["phone"]:
                print(message.message["phone"]["main_door"])
                ser.write(str("main_door: " + message.message["phone"]["main_door"]).encode())
            elif "garage_door" in message.message["phone"]:
                print(message.message["phone"]["garage_door"])
                ser.write(str("garage_door: " + message.message["phone"]["garage_door"]).encode())
            elif "burglary" in message.message["phone"]:
                global pir
                pir = False
                ser.write(str("pir: " + str(0)).encode())
                time.sleep(2)
                ser.write(str("b: " + str(0)).encode())
            elif "fire" in  message.message["phone"]:
                global fire
                fire = False
                ser.write(str("b: " + str(0)).encode())
            elif "flood" in message.message["phone"]:
                global flood
                flood = False
                ser.write(str("flood: " + str(0)).encode())
                time.sleep(2)
                ser.write(str("b: " + str(0)).encode())
            elif "system" in message.message["phone"]:
                string = "sudo " + message.message["phone"]["system"]
                call(string, shell=True)
            elif "earthquake" in message.message["phone"]:
                global quake
                quake = False
                print(quake)
                ser.write(str("quake: " + str(0)).encode())
                time.sleep(2)
                ser.write(str("b: " + str(0)).encode())


def publish_message(pHumidity, pTemp, pRain, pLdr, pFireRead, pPir, pFlood, pQuake):
    try:
        pubnub.publish().channel(channel).message({"pi": {"temp": pTemp, "humidity": pHumidity, "ldr": pLdr, "rain": pRain, "fire": pFireRead, "burglary": pPir, "flood": pFlood, "quake": pQuake}}).sync()
        print("done")
    except:
        print("error")
        raise


pubnub.add_listener(Listener())
pubnub.subscribe().channels(channel).execute()


while True:
    try:
        arduino_readings = arduino_get()
        print(arduino_readings)
        humidity, temp, rain, ldr, car_sens, flame, gas, pir_sens, flood_sens, vib = arduino_reading_format(arduino_readings)
        Fire(flame, gas)
        Car(car_sens)
        PirSens(pir_sens)
        Flood(flood_sens)
        Quake(vib)
        publish_message(humidity, temp, rain, ldr, fire, pir, flood, quake)
    except:
        print("error")
        continue
