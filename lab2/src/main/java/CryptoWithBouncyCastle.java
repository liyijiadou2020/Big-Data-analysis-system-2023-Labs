import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.util.encoders.Hex;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CryptoWithBouncyCastle {
    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            CertificateFactory certFactory= CertificateFactory.getInstance("X.509", "BC");
            X509Certificate certificate = (X509Certificate) certFactory.generateCertificate(new FileInputStream("Baeldung.cer"));

            char[] keystorePassword = "password".toCharArray();
            char[] keyPassword = "password".toCharArray();

            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(new FileInputStream("Baeldung.p12"), keystorePassword);
            PrivateKey key = (PrivateKey) keystore.getKey("baeldung", keyPassword);

            String secretMessage = "My name is Li Yijia. I study in SPBSTU. ";
            System.out.println("\nOriginal Message : " + secretMessage);

            byte[] stringToEncrypt = secretMessage.getBytes();
            byte[] encryptedData = encryptData(stringToEncrypt, certificate); // 1. шифрование
            System.out.println("\nEncrypted Message : ");
            System.out.println(new String(encryptedData));
//            System.out.println(new String(Hex.encode(encryptedData)));

            byte[] rawData = decryptData(encryptedData, key); // 2. дешифрование
            String decryptedMessage = new String(rawData);
            System.out.println("\nDecrypted Message : ");
            System.out.println(decryptedMessage);

            byte[] signedData = signData(rawData, certificate, key); // 3. подписать
            Boolean check = verifySignedData(signedData); // 4. проверять подпись
            System.out.println("\nResult verification sign: " + check);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static byte[] encryptData(byte[] data,
                                     X509Certificate encryptionCertificate)
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

    public static byte[] decryptData(
            byte[] encryptedData,
            PrivateKey decryptionKey)
            throws CMSException {

        byte[] decryptedData = null;
        if (null != encryptedData && null != decryptionKey) {
            CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);

            Collection<RecipientInformation> recipients
                    = envelopedData.getRecipientInfos().getRecipients();
            KeyTransRecipientInformation recipientInfo
                    = (KeyTransRecipientInformation) recipients.iterator().next();
            JceKeyTransRecipient recipient
                    = new JceKeyTransEnvelopedRecipient(decryptionKey);

            return recipientInfo.getContent(recipient);
        }
        return decryptedData;
    }

    public static byte[] signData(
            byte[] data,
            X509Certificate signingCertificate,
            PrivateKey signingKey) throws Exception {

        byte[] signedMessage = null;
        List<X509Certificate> certList = new ArrayList<X509Certificate>();
        CMSTypedData cmsData= new CMSProcessableByteArray(data);
        certList.add(signingCertificate);
        Store certs = new JcaCertStore(certList);

        CMSSignedDataGenerator cmsGenerator = new CMSSignedDataGenerator();
        ContentSigner contentSigner
                = new JcaContentSignerBuilder("SHA256withRSA").build(signingKey);
        cmsGenerator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().setProvider("BC")
                        .build()).build(contentSigner, signingCertificate));
        cmsGenerator.addCertificates(certs);

        CMSSignedData cms = cmsGenerator.generate(cmsData, true);
        signedMessage = cms.getEncoded();
        return signedMessage;
    }

    public static boolean verifySignedData(byte[] signedData)
            throws Exception {
//        X509Certificate signCert = null;
        ByteArrayInputStream inputStream
                = new ByteArrayInputStream(signedData);
        ASN1InputStream asnInputStream = new ASN1InputStream(inputStream);
        CMSSignedData cmsSignedData = new CMSSignedData(ContentInfo.getInstance(asnInputStream.readObject()));

        SignerInformationStore signers = cmsSignedData.getSignerInfos();
        SignerInformation signer = signers.getSigners().iterator().next();
        Collection<X509CertificateHolder> certCollection = cmsSignedData.getCertificates().getMatches(signer.getSID());
        X509CertificateHolder certHolder = certCollection.iterator().next();

        return signer
                .verify(new JcaSimpleSignerInfoVerifierBuilder()
                        .build(certHolder));
    }

}
