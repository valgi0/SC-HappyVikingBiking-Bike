#include <Arduino.h>
#include "Scheduler.h"
#include "Task.h"
#include "LightSensorTask.h"
#include "debugTask.h"
#include "SerialTask.h"
#include "UnlockTask.h"
#include "mylib.h"

// default value for all system
const int def_led_light_pin = 13;
const int def_period = 100;
const int def_sensot_period = 1500;
const int def_threshold = 15;
const int def_light_sensor_pin = A0;
const int def_serial_period = 300;
const Scheduler scheduler;

//state
int internalState[STATE_SIZE] = {'\0'};




void setup() {

  //state set up
  internalState[SCHEDULER_PERIOD] = def_period;
  internalState[LIGHTSENSOR_PERIOD] = def_sensot_period;
  internalState[PIN_LED_LIGHT] = def_led_light_pin;
  internalState[PIN_SENSOR_LIGHT] = def_light_sensor_pin;
  internalState[THRESHOLD] = def_threshold;
  internalState[SERIAL_TASK_PERIOD] = def_serial_period;

  // initial setup for arduino
  Serial.begin(9600);
  pinMode(internalState[PIN_LED_LIGHT], OUTPUT);

  // initial set up for scheduler
  scheduler.init(internalState[SCHEDULER_PERIOD], internalState);

  // Let's start
  Serial.read();
  UnlockTask* unlock = new UnlockTask();
  unlock -> start(internalState);
  Serial.print("Light: "); Serial.println(internalState[PIN_LED_LIGHT]);

  // tasks generation
  Task* t1 = new debugTask("I'am a task", 1);
  t1 -> init(1000);
  //scheduler.addTask(t1);

  Task* t2 = new LightSensorTask(
    internalState[PIN_LED_LIGHT],
    internalState[PIN_SENSOR_LIGHT],
    internalState[THRESHOLD]
  );
  t2 -> init(internalState[LIGHTSENSOR_PERIOD]);
  scheduler.addTask(t2);

  Task* t3 = new SerialTask(internalState);
  t3 -> init(internalState[SERIAL_TASK_PERIOD]);
  scheduler.addTask(t3);


}



void loop() {
  scheduler.schedule();
}
