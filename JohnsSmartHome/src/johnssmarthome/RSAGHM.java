/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johnssmarthome;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
    
    public void setPrivateKey(BigInteger e) {
        this.e = e;
    }

    public void setPublicKey(BigInteger d) {
        this.d = d;
    }
    
    private void getCoprime(BigInteger n, BigInteger z) {
        e = n.subtract(BigInteger.ONE);
        
        // While we do not get an e such that it is coprime with z, and while
        // it is greater than one, keep searching for e.
        while(e.gcd(z).compareTo(BigInteger.ONE) != 0 && e.compareTo(BigInteger.ONE) == 1) {
            e.subtract(BigInteger.ONE);
        }
    }
    
    public BigInteger encrypt(String message) {
        byte messageBytes[] = message.getBytes();
        BigInteger messageEncrypted = new BigInteger(messageBytes).modPow(e, n);
        return messageEncrypted;
        //return (new String(messageEncrypted.toByteArray(), StandardCharsets.UTF_8));
        //System.out.println(new String(messageBigIntegers.toByteArray(),StandardCharsets.UTF_8));
    }
    
    public String decrypt(BigInteger ciphertext) {
        //byte messageBytes[] = ciphertext.getBytes();
        //BigInteger messagePlain = new BigInteger(messageBytes).modPow(d, n);
        //return (new String(messagePlain.toByteArray(), StandardCharsets.UTF_8));
        return new String(ciphertext.modPow(d, n).toByteArray(), StandardCharsets.UTF_8);
    }
    
}
