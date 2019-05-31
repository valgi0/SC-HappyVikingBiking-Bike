#include<Arduino.h>
#include "mylib.h"
#include "Task.h"
#include "AccTask.h"
#include "AcceleroMMA7361.h"


AccTask::AccTask(int x, int y, int z, int gsel, int sleep, int selftest, int zg){
  this -> x = x;
  this -> y = y;
  this -> z = z;
  this -> gsel = gsel;
  this -> sleep = sleep;
  this -> selftest = selftest;
  this -> zg = zg;
  accelero.begin(sleep, selftest, zg, gsel, x, y, z);
  accelero.setARefVoltage(5);                   //sets the AREF voltage to 3.3V
  accelero.setSensitivity(HIGH);                   //sets the sensitivity to +/-6G
  this -> recalibrate();
}

void AccTask::init(int period){
  Task::init(period);
}

void AccTask::tick(int* state){
  int x = this -> accelero.getXAccel();
  int y = this -> accelero.getYAccel();
  int z = this -> accelero.getZAccel();
  this->checkIfItIsDown(state,x,y,z);
  this -> updateHistoryValue(x,y,z);
}
