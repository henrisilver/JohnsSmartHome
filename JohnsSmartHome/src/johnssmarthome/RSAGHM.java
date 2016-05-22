/*
 * John's Smart Home
 * Autores:
 * Giuliano Barbosa Prado
 * Henrique de Almeida Machado da Silveira
 * Marcello de Paula Ferreira Costa
 */
package johnssmarthome;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

// Classe que representa nosso algoritmo de criptografia.
// Implementamos um algoritmo baseado em RSA e na Cifra de César.
// O RSA garante a maior parte da segurança do sistema, permitindo também
// facilidade na troca de chaves. A Cifra de César fornece uma segurança
// adicional, empacotando o resultado gerado pela aplicação do RSA à mensagem.
public class RSAGHM {
    
    // Chaves utilizadas:
    // e, n: chave publica assimetrica (RSA)
    // e, d: chave privada assimetrica (RSA)
    // cesarCode: chave privada simétrica (Cifra de Cesar)
    private BigInteger e, d, n;
    private int cesarCode;
    
    // Estamos configurando, aqui, o funcionamento do RSA.
    // O numero de bits da chave sera 1024 (RSA-1024)
    // Alem disso, no processo de criação das chaves, especificamos 100%
    // de chance de utilizar números primos.
    private static final int NUMBITS = 1024;
    private static final int PRIME_PROBABILITY = 100;
    
    /**
     * Construtor padrão do algoritmo RSA personalizado
     *
     * Inicializa o RSA e gera chaves aleatoriamente
     *         
     */
    public RSAGHM() {
        
        // Gerador de números aleatórios
        SecureRandom randomGenerator = new SecureRandom();
        
        // Gera um código para a Cifra de Cesar aleatoriamente, estando
        // entre -128 e 127 (gama de valores que o tipo byte, em Java, pode
        // utilizar). Assim, há 256 possibilidades para a variavel cesarCode.
        do {
            cesarCode = randomGenerator.nextInt();
        } while(cesarCode < -128 || cesarCode > 127);
        
        // A seguir, inicia-se a definição das chaves do RSA, caso não sejam
        // fornecidas pelo usuário. Os valores p e q são escolhidos aleatoriamente,
        // sendo números primos de 1024 bits.
        BigInteger p = new BigInteger(NUMBITS, PRIME_PROBABILITY,randomGenerator);
        BigInteger q = new BigInteger(NUMBITS, PRIME_PROBABILITY,randomGenerator);
        
        // O numero n é definido como p * q. 
        // n = p * q
        n = p.multiply(q);
        
        // O numero z é definido como (p-1) vezes (q-1)
        // z = (p-1) * (q-1)
        BigInteger z = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        
        // O numero e é calculado, sendo ele primo com relação a z (além disso,
        // e deve ser menor que n).
        // Define e.
        getCoprime(n, z);
        
        // d é definido como o inverso modular de (e,z)
        d = e.modInverse(z);
    }
    
    /**
     * Construtor alternativo, em que o usuário define os valores para as
     * chaves RSA e da Cifra de César
     * @param e Parte da chave pública RSA
     * @param d Parte da chave privada RSA
     * @param n Componente das chaves pública e privada RSA
     * @param cesarCode Código de César
     */
    public RSAGHM(BigInteger e, BigInteger d, BigInteger n, int cesarCode) {
        setE(e);
        setD(d);
        setN(n);
        setCesarCode(cesarCode);     
    }
    
    /**
     * Define o valor de d, parte da chave pública RSA
     *
     * @param e Parte da chave pública RSA
     *         
     */
    public void setE(BigInteger e) {
        this.e = e;
    }

    /**
     * Define o valor de d, parte da chave privada RSA
     *
     * @param d Parte da chave privada RSA
     *         
     */
    public void setD(BigInteger d) {
        this.d = d;
    }
    
    /**
     * Define o valor de n, componente das chaves pública e privada RSA
     *
     * @param n Componente das chaves pública e privada RSA
     *         
     */
    public void setN(BigInteger n) {
        this.n = n;
    }
    
    /**
     * Retorna o valor de e, parte da chave pública RSA
     * 
     * @return e, parte da chave pública RSA
     *         
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * Retorna o valor de d, parte da chave privada RSA
     * 
     * @return d, parte da chave privada RSA
     *         
     */
    public BigInteger getD() {
        return d;
    }
    
    /**
     * Retorna o valor de n, componente das chaves pública e privada RSA
     * 
     * @return n, componente das chaves pública e privada RSA
     *         
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * Retorna o código de César
     * 
     * @return Código de César atual
     *         
     */
    public int getCesarCode() {
        return cesarCode;
    }

    /**
     * Define o código de César
     *
     * @param  cesarCode Código de César
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
        // e é inicializado como uma unidade a menor que n.
        e = n.subtract(BigInteger.ONE);
        
        // Enquanto não encontrar um e que é primo entre si com z, e enquanto
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
            // Valor do deslocamento é calculado, utilizando uma variável
            // inteira para evitar overflow
            int shift = modulo(messageBytes[i] + cesarCode);
            // Com a utilização do método "modulo", definido abaixo, temos
            // certeza de que o valor do inteiro está entre -128 e 127,
            // valores admitidos por um byte. Assim, realizamos um
            // casting de shift para byte.
            messageBytes[i] = (byte) shift;           
        }
        // Aplica a função matemática de criptografia, que é c = m^e mod n,
        // onde c é messageEncrypted, m é messageBytes, e é e, n é n
        BigInteger messageEncrypted = new BigInteger(messageBytes).modPow(e, n);
        // Retorna a mensagem criptografada
        return messageEncrypted;
    }
    /**
     * Descriptografa uma mensagem
     *
     * @param  ciphertext texto cifrado
     * 
     * @return mensagem em texto plano
     *         
     */
    public String decrypt(BigInteger ciphertext) {
        // Aplica a função matemática de decriptografia, e converte o resultado 
        // para um array de bytes. m = c^d mod n. Temos que m = messageBytes,
        // c = cipherText, d é d, n é n.
        byte[] messageBytes = ciphertext.modPow(d, n).toByteArray();
        // Aplica o código de Cesar
        for (int i=0;i<messageBytes.length;i++) {
            // Valor do deslocamento é calculado, utilizando uma variável
            // inteira para evitar overflow
            int shift = modulo(messageBytes[i]+256-cesarCode);
            // Com a utilização do método "modulo", definido abaixo, temos
            // certeza de que o valor do inteiro está entre -128 e 127,
            // valores admitidos por um byte. Assim, realizamos um
            // casting de shift para byte.
            messageBytes[i] = (byte) shift;
        }
        // Retorna o resultado plano como String
        return new String(messageBytes, StandardCharsets.UTF_8);
    }
    
    // Implementação personalizada da operação "modulo", devido a números
    // negativos. Como a gama de valores admitida por um byte abrange de
    // -128 a 127, checamos se o valor n, fornecido como entrada, ultrapassa
    // esses valores. Se não, ele próprio é retornado. Se sim, e n estiver
    // acima de 127, ele é mapeado para a parte negativa da gama de valores,
    // ou seja, é subtraído de 256. Se estiver abaixo de -128, é mapeado para
    // a part positiva, somando-se 256.
    private int modulo(int n) {
        if(n > 127) {
            return n - 256;
        }
        if(n < -128) {
            return n + 256;
        }
        return n;
    }
    
}
