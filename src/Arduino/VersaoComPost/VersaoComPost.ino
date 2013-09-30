/* Web_Parms_1.pde - very simple Webduino example of parameter passing and parsing */

/*
 * This is mostly a tool for testing and debugging the library, but can
 * also be used as an example of coding for it.
 *
 * To use it,  enter one of the following USLs into your browser.
 * Replace "host" with the IP address assigned to the Arduino.
 *
 * http://host/
 * http://host/index.html
 * mais algumnacoisa
 *
 * These return a "success" HTTP result and display the parameters
 * (if any) passed to them as a single string,  without attempting to
 * parse them.  This is done with a call to defaultCmd.
 * 
 * 
 * http://host/raw.html
 *
 * This is essentially the same as the index.html URL processing,
 * but is done by calling rawCmd.
 * 
 * 
 * http://host/parsed.html
 *
 * This invokes parsedCmd,  which displays the "raw" parameter string,
 * but also uses the "nexyURLparam" routine to parse out the individual
 * parameters, and display them.
 */


#define WEBDUINO_FAIL_MESSAGE "<h1>Request Failed</h1>"
#include "SPI.h" // new include
#include "avr/pgmspace.h" // new include
#include "Ethernet.h"
#include "WebServer.h"
#include "User.h"

#define VERSION_STRING "0.1"

#define Lampada_Suite_switch 1
#define Lampada_Suite_pin 11
#define Ventilador_Suite_switch 2
#define Ventilador_Suite_pin 12
Device allDevices[2];



const String uUserName = "root";
const String uPassword = "ZqGUJQen4KuvQJgbyrRGhYrbuMbXyKPV26zHLJmH";
int id = -1;
String user = "";
String pass = "";
String status01 = "";

bool auth = false;

/* CHANGE THIS TO YOUR OWN UNIQUE VALUE.  The MAC number should be
 * different from any other devices on your network or you'll have
 * problems receiving packets. */
static uint8_t mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };


/* CHANGE THIS TO MATCH YOUR HOST NETWORK.  Most home networks are in
 * the 192.168.0.XXX or 192.168.1.XXX subrange.  Pick an address
 * that's not in use and isn't going to be automatically allocated by
 * DHCP from your router. */
static uint8_t ip[] = { 
  10, 0, 0, 2 };

// ROM-based messages used by the application
// These are needed to avoid having the strings use up our limited
//    amount of RAM.

P(Default_head) = "unidentified URL requested.</h1><br>\n";
P(Raw_head) = "raw.html requested.</h1><br>\n";
P(Parsed_head) = "parsed.html requested.</h1><br>\n";
P(Good_tail_begin) = "URL tail = '";
P(Bad_tail_begin) = "INCOMPLETE URL tail = '";
P(Tail_end) = "'<br>\n";
P(Parsed_tail_begin) = "URL parameters:<br>\n";
P(Parsed_item_separator) = " = '";
P(Post_params_begin) = "Parameters sent by POST:<br>\n";
P(Line_break) = "<br>\n";
P(Json_begin) = "{ \"devices\" : [";
P(aspas)  = "\"";
P(virgula) = ", ";
P(igual) = " = ";
P(Json_end) = "]}";
P(Page_end) = "</body></html>";
P(Get_head) = "<h1>GET from ";
P(Post_head) = "<h1>POST to ";
P(Unknown_head) = "<h1>UNKNOWN request for ";
P(Page_start) = "<html><head><title>Web_Parms_1 Version " VERSION_STRING "</title></head><body>\n";
P(Params_end) = "End of parameters<br>\n";



/* This creates an instance of the webserver.  By specifying a prefix
 * of "", all pages will be at the root of the server. */
