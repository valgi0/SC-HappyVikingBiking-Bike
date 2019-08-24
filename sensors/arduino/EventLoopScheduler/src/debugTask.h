#ifndef __debug__task__
#define __debug__task__

#include "Task.h"
#include <Arduino.h>


class debugTask: public Task{
    char* debugString;
    int id;
    int period = 1000;

  public:
    debugTask(const char* string, int _id){
          debugString = string;
          id = _id;
    };

    void init(){
      Task::init(period);
    };

    void tick(int *state){
      Serial.write(debugString);
      Serial.write("\n\n");
      for(int i = 0; i < 20; i++){
        //Serial.print("[");Serial.print(i);Serial.print("]");Serial.print(" -> "); Serial.println(state[i]);
      }
    }
};

#endif
