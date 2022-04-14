import sys, operator
class item(object):
	category =0
	name = ""
	quantity = 0
	price = 0.0
	def __init__(self, cat, name, quant, price):
		self.category = cat
		self.name= name
		self.quantity = quant
		self.price = price
def ReadFile():
	with open("grocery.txt") as file:
		product = []
		for line in file:
			temp = line.split()
			product.append(item(int(temp[0]), temp[1], int(temp[2]), float(temp[3])))
	return product

def ask_receipt_format():
	while(1):
		format = int(input("How would you like the receipt to be printed?\n Please enter 1 for 'by category', 2 for 'by price' and 3 for 'by alphabetical order'\n\n"))
		if format == 1 or format ==2 or format == 3 :
			break
		print("Sorry wrong input! Please pick one of the options provided.\n\n")

	return format

def calculate_total(items):
	total_price=0
	for element in items:
		total_price = total_price + element.quantity * element.price
	return total_price

def print_bill():

	File = open('output_bill.txt', 'w')
	format = ask_receipt_format()
	items = ReadFile()
	total_price = calculate_total(items)
	counter = 0
	if (format == 1):
		print("Receipt by category\n")
		File.write("Receipt by category\n")
		print("C     Name                     Q         Price                    Sub-Total\n")
		File.write("C     Name                  Q       Price                  Sub-Total\n")
		for elt in items:
			if (elt.category == 1):
				print(elt.category,"   ",elt.name + (24-len(elt.name))*" ",str(elt.quantity) + (5-len(str(elt.quantity)))*" ","   ",str( elt.price) + (24-len(str(elt.price)))*" ",elt.quantity*elt.price,"\n")
				File.write(str(elt.category)+"   "+elt.name + (24-len(elt.name))*" "+str(elt.quantity) + (5-len(str(elt.quantity)))*" "+"   "+str( elt.price) + (24-len(str(elt.price)))*" "+str(elt.quantity*elt.price)+"\n")
				counter = counter + 1
		print("Your list is comprised by ",counter," types of products on category 1\n")
		File.write("Your list is comprised by "+str(counter)+" types of products on category 1\n")
		counter =0
		for elt in items:
			if (elt.category == 2):
				print(elt.category,"   ",elt.name + (24-len(elt.name))*" ",str(elt.quantity) + (5-len(str(elt.quantity)))*" ","   ",str( elt.price) + (24-len(str(elt.price)))*" ",elt.quantity*elt.price,"\n")
				File.write(str(elt.category)+"   "+elt.name + (24-len(elt.name))*" "+str(elt.quantity) + (5-len(str(elt.quantity)))*" "+"   "+str( elt.price) + (24-len(str(elt.price)))*" "+str(elt.quantity*elt.price)+"\n")
				counter = counter + 1
		print("Your list is comprised by ",counter," types of products on category 2")
		File.write("Your list is comprised by "+str(counter)+" types of products on category 2")
		print("\n\nBill total:" + " "*54, total_price,"\n\n")
		File.write("\n\nBill total:" + " "*49+ str(total_price)+"\n\n")
		print("\nC:Category\nQ:Quantity\n")
		File.write("\nC:Category\nQ:Quantity\n")
	if (format == 2):
		items.sort(key = operator.attrgetter('price'), reverse = False) 
		print("Receipt by unit Price in Ascending order\n")
		File.write("Receipt by unit Price in Ascending order\n")
		print("C     Name                     Q         Price                    Sub-Total\n")
		File.write("C     Name                  Q       Price                  Sub-Total\n")
		for elt in items:
			print(elt.category,"   ",elt.name + (24-len(elt.name))*" ",str(elt.quantity) + (5-len(str(elt.quantity)))*" ","   ",str( elt.price) + (24-len(str(elt.price)))*" ",elt.quantity*elt.price,"\n")
			File.write(str(elt.category)+"   "+elt.name + (24-len(elt.name))*" "+str(elt.quantity) + (5-len(str(elt.quantity)))*" "+"   "+str( elt.price) + (24-len(str(elt.price)))*" "+str(elt.quantity*elt.price)+"\n")
		print("\n\nBill total:" + " "*54, total_price,"\n\n")
		File.write("\n\nBill total:" + " "*49+str(total_price)+"\n\n")
		print("\nC:Category\nQ:Quantity\n")
		File.write("\nC:Category\nQ:Quantity\n")
	if (format == 3):
		items.sort(key = operator.attrgetter('name'), reverse = False) 
		print("Receipt by alphabetical order\n")
		File.write("Receipt by alphabetical order\n")
		print("C     Name                     Q         Price                    Sub-Total\n")
		File.write("C     Name                  Q       Price                  Sub-Total\n")
		for elt in items:
			print(elt.category,"   ",elt.name + (24-len(elt.name))*" ",str(elt.quantity) + (5-len(str(elt.quantity)))*" ","   ",str( elt.price) + (24-len(str(elt.price)))*" ",elt.quantity*elt.price,"\n")
			File.write(str(elt.category)+"   "+elt.name + (24-len(elt.name))*" "+str(elt.quantity) + (5-len(str(elt.quantity)))*" "+"   "+str( elt.price) + (24-len(str(elt.price)))*" "+str(elt.quantity*elt.price)+"\n")
		print("\n\nBill total:" + " "*54, total_price,"\n\n")
		File.write("\n\nBill total:" + " "*49+ str(total_price)+"\n\n")
		print("\nC:Category\nQ:Quantity\n")
		File.write("\nC:Category\nQ:Quantity\n")


def API():
	print_bill()

def main():
	API()
if __name__=="__main__":
	main()
