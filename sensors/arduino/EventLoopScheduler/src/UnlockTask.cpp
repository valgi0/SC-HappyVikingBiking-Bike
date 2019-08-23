#include "UnlockTask.h"
#include <Arduino.h>
#include "mylib.h"

int * p_state;
int debouncingone = 0;
int debouncingtwo = 0;
int accettable_delay = 1000;

void handleUnlookingBikeInterrupt();
void handleLock();

void UnlockTask::start(int *state){
  //Serial.println("UnlockTask is waiting");
  this -> _state = state;
  p_state = state;
  attachInterrupt(digitalPinToInterrupt(state[PIN_BUTTON]),
  handleUnlookingBikeInterrupt, RISING);
}

void handleUnlookingBikeInterrupt(){
  noInterrupts();
  if((millis() - debouncingone) > accettable_delay){

    detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
    attachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]),
     handleLock, RISING);
    //Serial.println("Interupt called");
    //Serial.flush();
    //UnlockTask::instance() -> waitForRaspBerry();
    p_state[STATE] = UNLOOKING;
    debouncingone = millis();
  }
  interrupts();
}

void handleLock(){
  noInterrupts();
  Serial.println("Cliccato");
  if((millis() - debouncingone) > accettable_delay){
    Serial.print(LOCK);
    detachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]));
    p_state[STATE] = SLEEPING;
    attachInterrupt(digitalPinToInterrupt(p_state[PIN_BUTTON]),
    handleUnlookingBikeInterrupt, RISING);
    debouncingone = millis();
  }
  interrupts();
}
