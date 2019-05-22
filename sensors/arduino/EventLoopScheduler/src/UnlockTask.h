#ifndef __UNLOCK_TASK__
#define __UNLOCK_TASK__
#include "Task.h"
#include "mylib.h"
#include <Arduino.h>

class UnlockTask{
  int *_state;
  static UnlockTask *s_instance;

public:
  void start(int *state);

  static UnlockTask* instance(){
    if(!s_instance){
      s_instance = new UnlockTask;
    }
    return s_instance;
  }

  void waitForRaspBerry(){
    _state[STATE] = RUNNING;
    Serial.write("Im wating for raspberry\n");
    Serial.flush();
    delay(500);
    int complete=FALSE;
    String buffer;
    int k1, k2;
    // wait until serail is available
    while(Serial.available() == 0){
      delay(100);
    }
    delay(500);
    Serial.println("UT: Found something");
    Serial.flush();
    // read until all keys are read
    while(!complete){
      buffer.concat(Serial.readString());
      //Serial.print("BUFFER READ: ");Serial.println(buffer);
      //Serial.println(buffer.indexOf(SETUP));
      //buffer.indexOf(SETUP);
      k1 = buffer.indexOf(SU_LIGHT);
      k2 = buffer.indexOf("END");
      //Serial.println(k1);
      //Serial.println(k2);
      //Serial.println("I found Every thing");
      // ok all keys are in
      complete = TRUE;
      String light = buffer.substring(k1 + LEN(SU_LIGHT) -1, k2);
      _state[THRESHOLD] = light.toInt();
    }
    return;
  };
};







#endif
