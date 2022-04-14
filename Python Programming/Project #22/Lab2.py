#team members: Hermann Yepdjio and Khamsavath Joe Muongmany
def main():
	while(1):
		num_squirrels = int(input ("How many squirrels are in the backyard?"))
		num_nuts = int( input ("How many nuts are in the backyard?"))
		if (num_nuts/num_squirrels <10):
			print("don't get squirrel, go and buy nuts!")
		else:
			print("All squirels are happy. Have a nice day!")
			break

if __name__=="__main__":
	main()
