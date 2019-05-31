#include <Arduino.h>
#include "Scheduler.h"
#include "Task.h"
#include "LightSensorTask.h"
#include "debugTask.h"
#include "SerialTask.h"
#include "UnlockTask.h"
#include "AirPollutionTask.h"
#include "GPSTask.h"
#include "AccTask.h"
#include "mylib.h"

// default value for all system
const int def_led_light_pin = 13;
const int def_period = 100;
const int def_sensot_period = 1500;
const int def_threshold = 100;
const int def_light_sensor_pin = A0;
const int def_serial_period = 300;
const int def_button_pin = 2;
const int def_pollution_sensor_pin = A1;
const int def_pollution_sensor_period = 3000;
const int def_gps_period = 500;
const int def_acc_period = 100;
const int def_acc_x_pin = A2;
const int def_acc_y_pin = A3;
const int def_acc_z_pin = A4;
const int def_acc_gzero_pin = 12;
const int def_acc_sleep_pin = 11;
const int def_acc_gsel_pin = 10;
const int def_acc_selftest_pin = 9;
const Scheduler scheduler;

//state
int internalState[STATE_SIZE] = {'\0'};

// set up singleton
UnlockTask *UnlockTask::s_instance = 0;

//chords
float lat, lon;


void setup() {

  //state set up
  internalState[SCHEDULER_PERIOD] = def_period;
  internalState[LIGHTSENSOR_PERIOD] = def_sensot_period;
  internalState[PIN_LED_LIGHT] = def_led_light_pin;
  internalState[PIN_SENSOR_LIGHT] = def_light_sensor_pin;
  internalState[THRESHOLD] = def_threshold;
  internalState[SERIAL_TASK_PERIOD] = def_serial_period;
  internalState[PIN_BUTTON] = def_button_pin;
  internalState[STATE] = SLEEPING;
  internalState[PIN_POLLUTION_SENSOR] = def_pollution_sensor_pin;
  internalState[POLLUTION_SENSOR_PERIOD] = def_pollution_sensor_period;
  internalState[RESULT_GPS_LAT] = (int)&lat;
  internalState[RESULT_GPS_LONG] = (int)&lon;
  internalState[GPS_PERIOD] = def_gps_period;
  internalState[PIN_ACC_X] = def_acc_x_pin;
  internalState[PIN_ACC_Y] = def_acc_y_pin;
  internalState[PIN_ACC_Z] = def_acc_z_pin;
  internalState[PIN_ACC_GSEL] = def_acc_gsel_pin;
  internalState[PIN_ACC_SLEEP] = def_acc_sleep_pin;
  internalState[PIN_ACC_SELFTEST] = def_acc_selftest_pin;
  internalState[PIN_ACC_ZG] = def_acc_gzero_pin;



  // initial setup for arduino
  Serial.begin(9600);
  pinMode(internalState[PIN_LED_LIGHT], OUTPUT);
  pinMode(internalState[PIN_BUTTON], INPUT);

  // initial set up for scheduler
  scheduler.init(internalState[SCHEDULER_PERIOD], internalState);


  // tasks generation

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

  Task* t4 = new AirPollutionTask();
  t4 -> init(internalState[POLLUTION_SENSOR_PERIOD]);
  scheduler.addTask(t4);

  Task* t5 = new GPSTask();
  t5 -> init(internalState[GPS_PERIOD]);
  scheduler.addTask(t5);

  Task* t6 = new AccTask(
    internalState[PIN_ACC_X],
    internalState[PIN_ACC_Y],
    internalState[PIN_ACC_Z],
    internalState[PIN_ACC_GSEL],
    internalState[PIN_ACC_SLEEP],
    internalState[PIN_ACC_SELFTEST],
    internalState[PIN_ACC_ZG]
  );
  t6 -> init(internalState[ACC_PERIOD]);
  scheduler.addTask(t6);

  // Let's start
  UnlockTask::instance() -> start(internalState);
}



void loop() {
  switch(internalState[STATE]){
    case RUNNING: scheduler.schedule();
      break;
    case SLEEPING: delay(200);
    //Serial.println("I'm in sleeping mode");
      break;
    case UNLOOKING: UnlockTask::instance()->waitForRaspBerry();
      break;
  }
}
