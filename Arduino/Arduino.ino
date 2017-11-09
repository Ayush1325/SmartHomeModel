#include <DHT.h>
#include <Servo.h>
#define DHTPIN 2     // what pin we're connected to
#define DHTTYPE DHT22 
DHT dht(DHTPIN, DHTTYPE);

const int flood_pin = A3;
const int vib_pin = A4;
const int pir_pin = A5;
const int rain_sensor_pin = 4;
const int led1_pin = 3;
const int ldr_sensor_pin = A0;
const int fan1_pin = 5;
const int car_sensor_pin = 7;
const int flame_sensor_pin = A2;
const int gas_sensor_pin = A1;
const int buzzer = 10;
const int led2_pin = 6;
const int fan2_pin = 11;
const int mainDoor_pin = 8;
const int garageDoor_pin = 9;
float hum, temp;
int rain, ldr, car_tracker, flame, gas;
int pir = 0;
int flood = 0;
int quake = 0;
const String space = " ";
String message;
unsigned long previousMillis = 0;
unsigned long pirPreviousMillis = 0;
const long interval = 3000;
Servo mainDoor_servo, garageDoor_servo;

void setup() {
  Serial.begin(9600);
  pinMode(rain_sensor_pin, INPUT);
  pinMode(led1_pin, OUTPUT);
  pinMode(ldr_sensor_pin, INPUT);
  pinMode(fan1_pin, OUTPUT);
  pinMode(car_sensor_pin, INPUT);
  pinMode(flame_sensor_pin, INPUT);
  pinMode(gas_sensor_pin, INPUT);
  pinMode(pir_pin, INPUT);
  pinMode(flood_pin, INPUT);
  digitalWrite(led1_pin, LOW);
  digitalWrite(fan1_pin, LOW);
  mainDoor_servo.attach(mainDoor_pin);
  garageDoor_servo.attach(garageDoor_pin);
  dht.begin(); 
}

void loop() {
  unsigned long currentMillis = millis();
  if (currentMillis - previousMillis >= interval){
    previousMillis = currentMillis;
    sensors();
  }
  reciver();
  Sens();
}

void Sens(){
  if (digitalRead(pir_pin) == 1){
    pir = 1;
    digitalWrite(buzzer, HIGH);
  }if (digitalRead(flood_pin) == 1){
    flood = 1;
    digitalWrite(buzzer, HIGH);
  }if (digitalRead(vib_pin) == 1){
    quake = 1;
    digitalWrite(buzzer, HIGH);
  }
}

void reciver(){
  if(Serial.available()){
    message = Serial.readString();
    action();
  }
}

void sensors(){
  hum = dht.readHumidity();
  temp= dht.readTemperature();
  rain = digitalRead(rain_sensor_pin);
  ldr = analogRead(ldr_sensor_pin);
  car_tracker = digitalRead(car_sensor_pin);
  flame = analogRead(flame_sensor_pin);
  gas = analogRead(gas_sensor_pin);
  Serial.println(hum + space + temp + space + rain + space + ldr + space + car_tracker + space + flame + space + gas + space + pir + space + flood + space + quake);
}

void action(){
  if (message.startsWith("l1: ")){
    analogWrite(led1_pin, message.substring(4).toInt());
  }else if (message.startsWith("f1: ")){
    analogWrite(fan1_pin, message.substring(4).toInt());
  }else if (message.startsWith("l2: ")){
    analogWrite(led2_pin, message.substring(4).toInt());
  }else if (message.startsWith("f2: ")){
    analogWrite(fan2_pin, message.substring(4).toInt());
  }else if (message.startsWith("b: ")){
    digitalWrite(buzzer, message.substring(3).toInt());
  }else if (message.startsWith("pir: ")){
    pir = message.substring(5).toInt();
  }else if (message.startsWith("flood: ")){
    flood = message.substring(7).toInt();
  }else if (message.startsWith("quake: ")){
    quake = message.substring(7).toInt();
  }else if (message.startsWith("main_door: ")){
    if (message.substring(11).toInt() == 1){
      mainDoor_servo.write(90);
    }else {
      mainDoor_servo.write(0);
    }
  }else if (message.startsWith("garage_door: ")){
    if (message.substring(13).toInt() == 1){
      garageDoor_servo.write(90);
    }else {
      garageDoor_servo.write(0);
    }
  }
}
