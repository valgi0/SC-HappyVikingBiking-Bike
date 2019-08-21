#include <Arduino.h>
#include "AcceleroMMA7361.h"

AcceleroMMA7361 accelero;
int x;
int y;
int z;

void setup()
{
  Serial.begin(9600);
  accelero.begin();
  accelero.setARefVoltage(5);                   //sets the AREF voltage to 3.3V
  accelero.setSensitivity(HIGH);                   //sets the sensitivity to +/-6G
  accelero.calibrate();
}

void loop()
{
  x = accelero.getXAccel();
  y = accelero.getYAccel();
  z = accelero.getZAccel();
  Serial.print("\nx: ");
  Serial.print(x);
  Serial.print(" \ty: ");
  Serial.print(y);
  Serial.print(" \tz: ");
  Serial.print(z);
  Serial.print("\tG*10^-2");
  delay(500);                                     //make it readable
}

/*
int x = A2;
int y = A3;
int z = A4;

int xarray[10];
int yarray[10];
int zarray[10];

int sumx;
int sumy;
int sumz;

int i = 0;

void setup() {
  Serial.begin(9600);
}

void loop() {

   xarray[i] = analogRead(x);
   xarray[y] = analogRead(y);
   zarray[i] = analogRead(z);
   delay(100);
   i++;
   if(i == 10){
     sumx = 0;
     sumy = 0;
     sumz = 0;
     for(; i > 0; i--){
       sumx += xarray[i];
       sumy += yarray[i];
       sumz += zarray[i];
     }

      Serial.print("X: ");
      Serial.print(sumx/10);
      Serial.print(" Y: ");
      Serial.print(sumy/10);
      Serial.print(" Z: ");
      Serial.println(sumz/10);



   }
}



int button = 2;
int path = 0;

void func(){
  while(Serial.available() == 0){
    delay(100);
  }

  Serial.println(Serial.readString());
  Serial.flush();
}

void IRS(){
    noInterrupts();
    path = 1;
    interrupts();
  }

void setup() {
  // put your setup code here, to run once:
  pinMode(2, INPUT);
  Serial.begin(9600);
  attachInterrupt(digitalPinToInterrupt(button), IRS, RISING);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(path == 1){
    func();
  }else{
    Serial.println(digitalRead(2));
    delay(500);
  }
}


float lat = 28.5458,lon = 77.1703; // create variable for latitude and longitude object
SoftwareSerial gpsSerial(3,4);//rx,tx
TinyGPS gps; // create gps object
void setup(){
Serial.begin(9600); // connect serial
//Serial.println("The GPS Received Signal:");
gpsSerial.begin(9600); // connect gps sensor
}

void loop(){
  while(gpsSerial.available()){ // check for gps data
  if(gps.encode(gpsSerial.read()))// encode gps data
  {
  gps.f_get_position(&lat,&lon); // get latitude and longitude
  // display position

  Serial.print("Position: ");
  Serial.print("Latitude:");
  Serial.print(lat,6);
  Serial.print(";");
  Serial.print("Longitude:");
  Serial.println(lon,6);

  Serial.print(lat);
  Serial.print(" ");

 }
}
String latitude = String(lat,6);
  String longitude = String(lon,6);
Serial.println(latitude+";"+longitude);
delay(1000);
}
*/
