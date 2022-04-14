import sys
class Person(object):
	name = ""
	age = 0
	def __init__(self, name, age):
		self.name = name
		self.age = age
class Age(object):
	
	def ReadFile(self):
		with open("input.txt") as file:
			counter = 0
			name = ''
			age=0
			ages = []
			for line in file:
				if (counter%2==0):
					name = line.strip()
				else:
					age = int(line)	
					ages.append(Person(name, age))
				counter = counter + 1
		return ages
					
	def ReckonAge(self):
		ages = self.ReadFile()
		Sum = 0
		avg = 0
		count = 0
		for i in range(ages.__len__()):
			Sum = Sum+ages[i].age
			count = count + 1
		if (count!=0):
			avg = Sum/count
		arr = []
		arr.append(Sum)
		arr.append(avg)
		return arr
	def Output(self):
		f = open('output.txt', 'w')
		ages = self.ReadFile()
		arr = self.ReckonAge()
		for i in range(ages.__len__()):
			print(ages[i].name, ' : ' , ages[i].age, "\n")
			f.write(ages[i].name + ' : ' + str(ages[i].age) +"\n")
		print ( "\nSum ages : ", arr[0], "\n")
		print ("Average Age :",arr[1] ,"\n\n")
		f.write("\nSum ages : "+ str(arr[0])+"\n")
		f.write("Average Age :"+ str(arr[1])+"\n")
		f.close()
		if (arr[1]<=50):
			print("YOUNG GROUP.\n")	
		elif(arr[1]<100):
			print("GOOD AGE GROUP.\n")
		else:
			print("TELL THE SECRET")

def main():
	age = Age()
	age.Output()

if __name__=="__main__":
	main()
