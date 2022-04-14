import sys

def main():
    
    with open('file_input.txt') as file:
        for line in file:
            sys.stdout.write(line)
    
if __name__== "__main__":
  main()
  