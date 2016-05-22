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
    private int cesarCode;
    
    private static final int NUMBITS = 1024;
    private static final int PRIME_PROBABILITY = 100;
    
    /**
     * Construtor padrão do algoritmo RSA personalizado
     *
     * Inicializa o RSA e gera chaves aleatoriamente
     *         
     */
    public RSAGHM() {
        SecureRandom randomGenerator = new SecureRandom();
        cesarCode = randomGenerator.nextInt()%256;
        BigInteger p = new BigInteger(NUMBITS, PRIME_PROBABILITY,randomGenerator);
        BigInteger q = new BigInteger(NUMBITS, PRIME_PROBABILITY,randomGenerator);
        
        // n = p * q
        n = p.multiply(q);
        
        // z = (p-1) * (q-1)
        BigInteger z = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        // Define e.
        getCoprime(n, z);
        
        // Setting d
        d = e.modInverse(z);
    }
    
    /**
     * Define a chave privada
     *
     * @param  e chave privada
     *         
     */
    public void setPrivateKey(BigInteger e) {
        this.e = e;
    }

    /**
     * Define a chave publica
     *
     * @param  d chave publica
     *         
     */
    public void setPublicKey(BigInteger d) {
        this.d = d;
    }
    
    /**
     * Retorna a chave privada
     * 
     * @return chave privada
     *         
     */
    public BigInteger getPrivateKey() {
        return e;
    }

    /**
     * Retorna a chave publica
     * 
     * @return chave publica
     *         
     */
    public BigInteger getPublicKey() {
        return d;
    }

    /**
     * Retorna o código de Cesar
     * 
     * @return codigo de Cesar atual
     *         
     */
    public int getCesarCode() {
        return cesarCode;
    }

    /**
     * Define o codigo de Cesar
     *
     * @param  cesarCode codigo de Cesar
     *         
     */
    public void setCesarCode(int cesarCode) {
        this.cesarCode = cesarCode;
    }
    
    
     /**
     * Faz com que e seja primo entre si com z
     *
     * @param  n valor inicial para e.
     * @param  z valor que quero ter como primo entre si
     *         
     */
    private void getCoprime(BigInteger n, BigInteger z) {
        e = n.subtract(BigInteger.ONE);
        
        // Enquanto não encontrar um e que é primo entre si com z, e enquando
        // ele for maior que 1, continua buscando por e
        while(e.gcd(z).compareTo(BigInteger.ONE) != 0 && e.compareTo(BigInteger.ONE) == 1) {
            e = e.subtract(BigInteger.ONE);
        }
    }
    
    /**
     * Criptografa uma mensagem
     *
     * @param  message texto plano
     * 
     * @return messagem criptografada
     *         
     */
    public BigInteger encrypt(String message) {
        // Transforma a mensagem em um array de bytes
        byte messageBytes[] = message.getBytes();
        // Aplica o código de Cesar
        for (int i=0;i<messageBytes.length;i++) {
            messageBytes[i] = (byte)((messageBytes[i]+cesarCode)%256);
        }
        // Aplica a função matemática de criptografia
        BigInteger messageEncrypted = new BigInteger(messageBytes).modPow(e, n);
        // Retorna a mensagem criptografada
        return messageEncrypted;
    }
    /**
     * Decriptografa uma mensagem
     *
     * @param  ciphertext texto cifrado
     * 
     * @return mensagem em texto plano
     *         
     */
    public String decrypt(BigInteger ciphertext) {
        // Aplica a função matemática de decriptografia, e converte o resultado 
        // para um array de bytes.
        byte[] messageBytes = ciphertext.modPow(d, n).toByteArray();
        // Aplica o código de Cesar
        for (int i=0;i<messageBytes.length;i++) {
            messageBytes[i] = (byte)((messageBytes[i]+256-cesarCode)%256);
        }
        // Retorna o resultado plano como String
        return new String(messageBytes, StandardCharsets.UTF_8);
    }
    
}
