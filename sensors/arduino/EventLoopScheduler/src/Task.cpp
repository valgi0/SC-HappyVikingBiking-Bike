#include "Task.h"
#include <Arduino.h>


void Task::tick(){
  Serial.begin(9600);
  Serial.write("Test");
}
