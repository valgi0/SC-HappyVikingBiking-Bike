#ifndef __unlock_task__
#define __unlock_task__
#include "Task.h"
#include "mylib.h"
#include <Arduino.h>

class UnlockTask : public Task{
  int *_state;

public:
  UnlockTask();
  void init(int period);
  void tick(int *state);
  void start(int *state);

private:
  void waitForRaspBerry(){
    int complete=FALSE;
    String buffer;
    int k1, k2;
    // wait until serail is available
    while(!Serial.available()){
      delay(100);
    }
    delay(500);
    //Serial.println("UT: Found something");
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
