#include "UnlockTask.h"
#include <Arduino.h>
#include "mylib.h"

int * p_state;

void handleUnlookingBikeInterrupt();
void handleLock();

void UnlockTask::start(int *state){
  Serial.println("UnlockTask is waiting");
  this -> _state = state;
  p_state = state;
  attachInterrupt(digitalPinToInterrupt(state[PIN_BUTTON]),
  handleUnlookingBikeInterrupt, RISING);
}

void handleUnlookingBikeInterrupt(){
  noInterrupts();
  detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
  attachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]),
   handleLock, RISING);
  //Serial.println("Interupt called");
  //Serial.flush();
  //UnlockTask::instance() -> waitForRaspBerry();
  p_state[STATE] = UNLOOKING;
  delay(300);
  interrupts();
}

void handleLock(){
  noInterrupts();
  Serial.print("Look bike interupt");
  detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
  p_state[STATE] = SLEEPING;
  attachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]),
  handleUnlookingBikeInterrupt, RISING);
  delay(300);
  interrupts();
}
