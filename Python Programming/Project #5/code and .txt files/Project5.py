import time
from matplotlib import pyplot as plt
import ahocorasick as aho

#find only the last occurence of the keywords in the input text file
def one_by_one_matching(file_name, key_words):
    with open(file_name, 'r') as file:
        data = file.read().replace('\n', '')
    for key_word in key_words:
        index = data.rfind(key_word)
        if index != -1:
            print ("The word'" + key_word + "'" + ' was found at index ' + str(index) + '.')


##Another implementation but it is slower than the one implemented in the pyahocorasick package
# class AhoNode:
#     def __init__(self):
#         self.goto = {}
#         self.out = []
#         self.fail = None
#
#
# def aho_create_forest(patterns):
#     root = AhoNode()
#
#     for path in patterns:
#         node = root
#         for symbol in path:
#             node = node.goto.setdefault(symbol, AhoNode())
#         node.out.append(path)
#     return root
#
#
# def aho_create_statemachine(patterns):
#     root = aho_create_forest(patterns)
#     queue = []
#     for node in root.goto.itervalues():
#         queue.append(node)
#         node.fail = root
#
#     while len(queue) > 0:
#         rnode = queue.pop(0)
#
#         for key, unode in rnode.goto.iteritems():
#             queue.append(unode)
#             fnode = rnode.fail
#             while fnode != None and not fnode.goto.has_key(key):
#                 fnode = fnode.fail
#             unode.fail = fnode.goto[key] if fnode else root
#             unode.out += unode.fail.out
#
#     return root
#
#
# def aho_find_all(s, root, callback):
#     node = root
#
#     for i in xrange(len(s)):
#         while node != None and not node.goto.has_key(s[i]):
#             node = node.fail
#         if node == None:
#             node = root
#             continue
#         node = node.goto[s[i]]
#         for pattern in node.out:
#             callback(i - len(pattern) + 1, pattern)
#
#
#
# ############################
# # Demonstration of work
# def on_occurence(pos, patterns):
#     print "At pos %s found pattern: %s" % (pos, patterns)
#
#
#
# def Aho_Corasick(file_name, patterns):
#     with open(file_name, 'r') as file:
#         data = file.read().replace('\n', '')
#     root = aho_create_statemachine(patterns)
#     aho_find_all(data, root, on_occurence)

def Aho_Corasick(file_name, key_words):
    with open(file_name, 'r') as file:
        data = file.read().replace('\n', '')
    auto = aho.Automaton()
    index = 0
    for index, key in enumerate(data.split()):
        auto.add_word(key, (index, key))
        index += 1
    for key_word in key_words:
        try:
            auto.get(key_word)
        except KeyError:
            print("Oops! This pattern does not exits.")

def get_file_sizes(file_names):
    file_sizes = []
    for file_name in file_names:
        file = open(file_name)
        count = 0
        for line in file:
            count += len(line) - 1
        file_sizes.append(count)
    print ("sizes", file_sizes)
    return file_sizes

def API():
    file_names = ["test.txt", "test2.txt", "test3.txt", "test4.txt", "test5.txt", "test6.txt","test7.txt", "test8.txt", "test9.txt", "test10.txt"]
    file_sizes = get_file_sizes(file_names)
    one_by_one_times = []
    Aho_Corasick_times = []
    key_words_list = ["keywords.txt", "keywords_1.txt", "keywords_2.txt", "keywords_3.txt", "keywords_4.txt", "keywords_5.txt", "keywords_6.txt"]
    for f_name in key_words_list:
        with open(f_name, 'r') as file:
            key_words = (file.read().replace('\n', '')).split()
        for file_name in file_names:
            print("\n\n-----------------------------One by one matching starting---------------------------------\n")
            t1 = time.time()
            one_by_one_matching(file_name, key_words)
            t2 = time.time() - t1
            one_by_one_times.append(t2)
            print("\n Time Elapsed: " + str(t2))
            print("\n\n-----------------------------One by one matching ending-----------------------------------\n")
            print("\n\n---------------------------Aho-Corasick matching starting---------------------------------\n")
            t1 = time.time()
            Aho_Corasick(file_name, key_words)
            t2 = time.time() - t1
            Aho_Corasick_times.append(t2)
            print("\n Time Elapsed: " + str(t2))
            print("\n\n------------------------------Aho-Corasick matching ending--------------------------------\n")

        plt.plot(file_sizes, Aho_Corasick_times, "k.")
        plt.plot(file_sizes, Aho_Corasick_times, "g-")
        plt.plot(file_sizes, one_by_one_times, "b.")
        plt.plot(file_sizes, one_by_one_times, "r-")
        plt.xlabel('Length of File (Number of Characters)')
        plt.ylabel('Time Taken (in seconds)')
        plt.title('Efficiency: Aho-Corasick Matching Vs One-by-One Matching. \nNumber of patterns to match: ' + str(len(key_words)) )
        plt.show()
        plt.close()

        one_by_one_times = []
        Aho_Corasick_times = []
def main():
    API()


if __name__ == "__main__":
    main()