#include "LightSensorTask.h"
#include<Arduino.h>
#include "mylib.h"

LightSensorTask::LightSensorTask(
   const int _led,
   const int _sensor,
   const int _thresh
 ){
   /*
   Serial.write("+++++++ LIGHT SENS. SET UP ++++++\n");
   Serial.write("Led: "); Serial.println(_led);
   Serial.write("Sensor: "); Serial.println(_sensor);
   Serial.write("Threshold: "); Serial.println(_thresh);
   */
   int initialLight;
   this -> pinLed = _led;
   this -> pinSensor = _sensor;
   this -> threshold = _thresh;
   initialLight = analogRead(_sensor);
   //Serial.write("InitialLight: "); Serial.println(initialLight);
   if(initialLight >= threshold){
     this -> changeState(OFFLED);
   }else{
     this -> changeState(ONLED);
   }
};

void LightSensorTask::init(int period){
  Task::init(period);
};

void LightSensorTask::tick(int *state){
  int light = analogRead(this -> pinSensor);
  state[RESULT_LIGHT] = light;
  if(state[THRESHOLD] != this -> threshold &&
   state[THRESHOLD] >=  MINIMUM_LIGHT_ALLOWED){
     this -> threshold = state[THRESHOLD];
   }
  /*
  Serial.println("---- TICK Light Sensor -----");
  Serial.print("Light read: " ); Serial.println(light);
  */
  if((light >= this -> threshold && this -> state == OFFLED)
  || (light < this -> threshold && this -> state == ONLED)){
    // everything fine. No need to change state
    return;
  }else{
    // something has changed!
    if(light >= this -> threshold) this -> changeState(OFFLED);
    if(light < this -> threshold) this -> changeState(ONLED);
  }
}
