import numpy as np
import matplotlib.pyplot as plt

class square(object): #to instantiate square objects
    def __init__(self, size):
        self.size = size
        self.value = np.random.choice(range(1, self.size**2 + 1), (self.size, self.size), replace = False)

class genetic_algorithm(object):

    def get_information(self): #get info from user
        square_size = int(input("Please enter the size of the magic square : "))
        population_size = int(input("Please enter the size of the initial population :"))
        mutation_probability = float(input("Please enter a number for the probability of a mutation to happend after a crossover : "))
        return square_size, population_size, mutation_probability

    def init_population(self, square_size, pop_size): #generate the initial population
        pop =  np.empty(shape = [pop_size, square_size, square_size], dtype = int) #create an empty np array of size pop_size and items having the shape square_size * square_size
        for i in range (pop_size):
            sq =  square(square_size).value
            while ( (pop == sq).all(1).any()): #check if the sq is already in the population to avoid duplicates
                sq = square(square_size).value
            pop[i] = square(square_size).value   
        return pop

    def square_fitness(self, square_value, square_size):
        magic_number = (square_size + square_size**3)/2
        count1, count2, count3 = 0, 0, 0
        for row in square_value: #get fitness for all rows
            for item in row:
                count1 += item
            count2 += np.abs(magic_number - count1)
            count1 = 0

        for row in np.transpose(square_value): #get fitness for all columns
            for item in row:
                count1 += item
            count2 += np.abs(magic_number - count1)
            count1 = 0

        for i in range (square_size): #get fitness for both diagonals
            count1 += square_value[i][i] #sums up the numbers in the 1st diagonal
            count3 += square_value[i][square_size - i - 1] #sums up the numbers in the second diagonal
        count2 += np.abs(magic_number - count1) + np.abs(magic_number - count3)
        count1, count3 = 0, 0  #reset counter 1 and counter 3

        return count2


    def pop_fitness(self, pop): #compute fitnesses for each square in the population and return an array of fitnesses
        fitnesses = [0]*len(pop)
        count = 0
        for item in pop:
            fitnesses[count] = self.square_fitness(item, len(item))
            count += 1
        return np.array(fitnesses)

    def roulette_wheel(self, pop):
        fitness = self.pop_fitness(pop)  
        sum_fitness = sum(fitness)
        fitness_percentages = (1.0 / (fitness + 1))/sum(1.0 / (fitness + 1)) #fitness + 1 to avoid division by 0 in case of perfect fitness, 1/.. for smaller values to have higher percentages
        mating_pool_size = len(pop)//2
        if mating_pool_size % 2 != 0:
            mating_pool_size += 1 #to have a mating pool with even number of chromosomes to easily pair them for crossover
        mating_pool = np.random.choice(range(0, len(pop)), mating_pool_size, False, fitness_percentages) #randomly select parents for crossover based on  probabilities obtained from fitnesses
        return mating_pool #return indexes of crossover participants

    def crossover(self, chrom1, chrom2): #follows a specific procedure to avoid repetition of numbers in the offsprings (children)
        #first we start by generation a reversible inversion sequence for each of the parents
        chrom_1, chrom_2 = chrom1.flatten(), chrom2.flatten()  #flatten the matrix into 1D arrays for easy manipulation
        inversion_seq1 = [0]*len(chrom_1)  
        inversion_seq2 =[0]*len(chrom_2)
        
        #for each value in the chromosome count how many values on the left are bigger than value and place that count at index value-1 in the inversion_seq
        for i in range(len(chrom_1)):
            if (i == 0):
                inversion_seq1[chrom_1[i]-1] = 0 #first elt in chromosomes have no bigger elts to their left
                inversion_seq2[chrom_2[i]-1] = 0
            else:
                count1, count2 = 0, 0
                for j in range(i):
                    if (chrom_1[j] > chrom_1[i]):
                        count1 += 1
                    if (chrom_2[j] > chrom_2[i]):
                        count2 += 1
                inversion_seq1[chrom_1[i]-1] = count1 #insert count at the appropriate position in the inversion array
                inversion_seq2[chrom_2[i]-1] = count2
                count1, count2 = 0, 0 #reset the counters
         
        #generating the children arrays by one-point crossover method (take 1st half of chrom_1 and 2nd half of chrom_2 to make child_1 and the others halves for child_2
        child_1, child_2 = [0]*len(chrom_1), [0]*len(chrom_2)

        for i in range(len(chrom_1) - len(chrom_1)//4): #copy first 3 quarter of inv 1 and inv 2 into child_1 and child_2 respectively
            child_1[i] = inversion_seq1[i]
            child_2[i] = inversion_seq2[i]

        for i in range (len(chrom_1) - len(chrom_1)//4 + 1, len(chrom_1)): #copy last quarter of inv 1 and inv2 into child_2 and child_1 respectively
            child_1[i] = inversion_seq2[i]
            child_2[i] = inversion_seq1[i]

        #transforming back to permutation representation
        pos1 = [0] * len(chrom_1)
        pos2 = [0] * len(chrom_2)

        #starting at index n - 1, copy child[index] to pos [index]. then increment any value to the right of pos[index] which is >= than pos[index]
        pos1[len(chrom_1)-1] = child_1[len(chrom_1) - 1] # include last elt cuz we know that there is nothing to the right of last element
        pos2[len(chrom_2)-1] = child_2[len(chrom_2) - 1]
        for i in range (len(chrom_1) - 2, -1, -1):
            pos1[i] = child_1[i]
            pos2[i] = child_2[i]
            for j in range (i + 1, len(chrom_1)):
                if pos1[j] >= pos1[i] :
                    pos1[j] += 1
                if pos2[j] >= pos2[i] :
                    pos2[j] += 1
        
        #now reconstruct the final squres using pos1 and pos2 following the rule (if pos[i] = j, then square[j] = i+1
        square1, square2 = [0] * len(chrom_1), [0] * len(chrom_1)
        for i in range(len(chrom_1)):
            square1[pos1[i]] = i + 1
            square2[pos2[i]] = i + 1
        
        child_1, child_2 = np.array(square1).reshape(len(chrom1), len(chrom1)), np.array(square2).reshape(len(chrom2), len(chrom2)) #reshape from arrays to square matrices
        print "---------Crossover Applied-----------"
        
        return child_1, child_2

    def mutation(self, chrom):
        tmp_chrom = chrom.flatten() #flatten matrix to an array temporarily for easy manipulation
        indexes = np.random.choice(range(0, len(tmp_chrom)), 2)
        tmp = tmp_chrom[indexes[0]]
        tmp_chrom[indexes[0]] = tmp_chrom[indexes[1]] 
        tmp_chrom[indexes[1]] = tmp
        print "----------Mutation Applied-----------"
        return tmp_chrom.reshape(len(chrom), len(chrom))    #reshape tmp_chrom from array to matrix  and return its value

    def is_simulation_over(self, pop_fitness): #checks if the simulation is over or not
        if np.amin(pop_fitness) == 0:
            return True
        else:
            return False
    
    def simulation(self, pop, square_size, mut_prob): #runs the simulation
        pop_fitness = self.pop_fitness(pop)
        num_generation = 0
        x_1, y_1, y_2 = [], [], []  #used for plotting number generations Vs average fitness and number of generations vs fittes chromosome
        x_1.append(num_generation)
        y_1.append(np.amin(pop_fitness))
        y_2.append(np.average(pop_fitness))

        while(not self.is_simulation_over(pop_fitness)): #loops until a magic square is found
            print "\n---------Generation #:", num_generation, "-----------\n" 
            print "Average Fitness : ", np.average(pop_fitness), "\n"
            print "Fittess Chromosome: \n"
            print pop[np.argmin(pop_fitness)]
            print "\n Fitness: ", np.amin(pop_fitness), "\n"
            mating_pool = self.roulette_wheel(pop) #perform selection to build a mating pool
            for i in range (0, len(mating_pool), 2): 
                child_1, child_2 = self.crossover(pop[mating_pool[i]], pop[mating_pool[i + 1]]) #perform cross over between two parents
                mutate = np.random.choice([True, False], 2, True, [mut_prob, 1-mut_prob]) #randomly select if mutation should occur or not
                if mutate[0] and self.square_fitness(child_1, len(child_1)) != 0 : #mutate if yes
                    child_1 = self.mutation(child_1)
                if mutate[1] and self.square_fitness(child_2, len(child_2)) != 0: #mutate if yes
                    child_2 = self.mutation(child_2)
                lowest_fitnesses = pop_fitness.argsort()[-2:][::-1]
                #if ( not (pop == child_1).all(1).any()): #check if an instance of child_1 is already in the population
                pop[lowest_fitnesses[0]] = child_1 #replace parent by the child in the original population 
                #if ( not(pop == child_2).all(1).any()): #check if an instance of child_2 is already in the population 
                pop[lowest_fitnesses[1]] = child_2 #replace parent by the child in the original population 

           # pop[5] = np.array([[2, 16, 13, 3], [11, 5, 8, 10], [7, 9, 12, 6], [14, 4, 1, 15]])
          
            num_generation += 1 #increment the generation number
            pop_fitness = self.pop_fitness(pop)
            x_1.append(num_generation)
            y_1.append(np.amin(pop_fitness))
            y_2.append(np.average(pop_fitness))

            if ( not self.is_simulation_over(pop_fitness) and num_generation > 3000):
                plt.plot(x_1, y_1, "k.")
                plt.plot(x_1, y_1, "b-")
                plt.xlabel('Generation Number')
                plt.ylabel('Fittest Chromosome')
                plt.title('Evolution of the Fittest Chromosome Over Generations')
                plt.show()
                plt.close()

                plt.plot(x_1, y_2, "k.")
                plt.plot(x_1, y_2, "b-")
                plt.xlabel('Generation Number')
                plt.ylabel('Average Fitness')
                plt.title('Evolution of the Average Fitness Over Generation')
                plt.show()
                plt.close()

                num_generation = 0
                x_1, y_1, y_2 = [], [], [] 
                pop = self.init_population(square_size, len(pop))
                print "------------Starting Over With a Completely New Population-------------------"
            


        print "\n---------Simulation Completed------------\n"
        print "\n---------Generation #:", num_generation, "-----------\n" 
        print "A Magic Square is : \n"
        print pop[np.argmin(pop_fitness)]
        print "\n Fitness: ", np.amin(pop_fitness), "\n"
        plt.plot(x_1, y_1, "k.")
        plt.plot(x_1, y_1, "b-")
        plt.xlabel('Generation Number')
        plt.ylabel('Fittest Chromosome')
        plt.title('Evolution of the Fittest Chromosome Over Generations')
        plt.show()
        plt.close()

        plt.plot(x_1, y_2, "k.")
        plt.plot(x_1, y_2, "b-")
        plt.xlabel('Generation Number')
        plt.ylabel('Average Fitness')
        plt.title('Evolution of the Average Fitness Over Generation')
        plt.show()
        plt.close()



    def API(self):
        square_size, pop_size, mut_prob = self.get_information()
        pop = self.init_population(square_size, pop_size)
        self.simulation(pop, square_size, mut_prob)




def main():
    np.random.seed(0)
    gen = genetic_algorithm()
    gen.API()
if __name__ == "__main__":
    main()
