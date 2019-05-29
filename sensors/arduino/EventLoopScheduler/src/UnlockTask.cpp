#include "UnlockTask.h"
#include <Arduino.h>
#include "mylib.h"

int * p_state;

void handleInterrupt();
void handleLock();

void UnlockTask::start(int *state){
  Serial.println("UnlockTask is waiting");
  this -> _state = state;
  p_state = state;
  attachInterrupt(digitalPinToInterrupt(state[PIN_BUTTON]),
  handleInterrupt, RISING);
}

void handleInterrupt(){
  detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
  noInterrupts();
  attachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]),
   handleLock, RISING);
  //Serial.println("Interupt called");
  //Serial.flush();
  //UnlockTask::instance() -> waitForRaspBerry();
  p_state[STATE] = UNLOOKING;
  interrupts();
}

void handleLock(){
  noInterrupts();
  detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
  p_state[STATE] = SLEEPING;
  attachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]),
  handleInterrupt, RISING);
  interrupts();
}
