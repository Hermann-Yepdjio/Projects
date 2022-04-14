#team members: Hermann Yepdjio and Khamsavath Joe Muongmany
class CandidateData (object):
    name = ""
    age =0
    natural_born_citizenship= "No"
    residency_time = 0
    
    def __init__(self, name, age, nat_born_cit, resid_time):
       self.name = name
       self.age = age
       self.natural_born_citizenship= nat_born_cit
       self.residency_time=resid_time
       
class Elections (object):
    num_candidate = 0
    
    def __init__(self, num_candidate):
       self.num_candidate = num_candidate

    def electable(self, candidates):
       for i in range (self.num_candidate):
            if (candidates[i].age < 35 or candidates[i].residency_time < 14 or candidates[i].natural_born_citizenship == "No" or candidates[i].natural_born_citizenship == "no" ):
                print ("The candidate ", candidates[i].name, " can't run for president.")
            elif (candidates[i].natural_born_citizenship != "yes" and candidates[i].natural_born_citizenship != "Yes"):
                print ("Sorry wrong information was provided for ", candidates[i].name, " about his/her natural born citizenship.")
            else:
                print ("The candidate ", candidates[i].name, " can run for president.")
    def inputCandidate(self, num_candidate):
       candidates = []
       for i in range (num_candidate):
           name = input("Enter the name of the candidate: ")
           age = int(input("Enter the age of the candidate in years: "))
           N_B_C = input("Is the candidate a natural born citizenship? (yes/no): ")
           Res_time = int(input("Enter the residency time of the candidate in years: "))
           candidates.append(CandidateData(name, age, N_B_C, Res_time))
       return candidates
    
    def API(self):
       candidates = self.inputCandidate(self.num_candidate)
       self.electable(candidates)
       
def main():
    num_candidate = int(input("How many candidates run for president?"))
    election = Elections(num_candidate)
    election.API();

if __name__ == "__main__":
    main()
