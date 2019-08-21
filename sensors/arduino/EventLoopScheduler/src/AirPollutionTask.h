#ifndef __AIR__P_
#define __AIR_P_
#include "Task.h"
#include "mylib.h"

class AirPollutionTask: public Task{
public:
  void tick(int *state);
  void init(int period);

};



#endif
