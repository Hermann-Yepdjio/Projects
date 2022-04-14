import random
from math import *
from time import sleep

class station:


    def __init__(self, station_id, pump_in_switch, pump_out_switch, tank_level, pump_in, pump_out, position_from_center, fitness):
        self.id = station_id
        self.pump_in_switch = pump_in_switch
        self.pump_out_switch = pump_out_switch
        self.tank_level = tank_level
        self.pump_in = pump_in
        self.pump_out = pump_out
        self.position_from_center = position_from_center
        self.fitness = fitness


class genetic:
    num_stations = 16
    stations = []
    positions = ["20L", "20R", "40L", "40R", "60L", "60R", "80L", "80R", "100L", "100R", "120L", "120R", "140L", "140R", "160L", "160R"]
    tank_capacity = 100
    init_tank_level = 40
    init_tank_level_deviation = 15
    init_pump_in = 10
    init_pump_in_deviation = 5
    total_pump_out = 200
    init_pump_out = 12
    init_pump_out_deviation = 2
    appropriate_tank_level = 45
    appropriate_total_pump_out = 160
    def __init__(self):
        pass
    

    def create_population(self): #create a population of 16 stations
        for i in range(self.num_stations):
            self.stations.append(station(i+1, 0, 0, 0, 0, 0, self.positions[i], 0))


    def init_population(self): #initialize the population
        self.create_population()
        for station in self.stations:
            station.tank_level = random.randint(self.init_tank_level - self.init_tank_level_deviation, self.init_tank_level + self.init_tank_level_deviation)/1.0
            station.pump_in = random.randint(self.init_pump_in - self.init_pump_in_deviation, self.init_pump_in + self.init_pump_in_deviation )/1.0
            station.pump_out = random.randint(self.init_pump_out - self.init_pump_out_deviation, self.init_pump_out + self.init_pump_out_deviation)/1.0
            if (station.tank_level - station.pump_out + station.pump_in <= 100):
                station.pump_in_switch = 1
            if (station.tank_level - station.pump_out + station.pump_in >= 0):
                station.pump_out_switch = 1
        self.fitness()
        return self.stations
    

    def roulette_wheel(self): #based on fitnesses, randomly choose a chromosome from the population for mutation or cross-over
                              #chromosomes with higher fitness have greater chances to be selected
                              #return a station's id
        selection_array = []
        for station in self.stations:
            for i in range (0, int(ceil(station.fitness * 10))):
                selection_array.append(station.id)
        return selection_array[random.randint(0, len(selection_array) - 1)]
        
    
    
    def print_population(self): #print the population with attributes in a nice format
        print ("Station # || pump_in switch || pump_out switch || tank_level || pump_in || pump_out  || Position_from_center || fitness")
        print ('='*118)
        self.fitness()
        for station in self.stations:
            print ('    ' + str(station.id) + ' '*(6-len(str(station.id))) + '||        ' + 
                         str(station.pump_in_switch) + ' '*(8-len(str(station.pump_in_switch)))  + '||        ' +
                         str(station.pump_out_switch) + ' '*(9-len(str(station.pump_out_switch))) + '||    ' + 
                         '%.2f' % station.tank_level + ' '*(8-len('%.2f' % station.tank_level)) + '||  ' + 
                         '%.2f' % station.pump_in + ' '*(7 - len('%.2f' % station.pump_in)) + '||   ' + 
                         '%.2f' % station.pump_out + ' '*(8 - len('%.2f' % station.pump_out)) + '||         ' +
                         str(station.position_from_center) + ' '*(12 - len(str(station.position_from_center))) + ' || ' + 
                         '%.3f' % station.fitness + ' '*(7-len('%.3f' % station.fitness)))
        print ("Plant fitness: " + '%.3f' % self.fitness())
        print ("Plant total dispatch: " + str(self.plant_pump_out()) + " gallons")

   
    

    
    def fitness(self): #compute the fitness for each station based on their tank levels and save the values in the station object themselves
                       #also compute and return the overall fitness of the plant base on its overall pump_out volume
        for station in self.stations:
            if (station.tank_level <= self.appropriate_tank_level):
                station.fitness = station.tank_level/float(self.appropriate_tank_level)
            else:
                station.fitness = (2*self.appropriate_tank_level - station.tank_level)/float(self.appropriate_tank_level)
        total_pump_out = self.plant_pump_out()
        if (total_pump_out <= self.appropriate_total_pump_out):
            return total_pump_out/float(self.appropriate_total_pump_out)
        else:
            return (2*self.appropriate_total_pump_out - total_pump_out)/float(self.appropriate_total_pump_out)


    def plant_pump_out(self): #compute and return the total amount of oil dispatched by the plant
        total_pump_out = 0
        for station in self.stations:
            if (station.pump_out_switch == 1):
                total_pump_out += station.pump_out
        return total_pump_out


    def simulation(self): #simulates the functioning of the plant during a 48 hours time frame
        self.print_population()
        for i in range (0, 48):
            for station in self.stations:
                if (station.pump_in_switch == 1 and station.pump_out_switch == 1):
                    station.tank_level = station.tank_level + station.pump_in - station.pump_out
                    if (station.tank_level + station.pump_in - station.pump_out > 100):
                        station.pump_in_switch = 0
                    elif (station.tank_level + station.pump_in - station.pump_out < 0):
                        station.pump_out_switch = 0
                elif (station.pump_in_switch == 1 and station.pump_out_switch == 0):
                    station.tank_level += station.pump_in
                    if (station.tank_level + station.pump_in - station.pump_out > 0):
                        station.pump_out_switch = 1
                elif (station.pump_in_switch == 0 and station.pump_out_switch == 1):
                    station.tank_level -= station.pump_out
                    if (station.tank_level + station.pump_in - station.pump_out < 100):
                        station.pump_in_switch = 1
            print ("\nHour: ",i+1)
            self.print_population()
            #print ("Plant fitness: " + '%.3f' % self.fitness())
            #print ("Plant total dispatch: " + str(self.plant_pump_out()) + " gallons")
            #sleep(0.0005)


       
        


    def mutation(self): #performs the mutation
        new_station = station(0, 0, 0, 0, 0, 0, '', 0)
        new_station.id = self.roulette_wheel()
        new_station.position_from_center = self.stations[new_station.id - 1].position_from_center
        new_station.tank_level = random.randint(self.init_tank_level - self.init_tank_level_deviation, self.init_tank_level + self.init_tank_level_deviation)/1.0
        new_station.pump_in = random.randint(self.init_pump_in - self.init_pump_in_deviation, self.init_pump_in + self.init_pump_in_deviation )/1.0
        new_station.pump_out = random.randint(self.init_pump_out - self.init_pump_out_deviation, self.init_pump_out + self.init_pump_out_deviation)/1.0
        if (new_station.tank_level - new_station.pump_out + new_station.pump_in <= 100):
            new_station.pump_in_switch = 1
        if (new_station.tank_level - new_station.pump_out + new_station.pump_in >= 0):
            new_station.pump_out_switch = 1
        self.stations[new_station.id - 1] = new_station
        self.fitness() #compute the fitnesses again since new genes were introduced in the population
        print("\nMutation was just applied on station #" + str(new_station.id) + "\n")
        self.print_population()
        



    def cross_over(self): #performs a cross-over between two randomly chosen parents to bring new individuals in the population and eliminate old ones 
        id_parent1 = 0
        id_parent2 = 0
        while (id_parent1 == id_parent2):
            id_parent1 = self.roulette_wheel()
            id_parent2 = self.roulette_wheel()
        child_tank_level = (self.stations[id_parent1-1].tank_level + self.stations[id_parent2 -1].tank_level)/2.0
        child_pump_in = (self.stations[id_parent1-1].pump_in + self.stations[id_parent2 -1].pump_in)/2.0
        child_pump_out = (self.stations[id_parent1-1].pump_out + self.stations[id_parent2 -1].pump_out)/2.0
        child_pump_in_switch = 0
        child_pump_out_switch = 0
        if (child_tank_level + child_pump_in - child_pump_out < 100):
            child_pump_in_switch = 1
        if (child_tank_level + child_pump_in - child_pump_out > 0):
            child_pump_out_switch = 1

        id_station_with_lowest_fitness = 1
        for station1 in self.stations:
            if (station1.fitness < self.stations[id_station_with_lowest_fitness - 1].fitness):
                id_station_with_lowest_fitness = station1.id
        child = station(id_station_with_lowest_fitness, child_pump_in_switch, child_pump_out_switch, child_tank_level, child_pump_in,
                child_pump_out, self.stations[id_station_with_lowest_fitness -1].position_from_center, 0.0)
        print("\n Cross-over applied between station #" + str(id_parent1) + " and station #" + str(id_parent2))
        print("The new child will replace station #" + str(id_station_with_lowest_fitness) + " which has the lowest fitness in the whole plant")
        self.stations[id_station_with_lowest_fitness - 1] = child
        self.fitness() #to compute the new fitnesses since the population has been modified 
        self.print_population()

    def avg_stations_fitness(self):
        sum_stations_fitnesses = 0
        for station in self.stations:
            sum_stations_fitnesses += station.fitness
        return sum_stations_fitnesses/self.num_stations
    def API(self): #calls all the important functions
        self.init_population()
        while(self.fitness()<0.90 or self.avg_stations_fitness()<0.95):
            #self.print_population()
            #print ("Plant fitness: " + '%.3f' % self.fitness())
            #print ("Plant total dispatch: " + str(self.plant_pump_out()) + " gallons")
            self.cross_over()
            plant_fitness = self.fitness()
            self.simulation()
            if (plant_fitness == self.fitness()):
                self.mutation()
    

def main(): #main function
    obj = genetic()
    obj.API()


if __name__=="__main__":
    main()
