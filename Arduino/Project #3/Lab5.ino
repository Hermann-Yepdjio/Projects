//Hermann Yepdjio
//cs312
//Lab5
int output=13;  //
int timing[9];
void setup()
{
   
   pinMode(output, OUTPUT); //sets the digital pin as output for output
    for (int i= 0; i<9; i++)  //fill the array with appropriate timing data for each letter
    {
      if (i<3 || i>6)  //fill the array with timing data for letter S
        timing[i]=2000;
      else      //fill the array with timing data for letter O
         timing[i]=5000;  
    }

}
void extFunction(int i)  //externnal function to the pin to high or low
{
  digitalWrite(output, HIGH);   // sets the LED on
  delay(i);                  // waits for i/1000 seconds
  digitalWrite(output, LOW);    // sets the LED off
  delay(1000);                  // waits for a second
}
void loop()   // 
{
  for (int i=0; i<9; i++) 
      extFunction(timing[i]);
}



