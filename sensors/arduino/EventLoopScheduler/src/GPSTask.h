#ifndef __G_P_S__
#define __G_P_S__

#include<Arduino.h>
#include "mylib.h"
#include "Task.h"


class GPSTask : public Task {
public:
  GPSTask(){};
  void init(int period);
  void tick(int *state);
};


#endif
