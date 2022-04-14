class clone(object):
	name = ""
	timeCounter=0
	def __init__(self, name, timeC):
		self.name = name
		self.timeCounter = timeC
class starWars(object):
	def create_clone(self):
		num_clone = int(input("total number of clone warrior to be created? :"))
		return num_clone
	def input_clone(self, num_clone):
		clones = []
		for i in range (num_clone):
			name = input ("Inform clone warrior name :")
			timeC= int(input("What is the timeCounter/lifespan :"))
			if (timeC <0):
				print ("The info provided for clone warrior ", name," is incorrect. Therefore this clone will be discarded.")
			clones.append(clone(name, timeC))
		return clones
			
	def life_span(self, name, timeC):
		if(timeC==0):
			print ("The clone ", name," with Timer ",timeC, " is dead.")
		else:
			print ("The clone ", name," with Timer ",timeC, " is alive.")
	def API(self):
		num_clone = self.create_clone()
		clones = self.input_clone(num_clone)
		for i in range(num_clone):
			while (clones[i].timeCounter>=0):
				self.life_span(clones[i].name, clones[i].timeCounter)
				clones[i].timeCounter = clones[i].timeCounter - 1
		print("All clones are dead!")
def main():
	Object = starWars()
	Object.API()
if __name__ == "__main__":
	main()

