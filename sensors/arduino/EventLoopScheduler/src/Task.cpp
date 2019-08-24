#include "Task.h"
#include <Arduino.h>


void Task::tick(int * state){
  Serial.begin(9600);
  Serial.write("Test: ");
  for(int i = 0; i < 20; i ++){
  }
}
