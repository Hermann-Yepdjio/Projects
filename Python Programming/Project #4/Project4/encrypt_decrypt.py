import Miller_Rabin as mr
#import code_breaker as cb
import random, numpy as np
import time

def Extended_Euclid(a, b):
    if b == 0:
        return (a, 1, 0)
    else:
        (d_1, x_1, y_1) = Extended_Euclid(b, a % b)
        (d, x, y) = (d_1, y_1, x_1 - (a//b) * y_1)
        return (d, x, y)



def get_keys(prime_size):
    p, q = random.getrandbits(prime_size), random.getrandbits(prime_size)  # generate random number of 1024 bits
    while not(mr.Miller_Rabin(p, 4) and mr.Miller_Rabin(q, 4) and p != q):
        p, q = random.getrandbits(prime_size), random.getrandbits(prime_size)  # generate random number of 1024 bits

        mr.Miller_Rabin(p, 4)
        mr.Miller_Rabin(q, 4)
    print  mr.Miller_Rabin(p, 4)
    print  mr.Miller_Rabin(q, 4)

    n = p * q
    phi_n = (p - 1) * (q - 1)
    e = 3
    while( np.gcd(e, phi_n) != 1 ): #find smallest odd number e that is  relatively prime to phi_n
        e += 2
    d = Extended_Euclid(e, phi_n)[1] % phi_n  #find the multiplicative inverse of e modulo phi_n
    return ((e, n) , (d, n))

def encrypt(M, prime_size):
    public_key, private_key = get_keys(prime_size)
    print "Public Key: ", public_key
    print "Private Key: ", private_key
    e, n = public_key[0], public_key[1]
    enc_msg = mr.modular_exponentiation(M, e, n)
    return enc_msg, private_key, public_key

def decrypt(C, private_key):
    d, n = private_key[0], private_key[1]
    dec_msg = mr.modular_exponentiation(C, d, n)
    return dec_msg


def main():
    import code_breaker as cb
    random.seed(0)
    M = int(input("Please enter the message to be encrypted: "))
    for prime_size in range (30, 60, 5):
        print "-----------------------------sizeof(primes) = ", prime_size, "-----------------------------"
        time_elapsed_1 = time.time()
        enc_msg, private_key, public_key = encrypt(M, prime_size)
        time_elapsed_1 = time.time() - time_elapsed_1
        time_elapsed_2 = time.time()
        dec_msg = decrypt(enc_msg, private_key)
        time_elapsed_2 = time.time() - time_elapsed_2
        print "\nThe original message is : ", M, "\n"
        print "The encrypted message is : ", enc_msg, ""
        print "Time taken to encrypt the message: ", time_elapsed_1, "\n"
        print "The decrypted message is : ", dec_msg, ""
        print "Time taken to decrypt the message using the private key: ", time_elapsed_2, "\n"
        cb.retrieve_message(enc_msg, public_key)



if __name__ == "__main__":
    main()


