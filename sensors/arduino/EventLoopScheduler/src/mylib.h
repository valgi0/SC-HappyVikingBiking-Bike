#ifndef __MY__LIB__
#define __MY__LIB__

#define STATE_SIZE 20
#define RESULT_SECTION 13

//state cells definietion
#define SCHEDULER_PERIOD 0
#define LIGHTSENSOR_PERIOD 1
#define PIN_LED_LIGHT 2
#define PIN_SENSOR_LIGHT 3
#define THRESHOLD 4
#define SERIAL_TASK_PERIOD 5
#define PIN_BUTTON 6
#define PIN_POLLUTION_SENSOR 7
#define POLLUTION_SENSOR_PERIOD 8
#define GPS_PERIOD 9
#define RESULT_LIGHT 13
#define RESULT_GPS_LONG 14
#define RESULT_GPS_LAT 15
#define RESULT_POLLUTION 16
#define STATE 19

// raspberry comunication
#define SETUP "SETUP:"
#define LOCK "LOCK:"
#define UNLOCK "UNLOCK:"
#define SU_LIGHT "LIGHT="


//key
#define LIGHT "LIGHT:"
#define GPS "GPS:"
#define POLLUTION "AQ:"  //POLLUTION:lat=11111,lon=11111,pol=11111
#define GPS_LAT "lat="  //GPS:lat=11111,lon=111111
#define GPS_LON ",lon="
#define COLLISION "COLL:" //COLL:lat=1111,log=11111

#define FAKE_GPS "lat=1111111,lon=1111111,"




//General setting
#define FALSE 0
#define TRUE 1
#define LEN(x) sizeof(x)/sizeof(*x)
#define MINIMUM_LIGHT_ALLOWED 20

// State Arduino
#define RUNNING 1
#define SLEEPING 0
#define UNLOOKING 2
#define LOOKING 3


#endif
