# Team Members: Hermann Yepdjio and Khamsavath Joe Muongmany
import random
from datetime import datetime

def main():
    #This block of code is for part 1 of the lab
    #get current time
    today = datetime.now()

    # seeds the random number generator with the current time value
    random.seed(datetime.now())
    value1=random.randrange(0, 25)
    value2=random.randrange(0, 25)
    
    #compute the mean between 2 numbers
    result = (value1 + value2)/2
    
    #print result to the shell
    print ("the average between", value1, "and", value2, "is: ", result, " .\n\n")

    

    #Theses lines of code is for part 2 of the lab assignment
    # generate a random number between 50 and 70 (50 and 70 included)
    value1=random.randrange(50, 71)

    # generate a random number between 50 and 70 (50 and 70 included)
    value2=random.randrange(50, 71)
    value3=random.randrange(50, 71)
    value4=random.randrange(50, 71)
    value5=random.randrange(50, 71)

    #compute the mean of 5 numbers
    result = (value1 + value2 + value3 + value4+ value5)/5

    #print result to the shell
    print ("the average between", value1, value2, value3, value4, "and", value5, "is: ", result, " .\n\n") 
   
if __name__== '__main__':
    main()



    # Question 3:
        #We think the best way  would be to create an array of int of size 100 and then use a for loop to generate 100 random numbers
        #and store them in the array. then use another loop to add them all up and then devide the result by 100
