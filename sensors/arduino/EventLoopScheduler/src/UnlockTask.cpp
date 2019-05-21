#include "Task.h"
#include "UnlockTask.h"

UnlockTask::UnlockTask(){

}

void UnlockTask::init(int period){
  Task::init(period);
};

void UnlockTask::tick(int *state){
}

void UnlockTask::start(int *state){
  //Serial.println("UnlockTask is waiting");

  this -> _state = state;
  this -> waitForRaspBerry();
}
