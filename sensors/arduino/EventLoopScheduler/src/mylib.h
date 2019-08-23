#ifndef __MY__LIB__
#define __MY__LIB__

#define STATE_SIZE 35
#define RESULT_SECTION 28



/********************************************************
            state cells definietion
*********************************************************/

//periods
#define SCHEDULER_PERIOD 0
#define LIGHTSENSOR_PERIOD 1
#define SERIAL_TASK_PERIOD 2
#define POLLUTION_SENSOR_PERIOD 3
#define GPS_PERIOD 4
#define ACC_PERIOD 5

// pins
#define PIN_LED_LIGHT 8
#define PIN_SENSOR_LIGHT 9
#define PIN_BUTTON 10
#define PIN_BUTTON2 7
#define PIN_POLLUTION_SENSOR 11
#define PIN_ACC_X 12
#define PIN_ACC_Y 13
#define PIN_ACC_Z 14
#define PIN_ACC_ZG 15
#define PIN_ACC_SELFTEST 16
#define PIN_ACC_GSEL 17
#define PIN_ACC_SLEEP 18

//extrasettings
#define THRESHOLD 20
#define STATE 21
#define MEMORY_SIZE_ACC 22
#define WINDOW_VALUES_ACC 23

//result
#define RESULT_ACC 30
#define RESULT_LIGHT 31
#define RESULT_GPS_LONG 32
#define RESULT_GPS_LAT 33
#define RESULT_POLLUTION 34


/*************************************************
            COMUNICATION WITH RASPBERRY
**************************************************/

// raspberry comunication
#define SETUP "SETUP:"
#define LOCK "LOCK:"
#define UNLOCK "UNLOCK:"
#define SU_LIGHT "LIGHT="


//key
#define LIGHT "LIGHT:"
#define GPS "GPS:"
#define POLLUTION "AQ:"  //AQ:lat=11111,lon=11111,pol=11111
#define GPS_LAT "lat="  //GPS:lat=11111,lon=111111
#define GPS_LON ",lon="
#define COLLISION "COLL:" //COLL:lat=1111,log=11111
#define FAKE_GPS "lat=1111111,lon=1111111,"




/********************************************
        GENERAL SETTINGS
+++++++++++++++++++++++++++++++++++++++++++++*/

//General setting
#define FALSE 0
#define TRUE 1
#define LEN(x) sizeof(x)/sizeof(*x)
#define MINIMUM_LIGHT_ALLOWED 20
#define HISTORY_SIZE_ACC 30

// acc SETTINGS

#define OFFSET_Z 110
#define OFFSET_Y 30
#define OFFSET_X 10

// State Arduino
#define RUNNING 1
#define SLEEPING 0
#define UNLOOKING 2
#define LOOKING 3


#endif
