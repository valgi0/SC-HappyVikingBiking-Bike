#include "UnlockTask.h"
#include <Arduino.h>
#include "mylib.h"

int * p_state;

void handleInterrupt();

void UnlockTask::start(int *state){
  //Serial.println("UnlockTask is waiting");
  this -> _state = state;
  p_state = state;
  attachInterrupt(digitalPinToInterrupt(state[PIN_BUTTON]),
  handleInterrupt, RISING);
}

void handleInterrupt(){
  detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
  //Serial.println("Interupt called");
  //Serial.flush();
  //UnlockTask::instance() -> waitForRaspBerry();
  p_state[STATE] = UNLOOKING;
}
