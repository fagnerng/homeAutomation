#define WEBDUINO_FAIL_MESSAGE "<h1>Request Failed</h1>"
#include "SPI.h" // new include
#include "avr/pgmspace.h" // new include
#include "Ethernet.h"
#include "WebServer.h"
#include "User.h"

#define VERSION_STRING "0.1"

Device allDevices[2];

const String uUserName = "root";
const String uPassword = "ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH";
int id = -1;
String user = "";
String pass = "";
String newStatus = "";
bool auth = false;

static uint8_t mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
static uint8_t ip[] = { 
  10, 0, 0, 2 };

P(Json_begin) = "{ \"devices\" : [";
P(Json_end) = "]}";
#define PREFIX ""
WebServer webserver(PREFIX, 3000);
void authentic(){

  if (uUserName ==  user){
    if( uPassword == pass){
      auth = true;
    }
  }
  else{

    auth = false;

  }
}


#define NAMELEN 128
#define VALUELEN 128
int switch_power_auth(){
  authentic();
  String on = String ("on");
  String off = String ("off");
  if (auth){
    if (on == newStatus)
      digitalWrite(allDevices[id].switch_pin, LOW);
    else if (off == newStatus)
      digitalWrite(allDevices[id].switch_pin, HIGH);
    else return 2;
  }


  return auth;



}
void switch_power(int device){

  if (digitalRead(allDevices[device].switch_pin)==HIGH)
    digitalWrite(allDevices[device].switch_pin, LOW);
  else
    digitalWrite(allDevices[device].switch_pin, HIGH);

}
String toStringDevice(Device dev){

  String toReturn = "{\"id\" : ";
  toReturn += (String) dev.id;
  toReturn += " , \"status\" : ";
  toReturn += !digitalRead(dev.switch_pin)? "\"on\"" : "\"off\"" ;
  //String toReturn = !digitalRead(dev.switch_pin)? "on" : "off" ;  
  toReturn += "}";
  return toReturn;
}

void parsedCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete)
{
  Serial.println(type == WebServer::GET);
  Serial.println(type == WebServer::POST);
  URLPARAM_RESULT rc;
  char name[NAMELEN];
  int  name_len;
  char value[VALUELEN];
  int value_len;
  if (type == WebServer::HEAD)
    return;

  switch (type)
  {
    case WebServer::GET:
    server.httpSuccess();
    server.printP(Json_begin);
    server.print(toStringDevice(allDevices[0]));
    server.print(",");
    server.print(toStringDevice(allDevices[1]));
    server.printP(Json_end);

    break;
    case WebServer::POST:
    if (strlen(url_tail))
    {

      while (strlen(url_tail))
      {
        rc = server.nextURLparam(&url_tail, name, NAMELEN, value, VALUELEN);
        if (rc == URLPARAM_EOS){

          //nothing
        }
        else
        {
          //   /* 
          String nameParans = String (name);
          if (nameParans == (String)"username"){

            user = String(value);
          }
          else if (nameParans == (String)"password"){

            pass = String(value);

          }
          else if (nameParans == (String)"status") {

            newStatus = String(value);

          }
          else if (nameParans ==(String) "id"){
            if ((String) value == (String )"0")
              id = 0;
            else if ((String) value == (String )"1")
              id = 1;
            else id = -1;


          }


        }
      }



      int coder = switch_power_auth();
      Serial.println(coder);
      if (coder==1){
        server.httpSuccess();
      }
      else if (coder==2) {
        server.httpStatusError();
      }
      else{
        server.httpAuthError();
      }
      newStatus = "";
      user = "";
      pass = "";
      id=  -1;
    }
    else{
      server.httpParansError(); 
    }

    break;
  default:
    server.httpFail();
    break;
    // server.printP(Unknown_head);
  }


}

void my_failCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete)
{
  server.httpFail();
}




void setup()
{
  Ethernet.begin(mac, ip);
  Serial.begin(9600);
  Device Ventilador;
  {
    Ventilador.id = 0;
    Ventilador.switch_pin = 8;
    Ventilador.button_pin = 2;
  }

  Device Lampada;
  {
    Lampada.id = 1;
    Lampada.switch_pin = 9;
    Lampada.button_pin = 3;
  }

  allDevices[0] = Ventilador;
  allDevices[1] = Lampada;

  int i ;
  pinMode(allDevices[0].button_pin, INPUT); 
  for (i = 0; i < 2; i++){

    pinMode(allDevices[i].switch_pin, OUTPUT);
    digitalWrite(allDevices[i].switch_pin, HIGH);



  }
  allDevices[0].last_status = digitalRead(allDevices[0].button_pin);
  webserver.addCommand("control", &parsedCmd);
  digitalWrite(2,1);
  digitalWrite(3,1); 
  webserver.begin();

}

void loop()
{
  char buff[256];
  int len = 256;
  int i ;
  for (i = 0; i < 2; i++){
    if (allDevices[i].last_status != digitalRead(allDevices[i].button_pin)){
      allDevices[i].last_status = !allDevices[i].last_status;  
      switch_power(i);
    }
  }



  webserver.processConnection(buff, &len);
}









