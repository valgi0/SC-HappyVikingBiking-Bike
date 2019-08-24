#include <Arduino.h>

/*
Resistore collegato a 5v e all'ingresso analogico.
In più tra il resistore e l'inresso montiamo una resistenza
di 100H che lo collega alla massa
*/

int photo = A0;
int led = 13;


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(13, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  int light = analogRead(photo);
  Serial.write("Luce: ");
  Serial.println(light);
  if(light < 8){
    digitalWrite(led, HIGH);
  }else{
    digitalWrite(led, LOW);
  }
  delay(500);
}