#define PREFIX ""
WebServer webserver(PREFIX, 5000);
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
bool switch_power_auth(){
  Serial.println(user);
    Serial.println(pass);
      Serial.println(id);
        Serial.println(status01);
  authentic();
  String s2 = String ("HIGH");
  if (auth){
    if (s2 == status01)
      digitalWrite(allDevices[id].switch_pin, LOW);
    else
      digitalWrite(allDevices[id].switch_pin, HIGH);
  }
  
  Serial.println("mudou "+ (String) allDevices[id].name);

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
  toReturn += ", \"name\" : \"";
  toReturn += dev.name;
  toReturn += "\" , \"status\" : ";
  toReturn += digitalRead(dev.button_pin)? "\"on\"" : "\"off\"" ;
  toReturn += "}";
  return toReturn;
}

void parsedCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete)
{
  
  URLPARAM_RESULT rc;
  char name[NAMELEN];
  int  name_len;
  char value[VALUELEN];
  int value_len;


  //Serial.println(toStringDevice(allDevices[0]));
  // Serial.println(toStringDevice(allDevices[1]));
  /* this line sends the standard "we're all OK" headers back to the
   browser */
  server.httpSuccess();

  /* if we're handling a GET or POST, we can output our data here.
   For a HEAD request, we just stop after outputting headers. */
  if (type == WebServer::HEAD)
    return;

  //server.printP(Page_start);
  switch (type)
  {
    case WebServer::GET:
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

            status01 = String(value);

          }
          else if (nameParans ==(String) "id"){
           if ((String) value == (String )"0")
            id = 0;
           else
             id = 1;
         
          
          }
          //*/

        }
      }
     
     ///* 
     if (switch_power_auth()){
        server.print("passou");

      }
      else{
        server.print("login/pass error");

      }
      status01 = "";
      user = "";
      pass = "";
      id=  -1;
    }

    break;
    //default:

    // server.printP(Unknown_head);
  }

  //server.printP(Parsed_head);
  //server.printP(tail_complete ? Good_tail_begin : Bad_tail_begin);
  //server.print(url_tail);
  //server.printP(Tail_end);


  /*
  if (type == WebServer::POST)
   {
   server.printP(Post_params_begin);
   while (server.nextURLparam(&url_tail, name, NAMELEN, value, VALUELEN))
   {
   server.print(name);
   server.printP(Parsed_item_separator);
   server.print(value);
   server.printP(Tail_end);
   }
   }
   server.printP(Page_end);
   */
}

void my_failCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete)
{
  /* this line sends the standard "we're all OK" headers back to the
   browser */
  Serial.println("fail page");
  server.httpFail();

  /* if we're handling a GET or POST, we can output our data here.
   For a HEAD request, we just stop after outputting headers. */
  if (type == WebServer::HEAD)
    return;

  server.printP(Page_start);
  switch (type)
  {
    case WebServer::GET:
    server.printP(Get_head);
    break;
    case WebServer::POST:
    server.printP(Post_head);
    break;
  default:
    server.printP(Unknown_head);
  }

  server.printP(Default_head);
  server.printP(tail_complete ? Good_tail_begin : Bad_tail_begin);
  server.print(url_tail);
  server.printP(Tail_end);
  server.printP(Page_end);

}




void setup()
{
  /* initialize the Ethernet adapter */
  Ethernet.begin(mac, ip);
  Serial.begin(9600);
  Device Ventilador;
  {
  Ventilador.id = 0;
  Ventilador.name = "Ventilador_Suite";
  Ventilador.onTime= false;
  Ventilador.time = 0;
  Ventilador.switch_pin = 8;
  Ventilador.button_pin = 2;
  }
  
  Device Lampada;
  {
  Lampada.id = 1;
  Lampada.name = "Lampada_Suite";
  Lampada.onTime= false;
  Lampada.time = 0;
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
  webserver.addCommand("control.html", &parsedCmd);

  /* start the webserver */
  webserver.begin();

}

void loop()
{
  char buff[256];
  int len = 256;
  int i ;
  
  if (allDevices[0].last_status != digitalRead(allDevices[0].button_pin)){
    allDevices[0].last_status = !allDevices[0].last_status;  
    switch_power(0);
  }


 

  /* process incoming connections one at a time forever */
  webserver.processConnection(buff, &len);
}





