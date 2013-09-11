/*
  Web Server
 
 A simple web server that shows the value of the analog input pins.
 using an Arduino Wiznet Ethernet shield. 
 
 Circuit:
 * Ethernet shield attached to pins 10, 11, 12, 13
 * Analog inputs attached to pins A0 through A5 (optional)
 
 created 18 Dec 2009
 by David A. Mellis0
 
 modified 9 Apr 2012
 by Tom Igoe
 
 */

#include <SPI.h>
#include <Ethernet.h>

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = { 
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress local_ip(10,0,0,2);
int led1 = 11;
int led2 = 12;
int butonPin = 2;
int power = 0;
boolean authentic = 0;
int control = 0;

// Initialize the Ethernet server library
// with the IP address and port you want to use 
// (port 80 is default for HTTP):
EthernetServer server(3000);

void setup() {
 // Open serial communications and wait for port to open:
  Serial.begin(9600);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(butonPin, INPUT);
   while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }


  // start the Ethernet connection and the server:
  //mac_address, IPAddress local_ip, IPAddress dns_server, IPAddress gateway, IPAddress subnet
  Ethernet.begin(mac,local_ip);
  server.begin();
  Serial.print("server is at ");
  Serial.println(Ethernet.localIP());
}

boolean powerStatus(){
 return digitalRead(led1) == LOW; 
}
void switchPower(){
  if (powerStatus()){
    digitalWrite(led1, HIGH);          
  }else {
    digitalWrite(led1, LOW);
  }
  
}



void switchButton(){
     if (!power == digitalRead(butonPin)){
        power = !power;
        switchPower();
    } 
  
  
  
  
}
void loop() {
  // listen for incoming clients
  EthernetClient client = server.available();
  if (client) {
    Serial.println("new client");
    
    // an http request ends with a blank line
    String * atual  = &login;
    boolean currentLineIsBlank = true;
    while(client.connected()){
       switchButton();


       if (client.available()) {
        char c = client.read();
        Serial.write(c);
        atual += c;
        atual++;
        if (c == '\n'){
          Serial.write("dei enter\n");
          
        }
        if (!authentic ) {
          if (c == '0'){
             switchPower();
          }else if (c == '1'){
            client.write("Ventilador_01 : "); 
             powerStatus()? client.write("ligado\n") :client.write("desligado\n");
          }
        }         
      }
    
  }
    // give the web browser time to receive the data
    delay(1);
    // close the connection:
    client.stop();
    Serial.println("client disonnected");
  }else {
    switchButton();
  }
}

