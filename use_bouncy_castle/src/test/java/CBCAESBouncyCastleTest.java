import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liyijia
 * @create 2023-10-2023/10/3
 */
class CBCAESBouncyCastleTest {

    /**
     * Test
     */
    public static void testCBCAESBouncyCastle() throws DataLengthException, InvalidCipherTextException, UnsupportedEncodingException {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[32];
        random.nextBytes(key);

        CBCAESBouncyCastle cabc = new CBCAESBouncyCastle();
        cabc.setKey(key);

        String input = "Hello world!";
        System.out.println("> Input[" + input.length() + "]: " + input);

        byte[] plain = input.getBytes("UTF-8");
        System.out.println("> Plain text[" + plain.length + "]: " + new String(Hex.encode(plain))); // Hex编码

        byte[] encr = cabc.encrypt(plain);
        System.out.println("> Encrypted[" + encr.length + "]: " + new String(Hex.encode(encr)));

        byte[] decr = cabc.decrypt(encr);
        System.out.println("> Decrypted[" + decr.length + "]: " + new String(Hex.encode(decr)));

        String output = new String(decr, "UTF-8");
        System.out.println("> Decrypt result[" + output.length() + "]: " + output);

        assertEquals(input.length(), output.length());
        assertEquals(input, output);
    }

    public static void main(String[] args) throws InvalidCipherTextException, UnsupportedEncodingException {
        testCBCAESBouncyCastle();
    }

}