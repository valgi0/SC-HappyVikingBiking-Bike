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

    void tick(){
      Serial.write(debugString);
      Serial.write("\n\n");
    }
};

#endif
