class HelicopterData(object):
	fuel =0
	visibility=0
	def __init__(self, fuel, visibility):
	    self.fuel = fuel
	    self.visibility = visibility
class Flying(object):
	num_hel = 0
	def enter_data(self):
		self.num_hel = int(input("How many helicopter are flying? :"))
		helicopters = []
		for i in range (self.num_hel):    
			fuel = int(input("How much fuel is in the helicopter? (Please provide an interger between 0 and 100 refering to the pourcentage): "))
			visibility = int(input("What is the visibility? (Please provide an integer between 0 and 100 refering to the pourcentage): "))
			helicopters.append(HelicopterData(fuel,visibility))
		return helicopters
	def fly_helicopter(self, helicopters):
		for i in range (self.num_hel):
			if (helicopters[i].visibility < 60 ):
				print ("not safe to fly")
			else:
		        	while (helicopters[i].fuel>=10):
			    	    print ("flying now with ", helicopters[i].fuel,"%")	
			    	    helicopters[i].fuel = helicopters[i].fuel - 2
			        print ("low fuel ", helicopters[i].fuel, "% landing now") 
	def API(self):
		helicopters = self.enter_data()
		self.fly_helicopter(helicopters)

def main():
	var = Flying()
	var.API()
if __name__=="__main__":
	main()



