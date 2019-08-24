#ifndef __TASK__
#define __TASK__


/********************
Class Task
**********************/


class Task {
      int myPeriod; // How often a task must be execute
      int timeElapsed; //Counter to chek the time

    public:

      /***
      Method to intialize the Task
      *****/
        virtual void init(int period){
              myPeriod = period;
              timeElapsed = 0;
        }

      /*******
      Method to execute the task
      ********/
        virtual void tick(int * state) = 0;

      /*******
      Method usefull to check if it's time to tick()
      ********/
        bool updateAndCheckTime(int basePeriod){
              timeElapsed += basePeriod;
              if (timeElapsed >= myPeriod){
                timeElapsed = 0;
                return true;
              } else {
                return false;
              }
        }};
#endif
