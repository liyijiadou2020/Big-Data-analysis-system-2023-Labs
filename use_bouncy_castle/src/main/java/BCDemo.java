import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class BCDemo {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider()); // 添加BouncyCastle提供程序

        // 生成公钥和私钥
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        // 加密消息
        String plainText = "Hello World!";
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedText = cipher.doFinal(plainText.getBytes());
        System.out.println("加密后的消息:" + Base64.getEncoder().encodeToString(encryptedText));

        // 解密消息
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedText = cipher.doFinal(encryptedText);
        System.out.println("解密后的消息:" + new String(decryptedText));

        // 签名
        byte[] plainData = plainText.getBytes();
        Cipher signer = Cipher.getInstance("RSA", "BC");
        signer.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] signature = signer.doFinal(plainData);
        System.out.println("消息签名：" + Hex.toHexString(signature));

        // 验证签名
        Cipher verifier = Cipher.getInstance("RSA", "BC");
        verifier.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] verifiedData = verifier.doFinal(signature);
        if (Hex.toHexString(verifiedData).equals(Hex.toHexString(plainData))) {
            System.out.println("消息验证成功。");
        } else {
            System.out.println("消息验证失败。");
        }
    }
}