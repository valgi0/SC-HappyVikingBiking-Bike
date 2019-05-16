#ifndef __LIGHTSENSOR__
#define __LIGHTSENSOR__

#include<Arduino.h>
#include "Task.h"

#define OFFLED 0
#define ONLED 1


class LightSensorTask : public Task {

  // pin for photo the resistence -> Analogic pin
  int pinSensor;

  //pin for leg: digital
  int pinLed;

  // value of light turn on the led
  int threshold;

  // a windows of tollerance to avoid blinking
  int offset = 3;

  // led state
  int state = OFFLED;

public:
  LightSensorTask(const int _led, const int _sensor, const int _thresh);
  void init(int period);
  void tick(int *state);

private:

  // Update internal state
  void changeState(const int _state){
    if(_state == OFFLED){
      threshold = threshold - offset;
      digitalWrite(pinLed, LOW);
    }else{
      threshold = threshold + offset;
      digitalWrite(pinLed, HIGH);
    }
    Serial.print("Threshold: ");
    Serial.println(threshold);
    state = _state;
  };
};


#endif
