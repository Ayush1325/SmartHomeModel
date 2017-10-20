#include <DHT.h>;
#define DHTPIN 2     // what pin we're connected to
#define DHTTYPE DHT22 
DHT dht(DHTPIN, DHTTYPE);

int rain_sensor_pin = 4;
int led_pin = 3;
int ldr_pin = A0;
float hum, temp;
int rain, ldr;
String space = " ";
String message;

void setup() {
  Serial.begin(9600);
  pinMode(rain_sensor_pin, INPUT);
  pinMode(led_pin, OUTPUT);
  digitalWrite(led_pin, LOW);
  dht.begin(); 
}

void loop() {
  sensors();
}

void reciver(){
  if(Serial.available()){
    message = Serial.readString();
    action();
  }
}

void sensors(){
  for(int x = 0; x < 60; x++){
    reciver();
    delay(50); 
  }
  hum = dht.readHumidity();
  temp= dht.readTemperature();
  rain = digitalRead(rain_sensor_pin);
  ldr = analogRead(ldr_pin);
  Serial.println(hum + space + temp + space + rain + space + ldr);
}

void action(){
  if (message.startsWith("l: ")){
    analogWrite(led_pin, message.substring(3).toInt());
  }
}



