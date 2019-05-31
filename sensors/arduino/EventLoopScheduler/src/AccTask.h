#ifndef __ACC__H__
#define __ACC__H__

#include<Arduino.h>
#include "mylib.h"
#include "Task.h"
#include "AcceleroMMA7361.h"


class AccTask : public Task{
  //pins
  int x,y,z,gsel,sleep,selftest, zg;
  int xvalue, yvalue, zvalue;
  int xhistory[HISTORY_SIZE_ACC], yhistory[HISTORY_SIZE_ACC], zhistory[HISTORY_SIZE_ACC];
  int seeker = 0;
  AcceleroMMA7361 accelero;


public:
  AccTask(int x, int y, int z, int gsel, int sleep, int selftest, int zg);
  void tick(int* state);
  void init(int period);
private:
  void checkIfItIsDown(int *state, int x, int y, int z){

    //chek if bike is vertical
    if(y > OFFSET_X && z >= OFFSET_Z){
      state[RESULT_ACC] = TRUE;
    }
  };

  void updateHistoryValue(int x, int y, int z){
    xhistory[seeker] = x;
    yhistory[seeker] = y;
    zhistory[seeker] = z;
    seeker ++;
    seeker = seeker == HISTORY_SIZE_ACC ? 0:seeker;
  };

  void recalibrate(){
    accelero.calibrate();
  };
};

#endif
