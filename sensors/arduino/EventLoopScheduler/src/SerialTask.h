#ifndef __SERIAL_TSK__
#define __SERIAL_TSK__

#include "Task.h"
#include "mylib.h"
#include<Arduino.h>


//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// this task check difference between an old verision of the system state
// and a new updated version. If find difference in result section it check
// wich is the corrispondent sensor and send on serial port a message:
//   KEY:VALUE
//Where KEY poits to a sensor
//Value: the value changed
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


class SerialTask : public Task {
 // old state
  int old_state[STATE_SIZE] = {'\0'};

  //keys for serial comunication
  const char* key_light = LIGHT;
  const char* key_gps =GPS;
  const char* key_pollution =POLLUTION;
  const char* key_collision = COLLISION;

public:
  SerialTask(int *first_state);
  void tick(int *state);
  void init(int period);

private:
  //check the result section looking for changes
  int updateAndCheckState(int *new_state){
    for(int i = RESULT_SECTION; i < STATE_SIZE; i++){
      //if(new_state[i] != old_state[i]){
        //if find one it prepares a message KEY:VALUE

        switch (i) {
        //  case RESULT_LIGHT: Serial.print(key_light);
        //    Serial.println(new_state[i]);
        //    break;
          case RESULT_GPS_LONG: Serial.print(key_gps);
            Serial.print(GPS_LAT);
            Serial.print(*((float*)new_state[RESULT_GPS_LAT]));
            Serial.print(GPS_LON);
            Serial.println(*((float*)new_state[RESULT_GPS_LONG]));
            break;
          case RESULT_POLLUTION: Serial.print(key_pollution);
            Serial.print(GPS_LAT);
            Serial.print(*((float*)new_state[RESULT_GPS_LAT]), 7);
            Serial.print(GPS_LON);
            Serial.print(*((float*)new_state[RESULT_GPS_LONG]), 7);
            Serial.print(",pol=");
            Serial.println(new_state[i]);
            break;
          case RESULT_ACC:
            if(new_state[i]){
              Serial.print(key_collision);
              Serial.print(GPS_LAT);
              Serial.print(*((float*)new_state[RESULT_GPS_LAT]), 7);
              Serial.print(GPS_LON);
              Serial.println(*((float*)new_state[RESULT_GPS_LONG]), 7);
            }
            old_state[i] = FALSE;
            new_state[i] = FALSE;
        }

        //update the old state to the new one
        old_state[i] = new_state[i];
      //}
    }
  };


};

#endif
