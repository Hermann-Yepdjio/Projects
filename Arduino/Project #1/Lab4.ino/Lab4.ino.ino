//Hermann Yepdjio
//cs312_001
//Lab4
//2-14-2017
int green=13;  //green ledPin
int yellow=12;  //yellow ledPin 
int red=11;  //red ledPin
void setup() 
{
  // put your setup code here, to run once:
  pinMode(green, OUTPUT); //sets the digital pin as output for green
  pinMode(yellow, OUTPUT); //sets the digital pin as output for yellow
  pinMode(red, OUTPUT); //sets the digital pin as output for red
}

void loop()  //loops
{
  // put your main code here, to run repeatedly:
  digitalWrite(green, HIGH); //sets the green LED on
  delay(2000);   //waits for 2 seconds
  digitalWrite(green, LOW);  //sets the green LED off
  digitalWrite(yellow, HIGH); //sets the yellow LED on
  delay(2000);               //waits for 2 seconds
  digitalWrite(yellow, LOW);  // sets the yellow LED off
  digitalWrite(red, HIGH);  //sets the red LED on
  delay(2000);          // waits for 2 seconds
  digitalWrite(red, LOW);   //sets the red LED off
}
