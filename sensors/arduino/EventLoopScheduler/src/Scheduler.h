#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Timer.h"
#include "Task.h"

#define MAX_TASKS 10

class Scheduler {

  int basePeriod;
  int nTasks;
  Task* taskList[MAX_TASKS];
  Timer timer;
  int *state;

public:
  void init(int basePeriod, int * state);
  virtual bool addTask(Task* task);
  virtual void schedule();
};

#endif
