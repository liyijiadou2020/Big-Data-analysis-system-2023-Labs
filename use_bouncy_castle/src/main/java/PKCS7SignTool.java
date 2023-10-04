//import com.sun.org.apache.xpath.internal.operations.String;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.*;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.CollectionStore;

import javax.print.DocFlavor;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static java.util.Base64.getDecoder;

/**
 * 废了
 * @author liyijia
 * @create 2023-10-2023/10/3
 */
public class PKCS7SignTool {
    public static void main(String[] args) {
        try {
            Security.setProperty("crypto.policy", "unlimited");
            int maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");
            System.out.println("Max Key Size for AES: " + maxKeySize); // ok

            Security.addProvider(new BouncyCastleProvider());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(Files.newInputStream(Paths.get("Baeldung.cer")));

            char[] keyStorePassword = "password".toCharArray();
            char[] keyPassword = "password".toCharArray();

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream("Baeldung.p12"), keyStorePassword);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey("baeldung", keyPassword);

            java.lang.String message = "Hello world";
            System.out.println("message : " + message);

            byte[] stringToEncrypt = message.getBytes();
            byte[] encryptedDate = encryptData(stringToEncrypt, certificate); // #TODO: 验证
            System.out.println("Encrypted message : " + encryptedDate.toString());
            byte[] rawData = decryptDate(encryptedDate, privateKey);
            System.out.println("Decrypted message : " + rawData.toString());
        } catch (Exception e) {

        }
    }

//
//    /**
//     * 验签
//     * @param certFile
//     * @param originMessage
//     * @param signedMessage
//     * @return
//     * @throws FileNotFoundException
//     * @throws CertificateException
////     * @throws CMSException
////     * @throws OperatorCreationException
//     */
//    public static boolean verify(String certFile, byte[] originMessage, byte[] signedMessage)
//            throws FileNotFoundException, CertificateException, NoSuchProviderException, CMSException, OperatorCreationException {
//        X509Certificate certificate = loadCert(certFile);
//        CMSSignedData sign = new CMSSignedData(new CMSProcessableByteArray(originMessage), Base64.getDecoder().decode(signedMessage));
//
//        CollectionStore<X509CertificateHolder> certificateHolderStore = (CollectionStore)sign.getCertificates();
//        for (Iterator i = certificateHolderStore.iterator(); i.hasNext(); ) {
//            X509Certificate x509Cert = new JcaX509CertificateConverter().getCertificate((X509CertificateHolder) i.next());
//            //System.out.println("cert in signedMsg: "+x509Cert.getSubjectDN()+x509Cert.getSerialNumber());
//        }
//
//        SignerInformationStore signers = sign.getSignerInfos();
//        SignerInformation signerInfo = signers.getSigners().iterator().next();
//        //这里证书使用了传入的证书，没有用签名文件中的证书。实际正常都要用到的。
//        PublicKey publicKey = certificate.getPublicKey();
//        return signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider("BC").build(publicKey));
//    }
//
//    /**
//     * 签名
//     * @param certFile - 证书
//     * @param keyFile - 密钥文件名
//     * @param srcMessage - 待签名的数据
//     * @param containCert -  #todo 是否包含证书？
//     * @return
//     * @throws IOException
//     * @throws CertificateException
//     * @throws CMSException
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidKeySpecException
//     * @throws OperatorCreationException
//     */
//    public static byte[] sign(String certFile, String keyFile, byte[] srcMessage, boolean containCert)
//            throws IOException, CertificateException, NoSuchAlgorithmException,
//            InvalidKeySpecException, NoSuchProviderException, OperatorCreationException, CMSException
//    {
//        X509Certificate certificate = loadCert(certFile);
//
//        byte[] encodedKey = Files.readAllBytes(Paths.get(keyFile)); // 读取密钥
//        String keyStr = new String(encodedKey).replace("-----BEGIN RSA PRIVATE KEY-----", "")
//                .replace("-----END RSA PRIVATE KEY-----", "");
//
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyStr));
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        PrivateKey privateKey = kf.generatePrivate(spec);
//
//        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(privateKey);
//
//        CMSSignedDataGenerator generator  = new CMSSignedDataGenerator();
//        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
//                new JcaDigestCalculatorProviderBuilder().setProvider("BC")
//                        .build()).build(signer,  (X509Certificate)certificate));
//
//        //add cert to generated sign data;
//        if(containCert){
//            generator.addCertificates(new JcaCertStore(Arrays.asList(certificate)));
//        }
//
//        CMSSignedData signedData = generator.generate(new CMSProcessableByteArray(srcMessage), false);
//        return Base64.getDecoder().decode(signedData.getEncoded());
//    }

//    /**
//     * 加载证书
//     * @param certFile
//     * @return
//     * @throws FileNotFoundException
//     * @throws CertificateException
//     */
////    private static Certificate loadCert(String certFile) throws FileNotFoundException, CertificateException, NoSuchProviderException {
//    private static X509Certificate loadCert(String certFile) throws FileNotFoundException, CertificateException, NoSuchProviderException {
//        Security.addProvider(new BouncyCastleProvider());
//        CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
//        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new FileInputStream(certFile));
//        return certificate;
//    }


    public static byte[] encryptData(byte[] data, X509Certificate encryptionCertificate)
            throws CertificateEncodingException, CMSException, IOException {
        byte[] encryptedData = null;
        if (null != data && null != encryptionCertificate) {
            CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator
                    = new CMSEnvelopedDataGenerator();

            JceKeyTransRecipientInfoGenerator jceKey
                    = new JceKeyTransRecipientInfoGenerator(encryptionCertificate);
            cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
            CMSTypedData msg = new CMSProcessableByteArray(data);
            OutputEncryptor encryptor
                    = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
                    .setProvider("BC").build();
            CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator
                    .generate(msg,encryptor);
            encryptedData = cmsEnvelopedData.getEncoded();
        }
        return encryptedData;
    }



    /**
     * 解密
     * @param encryptedData - 加密后的数据
     * @param privateKey - 密钥
     * @return 返回解密后的数据
     */
    public static byte[] decryptDate(byte[] encryptedData, PrivateKey privateKey) throws CMSException {
        byte[] decrypted = null;
        if (null != encryptedData && null != privateKey) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);

            Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInformation = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(privateKey);

            return recipientInformation.getContent(recipient);
        }
        return decrypted;
    }


}
