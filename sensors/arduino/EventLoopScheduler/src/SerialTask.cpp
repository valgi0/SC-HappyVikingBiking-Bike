#include "SerialTask.h"
#include<Arduino.h>
#include "Task.h"
#include "mylib.h"


SerialTask::SerialTask(int *first_state){
  for(int i = 0; i < STATE_SIZE; i ++){
    this -> old_state[i] = first_state[i];
  }
};


void SerialTask::init(int period){
  Task::init(period);
};


void SerialTask::tick(int *state){
  this -> updateAndCheckState(state);
};
