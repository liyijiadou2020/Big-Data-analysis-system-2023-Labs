package com.example.xml.dom4j;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

public class Lab1 {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    static final String ENCODING = "UTF-8";
    static final String PASSWORD = "46EBA22EF5204DD5B110A1F730513965";
    static Cipher cipher = null;
    static SecretKeySpec secretKeySpec = null;

    public static void main(String[] args) {
        SAXReader saxReader = new SAXReader();
        try {
            // 1. obfuscate xml file：Encrypting attribute values and node content using the AES algorithm
            Document document = saxReader.read(new File("employees.xml"));
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            secretKeySpec = getSecretKey(PASSWORD);
            obfuscate(document);
            writeXml(document, "./obfuscated.xml");

            // 2. deobfuscate xml file:
            Document document2 = saxReader.read(new File("obfuscated.xml"));
            secretKeySpec = getSecretKey(PASSWORD);
            deobfuscate(document2);
            writeXml(document2, "./deobfuscated.xml");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    } // main

    public static void obfuscate(Document document) {
        Element root = document.getRootElement();
        obfuscate_node(root);

    }

    public static void deobfuscate(Document document) throws Exception {
        Element root = document.getRootElement();
        deobfuscate_node(root);
    }

    private static void writeXml(Document document, String pathStr) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputFormat format= OutputFormat.createPrettyPrint();
        format.setEncoding(ENCODING);
        try {
            XMLWriter writer=new XMLWriter(out,format);
            writer.write(document);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void obfuscate_node(Element node) {
        if (!node.getTextTrim().isEmpty()) {
            node.setText(encryptString(node.getTextTrim()));
        }
        final List<Attribute> listAttr = node.attributes();
        for (final Attribute attr : listAttr) {
            final String name = attr.getName();
            final String value = attr.getValue();
            if (!value.isEmpty()) {
                attr.setValue(encryptString(value)); // bug
            }
        }
        final List<Element> listElement = node.elements();
        for (final Element e : listElement) {
            obfuscate_node(e);
        }
    }


    private static void deobfuscate_node(Element node) throws Exception {
        if (!node.getTextTrim().isEmpty()) {
            node.setText(decryptString(node.getTextTrim()));
        }

        final List<Attribute> listAttr = node.attributes();
        for (final Attribute attr : listAttr) {
            final String name = attr.getName();
            final String value = attr.getValue();
            if (!value.isEmpty()) {
                attr.setValue(decryptString(value));
            }
        }
        final List<Element> listElement = node.elements();
        for (final Element e : listElement) {
            deobfuscate_node(e);
        }
    }



    /**
     * AES encrypt
     * @param content plain text
     * @return encrypted text
     */
    public static String encryptString(String content) {
        if (content.isEmpty()) {
            return null;
        }

        byte[] result;
        try {
            byte[] contentBytes = content.getBytes(ENCODING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            result = cipher.doFinal(contentBytes);
        } catch (UnsupportedEncodingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * AES decrypt
     * @param encrypted encrypted text
     * @return plain text
     * @throws Exception
     */
    public static String decryptString(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(PASSWORD));
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(result, ENCODING);
    }

    /**
     * Generate secret key
     * @return secret key
     */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        kg.init(128, random);

        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }




}