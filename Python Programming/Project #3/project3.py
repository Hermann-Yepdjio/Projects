import time
import matplotlib.pyplot as plt
from multiprocessing import Pool

def find_frequencies(args):
    file_name = args[0]
    char = args[1]
    tc_words= {}
    num_words = 0
    with open(file_name) as f:
        for line in f:
            words = line.split()
            for word in words:
                num_words += 1
                if word[0] == char:
                    if  word in tc_words:
                        tc_words[word] += 1
                    else:
                        tc_words[word] = 1
        
    return tc_words, num_words


def experimentation(file_name, num_cores):
    args = []
    for i in range (65, 91):
        #chars.append(chr(i))
        args.append([file_name, chr(i)])

    p = Pool(num_cores)
    t1 = time.time()
    result_1 = p.map(find_frequencies, args)
    t1 = time.time() - t1
    p.close()
    p.join()

    t2 = time.time()
    result_2 = []
    for arg in args:
        result_2 += find_frequencies(arg)
    t2 = time.time()-t2
    file_size = result_1[1][1]

    print "The program found ", file_size, "words in", file_name, ".\n\n" 

    print "Sequential took: ", t2, " seconds. \n\n"
    print "Multiprocessing took: ", t1, "seconds. \n\n"
    return t1, t2, file_size


def main():
    file_names = raw_input("\n\nPlease provide the file name : ")
    num_cores = int(input("\n\nPlease provide the number of cores you would like to use: "))
    file_sizes, multi_p_times, seq_times= [], [], []

    for file_name in file_names.split():
        t1, t2, file_size = experimentation(file_name, num_cores)
        multi_p_times.append(t1)
        seq_times.append(t2)
        file_sizes.append(file_size)
   
    plt.plot(file_sizes, multi_p_times, "k.")
    plt.plot(file_sizes, multi_p_times, "b-")
    plt.xlabel('file size')
    plt.ylabel('time taken')
    plt.title('Map Reduce Efficiency')
    plt.show()
    plt.close()

    plt.plot(file_sizes, seq_times, "k.") 
    plt.plot(file_sizes, seq_times, "b-")
    plt.xlabel('file size')
    plt.ylabel('time taken')
    plt.title('Sequential Search  Efficiency')
    plt.show()
    plt.close()

    


if __name__ == "__main__":
    main()
