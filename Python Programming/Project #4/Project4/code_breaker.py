import Pollard_Rho as pr
import Miller_Rabin as mr
import encrypt_decrypt as ec
import time

def retrieve_message(C, (e, n)):
    time_elapsed = time.time()
    p = pr.Pollard_Rho(n)
    q = n/p
    phi_n = (p - 1) * (q - 1)
    d = ec.Extended_Euclid(e, phi_n)[1] % phi_n  #find the multiplicative inverse of e modulo phi_n
    M = mr.modular_exponentiation(C, d, n)
    time_elapsed = time.time() - time_elapsed
    print "---------Breaking Encryption Code------------------"
    print "Encrypted messsage: ", C
    print "Found private key: ", (d, n)
    print "Found original message: ", M
    print "Time taken to decrypt the message using the public  key: ", time_elapsed

#retrieve_message(485671907631977606757, (5, 2752977223569345565189))