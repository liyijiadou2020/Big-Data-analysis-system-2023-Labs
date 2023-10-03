import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import static java.util.Base64.*;

/**
 * @author liyijia
 * @create 2023-10-2023/10/3
 */
public class Demo {
    public static void main(String[] args) {
        try {
            RSAEncodeDemo();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    CSM/PKCS7 加密
    public static void encryptData(byte[] data, X509Certificate encryptionCertificate)  {
        byte[] encryptedData = null;
        if (null != data && null != encryptionCertificate) {

        }

    }

    public static void RSAEncodeDemo() throws Exception {
        //生成密钥对
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        RSAKeyGenerationParameters rsaKeyGenerationParameters = new RSAKeyGenerationParameters(BigInteger.valueOf(3),
                new SecureRandom(), 1024, 25);
        //初始化参数
        rsaKeyPairGenerator.init(rsaKeyGenerationParameters);
        AsymmetricCipherKeyPair keyPair = rsaKeyPairGenerator.generateKeyPair();
        //公钥
        AsymmetricKeyParameter publicKey = keyPair.getPublic();
        //私钥
        AsymmetricKeyParameter privateKey = keyPair.getPrivate();

        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey);
        PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey);
        //变字符串
        ASN1Object asn1ObjectPublic = subjectPublicKeyInfo.toASN1Primitive();
        byte[] publicInfoByte = asn1ObjectPublic.getEncoded();
        ASN1Object asn1ObjectPrivate = privateKeyInfo.toASN1Primitive();
        byte[] privateInfoByte = asn1ObjectPrivate.getEncoded();
        //这里可以将密钥对保存到本地
        final Base64.Encoder encoder64 = Base64.getEncoder();
        System.out.println("PublicKey:\n" + encoder64.encodeToString(publicInfoByte));
        System.out.println("PrivateKey:\n" + encoder64.encodeToString(privateInfoByte));
        //加密、解密
        ASN1Object pubKeyObj = subjectPublicKeyInfo.toASN1Primitive();//这里也可以从流中读取，从本地导入
        AsymmetricKeyParameter pubKey = PublicKeyFactory.createKey(SubjectPublicKeyInfo.getInstance(pubKeyObj));
        AsymmetricBlockCipher cipher = new RSAEngine();
        cipher.init(true, pubKey);//true表示加密

        final Decoder decoder64 = getDecoder();
        //加密
        String data = "七叶洋地黄双苷滴眼液 0.4ml";
        System.out.println("\n明文：" + data);
        byte[] encryptData = cipher.processBlock(data.getBytes("utf-8")
                , 0, data.getBytes("utf-8").length);
        System.out.println("密文:" + encoder64.encodeToString(encryptData));

        //解密
        AsymmetricKeyParameter priKey = PrivateKeyFactory.createKey(privateInfoByte);
        cipher.init(false, priKey);//false表示解密
        byte[] decriyptData = cipher.processBlock(encryptData, 0, encryptData.length);
        String decryptData = new String(decriyptData, "utf-8");
        System.out.println("解密后数据：" + decryptData);
    }
}
