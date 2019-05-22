#include<Arduino.h>
#include "mylib.h"
#include "AirPollutionTask.h"
#include "Task.h"



void AirPollutionTask::tick(int* state){
  int value = 0;
  value = analogRead(state[PIN_POLLUTION_SENSOR]);
  state[RESULT_POLLUTION] = value;
}


void AirPollutionTask::init(int period){
  Task::init(period);
}
