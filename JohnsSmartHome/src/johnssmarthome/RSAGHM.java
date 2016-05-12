/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johnssmarthome;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author henrisilver
 */
public class RSAGHM {
    private BigInteger e, d, n;
    
    private static final int NUMBITS = 1024;
    private static final int PRIME_PROBABILITY = 100;
    
    public RSAGHM() {
        SecureRandom randomGenerator = new SecureRandom();
        BigInteger p = new BigInteger(NUMBITS, PRIME_PROBABILITY,randomGenerator);
        BigInteger q = new BigInteger(NUMBITS, PRIME_PROBABILITY,randomGenerator);
        
        // n = p * q
        n = p.multiply(q);
        
        // z = (p-1) * (q-1)
        BigInteger z = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        // Setting e.
        getCoprime(n, z);
        
        // Setting d
        d = e.modInverse(z);
    }
    
    private void getCoprime(BigInteger n, BigInteger z) {
        e = n.subtract(BigInteger.ONE);
        
        // While we do not get an e such that it is coprime with z, and while
        // it is greater than one, keep searching for e.
        while(e.gcd(z).compareTo(BigInteger.ONE) != 0 && e.compareTo(BigInteger.ONE) == 1) {
            e.subtract(BigInteger.ONE);
        }
    }
    
    public String encrypt(String message) {
        return null;
    }
    
    public String decrypt(String ciphertext) {
        return null;
    }
    
}
