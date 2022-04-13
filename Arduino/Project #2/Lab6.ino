//Hermann Yepdjio
// cs312
//Lab6
int ledPin=13;  //pin used for output
void setup() 
{
  Serial.begin(9600);  //open serial port at 9600 bps
  pinMode(ledPin, OUTPUT); //sets the digital pin as output
}

void loop() 
{
      for (int i=0; i<=255; i=i+25) //for loop with increments of 25
      {
        analogWrite(ledPin, i);  // push value of in analogWrite() function
        Serial.println(i);  //print value of i in serial.println
        delay(1000);  //allow a delay of 1s 
      }

}
