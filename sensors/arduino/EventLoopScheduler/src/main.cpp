#include <Arduino.h>
#include "Scheduler.h"
#include "Task.h"
#include "LightSensorTask.h"
#include "debugTask.h"
#include "mylib.h"

// default value for all system
const int def_led_light = 13;
const int def_period = 500;
const int def_threshold = 15;
const int def_light_sensor = A0;
const int def_serial_period = 2000;
const Scheduler scheduler;

//state
int internalState[20] = {'\0'};






void setup() {

  //state set up
  internalState[SCHEDULER_PERIOD] = def_period;
  internalState[LIGHTSENSOR_PERIOD] = 1500;
  internalState[LED_LIGHT] = def_led_light;
  internalState[SENSOR_LIGHT] = def_light_sensor;
  internalState[THRESHOLD] = def_threshold;
  internalState[SERIAL_TASK_PERIOD] = def_serial_period;

  // initial setup for arduino
  Serial.begin(9600);
  pinMode(internalState[LED_LIGHT], OUTPUT);

  // initial set up for scheduler
  scheduler.init(internalState[SCHEDULER_PERIOD], internalState);

  // tasks generation
  Task* t1 = new debugTask("I'am a task", 1);
  t1 -> init(1000);
  scheduler.addTask(t1);

  Task* t2 = new LightSensorTask(
    internalState[LED_LIGHT],
    internalState[SENSOR_LIGHT],
    internalState[THRESHOLD]
  );
  t2 -> init(internalState[LIGHTSENSOR_PERIOD]);
  scheduler.addTask(t2);
}



void loop() {
  scheduler.schedule();
}
