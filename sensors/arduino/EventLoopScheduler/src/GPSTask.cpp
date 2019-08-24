#include<Arduino.h>
#include "GPSTask.h"
#include <SoftwareSerial.h>
#include "TinyGPS.h"

SoftwareSerial gpsSerial(4,3);//rx,tx
TinyGPS gps;

void GPSTask::init(int period){
  Task::init(period);
  gpsSerial.begin(9600);
}

void GPSTask::tick(int *state){
  while(gpsSerial.available()){
    int c = gpsSerial.read();
    if(gps.encode(c)){
      gps.f_get_position((float *) state[RESULT_GPS_LAT], (float*) state[RESULT_GPS_LONG]);
      //Serial.print("Position Read: ");
      //Serial.print("LAT: ");
      //Serial.print(*((float*)state[RESULT_GPS_LAT]), 7);
      //Serial.print("  LON: ");
      //Serial.println(*((float*)state[RESULT_GPS_LONG]), 7);
    }
  }
}
