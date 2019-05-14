#include <Arduino.h>
#include "Scheduler.h"
#include "Task.h"
#include "debugTask.h"

#define PERIOD 500

Scheduler scheduler;

void setup() {
  Serial.begin(9600);
  scheduler.init(PERIOD);
  Task* t1 = new debugTask("I'am a task", 1);
  t1 -> init(1000);
  scheduler.addTask(t1);

}

void loop() {
  scheduler.schedule();
}
