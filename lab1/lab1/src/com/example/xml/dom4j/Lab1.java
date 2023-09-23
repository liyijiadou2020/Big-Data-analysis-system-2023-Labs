package com.example.xml.dom4j;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.XMLWriter;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Lab1 {

//    static Document document = null;
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    static final String ENCODING = "UTF-8";
    static final String PASSWORD = "46EBA22EF5204DD5B110A1F730513965"; // 加密秘钥
    static Cipher cipher = null;
    static SecretKeySpec secretKeySpec = null;

    public static void main(String[] args) {
        SAXWriter saxWriter = new SAXWriter();
        SAXReader saxReader = new SAXReader();
        try {
            // 1. 加密：使用AES加密属性值和节点内容
            Document document = saxReader.read(new File("employees.xml"));
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            secretKeySpec = getSecretKey(PASSWORD);
            obfuscate(document);
            writeXml(document, "./obfuscated.xml");

            // 2. 解密
            Document document2 = saxReader.read(new File("obfuscated.xml"));
            secretKeySpec = getSecretKey(PASSWORD);
            deobfuscate(document2);
            writeXml(document2, "./deobfuscated.xml");


        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
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
        //指定文件输出的位置
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(pathStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 指定文本的写出的格式：
        OutputFormat format= OutputFormat.createPrettyPrint();   //有空格换行
        format.setEncoding(ENCODING);
        try {
            //1.创建写出对象
            XMLWriter writer=new XMLWriter(out,format);
            //2.写出Document对象
            writer.write(document);
            //3.关闭流
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void obfuscate_node(Element node) {

        // 当前节点的名称、文本内容和属性
        if (!node.getTextTrim().isEmpty()) {
            node.setText(encryptString(node.getTextTrim()));
        }

        final List<Attribute> listAttr = node.attributes();// 当前节点的所有属性
        for (final Attribute attr : listAttr) {// 遍历当前节点的所有属性
            final String name = attr.getName();// 属性名称
            final String value = attr.getValue();// 属性的值
            if (!value.isEmpty()) {
                attr.setValue(encryptString(value)); // bug
            }
        }

        // 递归遍历当前节点所有的子节点
        final List<Element> listElement = node.elements();// 所有一级子节点的list
        for (final Element e : listElement) {// 遍历所有一级子节点
            obfuscate_node(e);// 递归
        }
    }


    private static void deobfuscate_node(Element node) throws Exception {

        if (!node.getTextTrim().isEmpty()) {
            node.setText(decryptString(node.getTextTrim()));
        }

        final List<Attribute> listAttr = node.attributes();// 当前节点的所有属性
        for (final Attribute attr : listAttr) {// 遍历当前节点的所有属性
            final String name = attr.getName();// 属性名称
            final String value = attr.getValue();// 属性的值
            if (!value.isEmpty()) {
                attr.setValue(decryptString(value)); // bug
            }
        }

        // 递归遍历当前节点所有的子节点
        final List<Element> listElement = node.elements();// 所有一级子节点的list
        for (final Element e : listElement) {// 遍历所有一级子节点
            deobfuscate_node(e);// 递归
        }
    }



    /**
     * AES加密
     * #TODO: 测试
     * @param content 明文
     * @return 密文
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
     * AES解密
     * @param encrypted
     *         已加密的密文
     * @return 返回解密后的数据
     * @throws Exception
     */
    public static String decryptString(String encrypted) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(PASSWORD));
        //执行操作
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(result, ENCODING);
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // javax.crypto.BadPaddingException: Given final block not properly padded解决方案
        // https://www.cnblogs.com/zempty/p/4318902.html - 用此法解决的
        // https://www.cnblogs.com/digdeep/p/5580244.html - 留作参考吧
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        //AES 要求密钥长度为 128
        kg.init(128, random);

        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        // 转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }




}