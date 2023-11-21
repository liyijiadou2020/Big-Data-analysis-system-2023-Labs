# Big-Data-analysis-system-2023-Labs
Группа № 5140904/30202, студентка Ли Ицзя

# 实验目录
1. 数据混淆和反混淆软件工具的实现
2. 使用 BouncyCastle 标准库创建数据加密的演示程序和测试脚本
3. 客户端-服务器应用程序的设计和实现，使用 SSL/TLS 加密密钥通过 HTTPS 协议进行通信（双向TLS）
4. 使用 Netflix Zuul 库为通过网关运行的客户端-服务器应用程序创建路由器。

#  𝙉𝙤𝙩𝙚𝙨 | 笔记
## 1 LAB 1 数据混淆
> 参考资料
> 
> 
> XML文件的结构：
> 
> https://blog.csdn.net/weixin_52307797/article/details/117920107
> 
> obfuscation后的XML需要满足什么条件？【ChatGPT】
> 
> 在进行 XML 混淆（obfuscation）处理时，需要注意以下几点，以确保生成的 XML 文件仍然可以被解析和使用：
> 
> 1. XML 文件的语法必须仍然是有效的。即使对文件进行了混淆处理，XML 文件必须仍然符合 XML 语法规范，否则无法被其他应用程序和系统正确解析。
> 2. XML 文件中的元素和属性名称必须仍然是唯一的。在混淆 XML 文件时，可以使用某些技术来重命名元素和属性，但是这些名称必须仍然是唯一的，以保证 XML 文件的结构完整性。
> 3. 混淆后的 XML 文件必须仍然具有可读性和可维护性。尽管混淆可以使 XML 文件变得更加复杂和难以理解，但是混淆后的文件应该仍然具有一定程度的可读性和可维护性，以方便后续编辑和维护。
> 4. 混淆后的 XML 文件应该保留必要的注释和文档结构。注释和文档结构对于理解 XML 文件的含义和结构非常重要，因此在进行混淆处理时，应该保留必要的注释和文档结构，以便其他人理解和使用文件。
> 
> 总之，混淆后的 XML 文件应该仍然具有标准的 XML 结构和语法，保留必要的元素和属性名称，可读性和可维护性，以及必要的注释和文档结构。
>

混淆前：
```XML
<employees>
	<employee id="111">
		<firstName>
			Lokesh
		</firstName>
		<lastName>
			Gupta
		</lastName>
		<location>
			India
		</location>
	</employee>
	<employee id="222">
		<firstName>
			Alex
		</firstName>
		<lastName>
			Gussin
		</lastName>
		<location>
			Russia
		</location>
	</employee>
	<employee id="333">
		<firstName>
			David
		</firstName>
		<lastName>
			Feezor
		</lastName>
		<location>
			USA
		</location>
	</employee>
</employees>
```

混淆后：
```XML
<e1 xmlns:n="http://example.com/namespace">
	<e2 a1="111">
		<e3>
			Lokesh
		</e3>
		<e4>
			Gupta
		</e4>
		<e5>
			India
		</e5>
	</e2>
	<e2 a1="222">
		<e3>
			Alex
		</e3>
		<e4>
			Gussin
		</e4>
		<e5>
			Russia
		</e5>
	</e2>
	<e2 a1="333">
		<e3>
			David
		</e3>
		<e4>
			Feezor
		</e4>
		<e5>
			USA
		</e5>
	</e2>
</e1>
```

所用到的工具：
XML解析工具：dom4j

> ****Java XML解析工具 dom4j介绍及使用实例：****https://cloud.tencent.com/developer/article/1058617
> 

> ****通过代码修改xml文件内容****https://blog.csdn.net/Aizen_Sousuke/article/details/106288137
> 

> ****JAVA对字符串内容进行AES、DES加密解密**** https://cloud.tencent.com/developer/article/1650310
> 

> ****Dom4j解析xml文件-递归遍历所有节点和属性****https://blog.csdn.net/chenleixing/article/details/44353491
>

实验成功。

---

## 2 LAB 2 使用BouncyCastle库加密

老师的代码是抄这里的：https://www.baeldung.com/java-bouncy-castle

---

## 3 LAB 3 设计和实现客户端和服务端应用，相互之间通过 HTTP 协议通讯，使用双向的 SSL / TLS 加密

实验步骤：
1. 创建客户的自签名证书
2. 创建服务器的自签名证书
3. 从客户的自签名证书中提取公开证书（公钥？）
4. 从服务器的自签名证书中提取公开证书（公钥？）
5. 服务器安装双向SSL
6. 客户安装双向SSL
7. 应用启动

```shell

# Создание самоподписанного сертификата клиента 
# 在client\src\main\resources\static下创建客户的自签名证书(jks是私钥格式)
keytool -genkeypair -alias gateway -keyalg RSA -keysize 2048 -storetype JKS -keystore gateway.jks -validity 3650 -dname "CN=San Zhang, OU=MyDepartment, O=MyCompany, L=Beijing, ST=Beijing, C=CN" -ext SAN=dns:localhost,ip:127.0.0.1 -keypass password -storepass password

# Создание файла публичного сертификата из сертификата клиента 
# 创建客户的公钥(crt是公钥格式)
keytool -export -alias gateway -file gateway.crt -keystore gateway.jks


# Создание самоподписанного сертификата сервера 
# 在server\src\main\resources\static下创建服务器的自签名证书
keytool -genkeypair -alias gateway -keyalg RSA -keysize 2048 -storetype JKS -keystore server.jks -validity 3650 -dname "CN=Si Li, OU=SPBSTU, O=SPBSTU, L=SPB, ST=SPB, C=RU" -ext SAN=dns:localhost,ip:127.0.0.1 -keypass password -storepass password

# Создание файла публичного сертификата из сертификата сервера 
# 导出公开证书 server.crt
keytool -export -alias server -file server.crt -keystore server.jks

# Импорт клиентского сертификат в jks-файл сервера
# 把客户端的gateway.crt复制到服务器的src\main\resources\static目录下,然后向服务器的server.jks中加入gateway的证书
keytool -import -alias gateway -file gateway.crt -keystore server.jks
# 检验server.jks是否具有自己的privateKey和gateway的cert
keytool -list -keystore server.jks -storepass password

# Импорт сертификата сервера в jks-файл клиента делается аналогично:
# 把服务端的jks复制到客户端的resource目录下。
keytool -import -alias server -file server.crt -keystore gateway.jks
# 检验gateway.jks是否具有自己的privateKey和server的cert
keytool -list -keystore gateway.jks -storepass password

# 在服务器的证书目录下完成转化：server.jks -> server.p12
keytool -importkeystore -srckeystore server.jks -destkeystore server.p12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass password -deststorepass password -srcalias server -destalias server -srckeypass password -destkeypass password -noprompt



keytool -importkeystore -srckeystore gateway.jks -destkeystore gateway.p12 -srcstoretype JKS -deststoretype PKCS12 -srcstorepass password -deststorepass password -srcalias gateway -destalias gateway -srckeypass password -destkeypass password -noprompt



```

```YAML
# Настройка сервера для двухстороннего SSL
# Добавьте записи, показанные ниже, в application.yml:
spring:
application:
 name: server
server:
port: 8765
ssl:
 enabled: true
 client-auth: need
 key-store: classpath:server.jks
 key-store-password: password
 key-alias: server
 key-store-type: JKS
 key-store-provider: SUN
 trust-store: classpath:server.jks
 trust-store-password: password
 trust-store-type: JKS

```
```yaml
# Настройка сервера для двухстороннего SSL
# Добавьте записи, показанные ниже, в application.yml:
spring:
application:
 name: server
server:
port: 8765
ssl:
 enabled: true
 client-auth: need
 key-store: classpath:server.jks
 key-store-password: password
 key-alias: server
 key-store-type: JKS
 key-store-provider: SUN
 trust-store: classpath:server.jks
 trust-store-password: password
 trust-store-type: JKS

```

```java
// **Создайте класс контроллера с конечной точкой REST для обслуживания входящего запроса
@RestController
@RequestMapping(value = "/server")
public class ServerController {
@RequestMapping(value = "/data", method = RequestMethod.GET)
 public String getData() {
 System.out.println("Returning data from server");
 return "Hello from server";
 }
}**
```

```yaml
# Настройка клиента для двухстороннего SSL

spring:
application:
 name: gateway
server:
port: 8080
ssl:
 enabled: true
 client-auth: need
 key-store: classpath:gateway.jks
 key-store-password: password
 key-alias: gateway
 key-store-type: JKS
 key-store-provider: SUN
 trust-store: classpath:gateway.jks
 trust-store-password: password
 trust-store-type: JKS
```

Также необходимо добавить зависимости, представленные ниже:
compile 'org.apache.httpcomponents:httpcore',
'org.apache.httpcomponents:httpclient'

```java
@Bean
public RestTemplate getRestTemplate() {
 RestTemplate restTemplate = new RestTemplate();
 KeyStore keyStore;
 HttpComponentsClientHttpRequestFactory requestFactory = null;
 try {
 keyStore = KeyStore.getInstance("jks");
 ClassPathResource classPathResource = new 
ClassPathResource("gateway.jks");
 InputStream inputStream = classPathResource.getInputStream();
 keyStore.load(inputStream, "password".toCharArray());
 SSLConnectionSocketFactory socketFactory = new 
SSLConnectionSocketFactory(new SSLContextBuilder()
 .loadTrustMaterial(null, new TrustSelfSignedStrategy())
 .loadKeyMaterial(keyStore, "password".toCharArray()).build(), 
NoopHostnameVerifier.INSTANCE);
 HttpClient httpClient = 
HttpClients.custom().setSSLSocketFactory(socketFactory)
 .setMaxConnTotal(5)
 .setMaxConnPerRoute(5)
 .build();
 requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
 requestFactory.setReadTimeout(10000);
 requestFactory.setConnectTimeout(10000);
 restTemplate.setRequestFactory(requestFactory);
 } catch (Exception exception) {
 exception.printStackTrace();
 }
 return restTemplate;
}
```

```java
После этого мы создадим класс контроллера с 2 методами:
@RestController
@RequestMapping(value="/gateway")
public class GatewayController {
@Autowired
RestTemplate restTemplate;
@Autowired
Environment env;
@RequestMapping(value = "/data", method = RequestMethod.GET)
public String getData() {
System.out.println("Returning data from gateway");
return "Hello from gateway";
}
@RequestMapping(value = "/server-data", method = RequestMethod.GET)
public String getServerData() {
System.out.println("Returning data from serer through gateway");
try {
 String msEndpoint = env.getProperty("endpoint.server");
 System.out.println("Endpoint name : [" + msEndpoint + "]");
 return restTemplate.getForObject(new 
URI(Objects.requireNonNull(msEndpoint)), String.class);
} catch (Exception ex) {
 ex.printStackTrace();
}
return "Exception occurred";
}
}
```

到这一步其实已经好了，但是如果我们用浏览器去访问 https://localhost:8080 会报错：
```Java
此网站无法提供安全连接localhost 不接受您的登录证书，或者您可能没有提供登录证书。
请尝试联系系统管理员。
ERR_BAD_SSL_CLIENT_AUTH_CERT
```

参考这里，问题得到了解决：https://www.cnblogs.com/librarookie/p/16807218.html

我们的web程序会向浏览器索取证书。

所以我们需要向浏览器导入证书。

```Shell
# 在 /src/main/resources 目录下找到 server.p12
# 双击证书，按照指示填写密码
# 成功导入证书
```

然而server.p12 安装完之后报错：
![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311192359967.png)
![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311200000051.png)
同意安装证书。好消息：连接上了！坏消息：连接上了，但是抛出错误！
![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311200001318.png)
原来是因为没有设置 MaxConnTotal。回到Client中设置：

```java
private static HttpClientConnectionManager getHttpClientConnectionManager() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, CertificateException, IOException {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(Integer.valueOf(5))
                .setMaxConnPerRoute(Integer.valueOf(5))
                .setSSLSocketFactory(getSslConnectionSocketFactory())
                .build();
    }
```

访问这里： https://localhost:8080/server/data 。**千万记住别漏了s**，这很关键！
最后成功了！
![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311200001547.png)

---

## 4 LAB 4 Создание маршрутизатора для клиент-серверного приложения, работающего через Gateway посредством библиотеки Netflix Zuul 使用 Netflix Zuul 库为通过 Gateway 运行的客户端-服务器应用程序创建路由器

报错：

```bash
"C:\\Program Files\\Microsoft\\jdk-11.0.16.101-hotspot\\bin\\java.exe" -XX:TieredStopAtLevel=1 -noverify -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true "-Dmanagement.endpoints.jmx.exposure.include=*" "-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2.2\\lib\\idea_rt.jar=1716:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2.2\\bin" -Dfile.encoding=UTF-8 -classpath C:\\source\\Lab-big_data\\lab4\\client5\\target\\classes;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-web\\2.3.12.RELEASE\\spring-boot-starter-web-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter\\2.3.12.RELEASE\\spring-boot-starter-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot\\2.3.12.RELEASE\\spring-boot-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-autoconfigure\\2.3.12.RELEASE\\spring-boot-autoconfigure-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-logging\\2.3.12.RELEASE\\spring-boot-starter-logging-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\ch\\qos\\logback\\logback-classic\\1.2.3\\logback-classic-1.2.3.jar;C:\\apache-maven-3.9.4\\ch\\qos\\logback\\logback-core\\1.2.3\\logback-core-1.2.3.jar;C:\\apache-maven-3.9.4\\org\\apache\\logging\\log4j\\log4j-to-slf4j\\2.13.3\\log4j-to-slf4j-2.13.3.jar;C:\\apache-maven-3.9.4\\org\\apache\\logging\\log4j\\log4j-api\\2.13.3\\log4j-api-2.13.3.jar;C:\\apache-maven-3.9.4\\org\\slf4j\\jul-to-slf4j\\1.7.30\\jul-to-slf4j-1.7.30.jar;C:\\apache-maven-3.9.4\\jakarta\\annotation\\jakarta.annotation-api\\1.3.5\\jakarta.annotation-api-1.3.5.jar;C:\\apache-maven-3.9.4\\org\\yaml\\snakeyaml\\1.26\\snakeyaml-1.26.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-json\\2.3.12.RELEASE\\spring-boot-starter-json-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\core\\jackson-databind\\2.11.4\\jackson-databind-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\core\\jackson-annotations\\2.11.4\\jackson-annotations-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\core\\jackson-core\\2.11.4\\jackson-core-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\datatype\\jackson-datatype-jdk8\\2.11.4\\jackson-datatype-jdk8-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\datatype\\jackson-datatype-jsr310\\2.11.4\\jackson-datatype-jsr310-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\module\\jackson-module-parameter-names\\2.11.4\\jackson-module-parameter-names-2.11.4.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-tomcat\\2.3.12.RELEASE\\spring-boot-starter-tomcat-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\apache\\tomcat\\embed\\tomcat-embed-core\\9.0.46\\tomcat-embed-core-9.0.46.jar;C:\\apache-maven-3.9.4\\org\\glassfish\\jakarta.el\\3.0.3\\jakarta.el-3.0.3.jar;C:\\apache-maven-3.9.4\\org\\apache\\tomcat\\embed\\tomcat-embed-websocket\\9.0.46\\tomcat-embed-websocket-9.0.46.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-web\\5.2.15.RELEASE\\spring-web-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-beans\\5.2.15.RELEASE\\spring-beans-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-webmvc\\5.2.15.RELEASE\\spring-webmvc-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-aop\\5.2.15.RELEASE\\spring-aop-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-context\\5.2.15.RELEASE\\spring-context-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-expression\\5.2.15.RELEASE\\spring-expression-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-core\\5.2.15.RELEASE\\spring-core-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-jcl\\5.2.15.RELEASE\\spring-jcl-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-zuul\\2.2.10.RELEASE\\spring-cloud-netflix-zuul-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-hystrix\\2.2.10.RELEASE\\spring-cloud-netflix-hystrix-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-aop\\2.3.12.RELEASE\\spring-boot-starter-aop-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\aspectj\\aspectjweaver\\1.9.6\\aspectjweaver-1.9.6.jar;C:\\apache-maven-3.9.4\\org\\apache\\httpcomponents\\httpclient\\4.5.13\\httpclient-4.5.13.jar;C:\\apache-maven-3.9.4\\org\\apache\\httpcomponents\\httpcore\\4.4.14\\httpcore-4.4.14.jar;C:\\apache-maven-3.9.4\\commons-codec\\commons-codec\\1.14\\commons-codec-1.14.jar;C:\\apache-maven-3.9.4\\com\\netflix\\netflix-commons\\netflix-commons-util\\0.3.0\\netflix-commons-util-0.3.0.jar;C:\\apache-maven-3.9.4\\javax\\inject\\javax.inject\\1\\javax.inject-1.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-starter-netflix-hystrix\\2.2.10.RELEASE\\spring-cloud-starter-netflix-hystrix-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-starter\\2.2.9.RELEASE\\spring-cloud-starter-2.2.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-context\\2.2.9.RELEASE\\spring-cloud-context-2.2.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\security\\spring-security-crypto\\5.3.9.RELEASE\\spring-security-crypto-5.3.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-commons\\2.2.9.RELEASE\\spring-cloud-commons-2.2.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\security\\spring-security-rsa\\1.0.9.RELEASE\\spring-security-rsa-1.0.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\bouncycastle\\bcpkix-jdk15on\\1.64\\bcpkix-jdk15on-1.64.jar;C:\\apache-maven-3.9.4\\org\\bouncycastle\\bcprov-jdk15on\\1.64\\bcprov-jdk15on-1.64.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-ribbon\\2.2.10.RELEASE\\spring-cloud-netflix-ribbon-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-archaius\\2.2.10.RELEASE\\spring-cloud-netflix-archaius-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-starter-netflix-archaius\\2.2.10.RELEASE\\spring-cloud-starter-netflix-archaius-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-core\\1.5.18\\hystrix-core-1.5.18.jar;C:\\apache-maven-3.9.4\\io\\reactivex\\rxjava\\1.3.8\\rxjava-1.3.8.jar;C:\\apache-maven-3.9.4\\org\\hdrhistogram\\HdrHistogram\\2.1.9\\HdrHistogram-2.1.9.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-serialization\\1.5.18\\hystrix-serialization-1.5.18.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\module\\jackson-module-afterburner\\2.11.4\\jackson-module-afterburner-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-metrics-event-stream\\1.5.18\\hystrix-metrics-event-stream-1.5.18.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-javanica\\1.5.18\\hystrix-javanica-1.5.18.jar;C:\\apache-maven-3.9.4\\org\\apache\\commons\\commons-lang3\\3.10\\commons-lang3-3.10.jar;C:\\apache-maven-3.9.4\\org\\ow2\\asm\\asm\\5.0.4\\asm-5.0.4.jar;C:\\apache-maven-3.9.4\\io\\reactivex\\rxjava-reactive-streams\\1.2.1\\rxjava-reactive-streams-1.2.1.jar;C:\\apache-maven-3.9.4\\org\\reactivestreams\\reactive-streams\\1.0.3\\reactive-streams-1.0.3.jar;C:\\apache-maven-3.9.4\\com\\netflix\\zuul\\zuul-core\\1.3.1\\zuul-core-1.3.1.jar;C:\\apache-maven-3.9.4\\commons-io\\commons-io\\2.4\\commons-io-2.4.jar;C:\\apache-maven-3.9.4\\org\\codehaus\\groovy\\groovy-all\\2.3.10\\groovy-all-2.3.10.jar;C:\\apache-maven-3.9.4\\org\\mockito\\mockito-all\\1.9.5\\mockito-all-1.9.5.jar;C:\\apache-maven-3.9.4\\org\\slf4j\\slf4j-api\\1.7.30\\slf4j-api-1.7.30.jar;C:\\apache-maven-3.9.4\\com\\netflix\\archaius\\archaius-core\\0.6.0\\archaius-core-0.6.0.jar;C:\\apache-maven-3.9.4\\com\\google\\guava\\guava\\11.0.2\\guava-11.0.2.jar;C:\\apache-maven-3.9.4\\com\\google\\code\\findbugs\\jsr305\\1.3.9\\jsr305-1.3.9.jar;C:\\apache-maven-3.9.4\\org\\codehaus\\jackson\\jackson-mapper-asl\\1.9.11\\jackson-mapper-asl-1.9.11.jar;C:\\apache-maven-3.9.4\\commons-configuration\\commons-configuration\\1.8\\commons-configuration-1.8.jar;C:\\apache-maven-3.9.4\\commons-lang\\commons-lang\\2.6\\commons-lang-2.6.jar;C:\\apache-maven-3.9.4\\commons-logging\\commons-logging\\1.1.1\\commons-logging-1.1.1.jar;C:\\apache-maven-3.9.4\\org\\codehaus\\jackson\\jackson-core-asl\\1.9.11\\jackson-core-asl-1.9.11.jar;C:\\apache-maven-3.9.4\\com\\google\\code\\findbugs\\annotations\\2.0.0\\annotations-2.0.0.jar;C:\\apache-maven-3.9.4\\com\\netflix\\servo\\servo-core\\0.7.2\\servo-core-0.7.2.jar com.lab4.client5.Client5Application --add-opens java.base/jdk.internal.loader=ALL-UNNAMED
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass$3$1 (file:/C:/apache-maven-3.9.4/org/codehaus/groovy/groovy-all/2.3.10/groovy-all-2.3.10.jar) to method java.lang.Object.finalize()
WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass$3$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

  .   ____          _            __ _ _
 /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\
( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\
 \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::       (v2.3.12.RELEASE)

2023-11-19 14:11:36.022  INFO 22252 --- [           main] com.lab4.client5.Client5Application      : No active profile set, falling back to default profiles: default
2023-11-19 14:11:36.429  INFO 22252 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=ba1a9484-b05d-3ca5-8a60-ed45abd36bec
2023-11-19 14:11:36.722  INFO 22252 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2023-11-19 14:11:36.729  INFO 22252 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-11-19 14:11:36.730  INFO 22252 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.46]
2023-11-19 14:11:36.814  INFO 22252 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-11-19 14:11:36.814  INFO 22252 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 777 ms
2023-11-19 14:11:37.051  WARN 22252 --- [           main] o.s.c.n.a.ArchaiusAutoConfiguration      : No spring.application.name found, defaulting to 'application'
2023-11-19 14:11:37.053  WARN 22252 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
2023-11-19 14:11:37.053  INFO 22252 --- [           main] c.n.c.sources.URLConfigurationSource     : To enable URLs as dynamic configuration sources, define System property archaius.configurationSource.additionalUrls or make config.properties available on classpath.
2023-11-19 14:11:37.056  WARN 22252 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
2023-11-19 14:11:37.056  INFO 22252 --- [           main] c.n.c.sources.URLConfigurationSource     : To enable URLs as dynamic configuration sources, define System property archaius.configurationSource.additionalUrls or make config.properties available on classpath.
2023-11-19 14:11:37.152  INFO 22252 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2023-11-19 14:11:37.208  INFO 22252 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2023-11-19 14:11:37.322  WARN 22252 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'groovyMarkupConfigurer' defined in class path resource [org/springframework/boot/autoconfigure/groovy/template/GroovyTemplateAutoConfiguration$GroovyMarkupConfiguration.class]: Invocation of init method failed; nested exception is java.lang.reflect.InaccessibleObjectException: Unable to make jdk.internal.loader.ClassLoaders$AppClassLoader(jdk.internal.loader.ClassLoaders$PlatformClassLoader,jdk.internal.loader.URLClassPath) accessible: module java.base does not "opens jdk.internal.loader" to unnamed module @19fb8826
2023-11-19 14:11:37.323  INFO 22252 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
2023-11-19 14:11:37.333  WARN 22252 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
2023-11-19 14:11:37.333  INFO 22252 --- [           main] c.n.c.sources.URLConfigurationSource     : To enable URLs as dynamic configuration sources, define System property archaius.configurationSource.additionalUrls or make config.properties available on classpath.
2023-11-19 14:11:37.336  INFO 22252 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2023-11-19 14:11:37.347  INFO 22252 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2023-11-19 14:11:37.362 ERROR 22252 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'groovyMarkupConfigurer' defined in class path resource [org/springframework/boot/autoconfigure/groovy/template/GroovyTemplateAutoConfiguration$GroovyMarkupConfiguration.class]: Invocation of init method failed; nested exception is java.lang.reflect.InaccessibleObjectException: Unable to make jdk.internal.loader.ClassLoaders$AppClassLoader(jdk.internal.loader.ClassLoaders$PlatformClassLoader,jdk.internal.loader.URLClassPath) accessible: module java.base does not "opens jdk.internal.loader" to unnamed module @19fb8826
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1799) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:594) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:516) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:324) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:322) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:897) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:879) ~[spring-context-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:551) ~[spring-context-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:755) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:747) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:402) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:312) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1247) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1236) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at com.lab4.client5.Client5Application.main(Client5Application.java:12) ~[classes/:na]
Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make jdk.internal.loader.ClassLoaders$AppClassLoader(jdk.internal.loader.ClassLoaders$PlatformClassLoader,jdk.internal.loader.URLClassPath) accessible: module java.base does not "opens jdk.internal.loader" to unnamed module @19fb8826
	at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:340) ~[na:na]
	at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:280) ~[na:na]
	at java.base/java.lang.reflect.Constructor.checkCanSetAccessible(Constructor.java:189) ~[na:na]
	at java.base/java.lang.reflect.Constructor.setAccessible(Constructor.java:182) ~[na:na]
	at org.codehaus.groovy.reflection.CachedConstructor$1.run(CachedConstructor.java:41) ~[groovy-all-2.3.10.jar:2.3.10]
	at java.base/java.security.AccessController.doPrivileged(Native Method) ~[na:na]
	at org.codehaus.groovy.reflection.CachedConstructor.<init>(CachedConstructor.java:39) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.CachedClass$2.initValue(CachedClass.java:75) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.CachedClass$2.initValue(CachedClass.java:65) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.util.LazyReference.getLocked(LazyReference.java:46) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.util.LazyReference.get(LazyReference.java:33) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.CachedClass.getConstructors(CachedClass.java:259) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassImpl.<init>(MetaClassImpl.java:213) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassImpl.<init>(MetaClassImpl.java:223) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassRegistry$MetaClassCreationHandle.createNormalMetaClass(MetaClassRegistry.java:168) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassRegistry$MetaClassCreationHandle.createWithCustomLookup(MetaClassRegistry.java:158) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassRegistry$MetaClassCreationHandle.create(MetaClassRegistry.java:141) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.ClassInfo.getMetaClassUnderLock(ClassInfo.java:221) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.ClassInfo.getMetaClass(ClassInfo.java:253) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.ClassInfo.getMetaClass(ClassInfo.java:263) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl.getMetaClass(MetaClassRegistryImpl.java:259) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.InvokerHelper.getMetaClass(InvokerHelper.java:855) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.InvokerHelper.invokePojoMethod(InvokerHelper.java:888) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.InvokerHelper.invokeMethod(InvokerHelper.java:880) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.castToBoolean(DefaultTypeTransformation.java:180) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.java:69) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.findASTTranformationClass(ASTTransformationCustomizer.groovy:207) ~[groovy-all-2.3.10.jar:2.3.10]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrapNoCoerce.invoke(StaticMetaMethodSite.java:148) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:99) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:53) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:157) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:169) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.findPhase(ASTTransformationCustomizer.groovy:224) ~[groovy-all-2.3.10.jar:2.3.10]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrapNoCoerce.invoke(StaticMetaMethodSite.java:148) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:99) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:53) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:157) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:169) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.<init>(ASTTransformationCustomizer.groovy:178) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.<init>(ASTTransformationCustomizer.groovy:186) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.text.markup.MarkupTemplateEngine.<init>(MarkupTemplateEngine.java:88) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer.createTemplateEngine(GroovyMarkupConfigurer.java:160) ~[spring-webmvc-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer.afterPropertiesSet(GroovyMarkupConfigurer.java:153) ~[spring-webmvc-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1858) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1795) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	... 17 common frames omitted

Process finished with exit code 1
```

在 `application.yml` 中添加了以下语句，但还是报错：

```java
zuul:
  routes:
    foos:
      path: /data/**
      url: <http://localhost:8081/rest-api/data>

spring:
  groovy:
    template:
      enabled: false
      check-template-location: false
```

报错：

```java
"C:\\Program Files\\Microsoft\\jdk-11.0.16.101-hotspot\\bin\\java.exe" -XX:TieredStopAtLevel=1 -noverify -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true "-Dmanagement.endpoints.jmx.exposure.include=*" "-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2.2\\lib\\idea_rt.jar=2169:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2023.2.2\\bin" -Dfile.encoding=UTF-8 -classpath C:\\source\\Lab-big_data\\lab4\\client5\\target\\classes;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-web\\2.3.12.RELEASE\\spring-boot-starter-web-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter\\2.3.12.RELEASE\\spring-boot-starter-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot\\2.3.12.RELEASE\\spring-boot-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-autoconfigure\\2.3.12.RELEASE\\spring-boot-autoconfigure-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-logging\\2.3.12.RELEASE\\spring-boot-starter-logging-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\ch\\qos\\logback\\logback-classic\\1.2.3\\logback-classic-1.2.3.jar;C:\\apache-maven-3.9.4\\ch\\qos\\logback\\logback-core\\1.2.3\\logback-core-1.2.3.jar;C:\\apache-maven-3.9.4\\org\\apache\\logging\\log4j\\log4j-to-slf4j\\2.13.3\\log4j-to-slf4j-2.13.3.jar;C:\\apache-maven-3.9.4\\org\\apache\\logging\\log4j\\log4j-api\\2.13.3\\log4j-api-2.13.3.jar;C:\\apache-maven-3.9.4\\org\\slf4j\\jul-to-slf4j\\1.7.30\\jul-to-slf4j-1.7.30.jar;C:\\apache-maven-3.9.4\\jakarta\\annotation\\jakarta.annotation-api\\1.3.5\\jakarta.annotation-api-1.3.5.jar;C:\\apache-maven-3.9.4\\org\\yaml\\snakeyaml\\1.26\\snakeyaml-1.26.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-json\\2.3.12.RELEASE\\spring-boot-starter-json-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\core\\jackson-databind\\2.11.4\\jackson-databind-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\core\\jackson-annotations\\2.11.4\\jackson-annotations-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\core\\jackson-core\\2.11.4\\jackson-core-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\datatype\\jackson-datatype-jdk8\\2.11.4\\jackson-datatype-jdk8-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\datatype\\jackson-datatype-jsr310\\2.11.4\\jackson-datatype-jsr310-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\module\\jackson-module-parameter-names\\2.11.4\\jackson-module-parameter-names-2.11.4.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-tomcat\\2.3.12.RELEASE\\spring-boot-starter-tomcat-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\apache\\tomcat\\embed\\tomcat-embed-core\\9.0.46\\tomcat-embed-core-9.0.46.jar;C:\\apache-maven-3.9.4\\org\\glassfish\\jakarta.el\\3.0.3\\jakarta.el-3.0.3.jar;C:\\apache-maven-3.9.4\\org\\apache\\tomcat\\embed\\tomcat-embed-websocket\\9.0.46\\tomcat-embed-websocket-9.0.46.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-web\\5.2.15.RELEASE\\spring-web-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-beans\\5.2.15.RELEASE\\spring-beans-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-webmvc\\5.2.15.RELEASE\\spring-webmvc-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-aop\\5.2.15.RELEASE\\spring-aop-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-context\\5.2.15.RELEASE\\spring-context-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-expression\\5.2.15.RELEASE\\spring-expression-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-core\\5.2.15.RELEASE\\spring-core-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\spring-jcl\\5.2.15.RELEASE\\spring-jcl-5.2.15.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-zuul\\2.2.10.RELEASE\\spring-cloud-netflix-zuul-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-hystrix\\2.2.10.RELEASE\\spring-cloud-netflix-hystrix-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\boot\\spring-boot-starter-aop\\2.3.12.RELEASE\\spring-boot-starter-aop-2.3.12.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\aspectj\\aspectjweaver\\1.9.6\\aspectjweaver-1.9.6.jar;C:\\apache-maven-3.9.4\\org\\apache\\httpcomponents\\httpclient\\4.5.13\\httpclient-4.5.13.jar;C:\\apache-maven-3.9.4\\org\\apache\\httpcomponents\\httpcore\\4.4.14\\httpcore-4.4.14.jar;C:\\apache-maven-3.9.4\\commons-codec\\commons-codec\\1.14\\commons-codec-1.14.jar;C:\\apache-maven-3.9.4\\com\\netflix\\netflix-commons\\netflix-commons-util\\0.3.0\\netflix-commons-util-0.3.0.jar;C:\\apache-maven-3.9.4\\javax\\inject\\javax.inject\\1\\javax.inject-1.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-starter-netflix-hystrix\\2.2.10.RELEASE\\spring-cloud-starter-netflix-hystrix-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-starter\\2.2.9.RELEASE\\spring-cloud-starter-2.2.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-context\\2.2.9.RELEASE\\spring-cloud-context-2.2.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\security\\spring-security-crypto\\5.3.9.RELEASE\\spring-security-crypto-5.3.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-commons\\2.2.9.RELEASE\\spring-cloud-commons-2.2.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\security\\spring-security-rsa\\1.0.9.RELEASE\\spring-security-rsa-1.0.9.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\bouncycastle\\bcpkix-jdk15on\\1.64\\bcpkix-jdk15on-1.64.jar;C:\\apache-maven-3.9.4\\org\\bouncycastle\\bcprov-jdk15on\\1.64\\bcprov-jdk15on-1.64.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-ribbon\\2.2.10.RELEASE\\spring-cloud-netflix-ribbon-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-netflix-archaius\\2.2.10.RELEASE\\spring-cloud-netflix-archaius-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\org\\springframework\\cloud\\spring-cloud-starter-netflix-archaius\\2.2.10.RELEASE\\spring-cloud-starter-netflix-archaius-2.2.10.RELEASE.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-core\\1.5.18\\hystrix-core-1.5.18.jar;C:\\apache-maven-3.9.4\\io\\reactivex\\rxjava\\1.3.8\\rxjava-1.3.8.jar;C:\\apache-maven-3.9.4\\org\\hdrhistogram\\HdrHistogram\\2.1.9\\HdrHistogram-2.1.9.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-serialization\\1.5.18\\hystrix-serialization-1.5.18.jar;C:\\apache-maven-3.9.4\\com\\fasterxml\\jackson\\module\\jackson-module-afterburner\\2.11.4\\jackson-module-afterburner-2.11.4.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-metrics-event-stream\\1.5.18\\hystrix-metrics-event-stream-1.5.18.jar;C:\\apache-maven-3.9.4\\com\\netflix\\hystrix\\hystrix-javanica\\1.5.18\\hystrix-javanica-1.5.18.jar;C:\\apache-maven-3.9.4\\org\\apache\\commons\\commons-lang3\\3.10\\commons-lang3-3.10.jar;C:\\apache-maven-3.9.4\\org\\ow2\\asm\\asm\\5.0.4\\asm-5.0.4.jar;C:\\apache-maven-3.9.4\\io\\reactivex\\rxjava-reactive-streams\\1.2.1\\rxjava-reactive-streams-1.2.1.jar;C:\\apache-maven-3.9.4\\org\\reactivestreams\\reactive-streams\\1.0.3\\reactive-streams-1.0.3.jar;C:\\apache-maven-3.9.4\\com\\netflix\\zuul\\zuul-core\\1.3.1\\zuul-core-1.3.1.jar;C:\\apache-maven-3.9.4\\commons-io\\commons-io\\2.4\\commons-io-2.4.jar;C:\\apache-maven-3.9.4\\org\\codehaus\\groovy\\groovy-all\\2.3.10\\groovy-all-2.3.10.jar;C:\\apache-maven-3.9.4\\org\\mockito\\mockito-all\\1.9.5\\mockito-all-1.9.5.jar;C:\\apache-maven-3.9.4\\org\\slf4j\\slf4j-api\\1.7.30\\slf4j-api-1.7.30.jar;C:\\apache-maven-3.9.4\\com\\netflix\\archaius\\archaius-core\\0.6.0\\archaius-core-0.6.0.jar;C:\\apache-maven-3.9.4\\com\\google\\guava\\guava\\11.0.2\\guava-11.0.2.jar;C:\\apache-maven-3.9.4\\com\\google\\code\\findbugs\\jsr305\\1.3.9\\jsr305-1.3.9.jar;C:\\apache-maven-3.9.4\\org\\codehaus\\jackson\\jackson-mapper-asl\\1.9.11\\jackson-mapper-asl-1.9.11.jar;C:\\apache-maven-3.9.4\\commons-configuration\\commons-configuration\\1.8\\commons-configuration-1.8.jar;C:\\apache-maven-3.9.4\\commons-lang\\commons-lang\\2.6\\commons-lang-2.6.jar;C:\\apache-maven-3.9.4\\commons-logging\\commons-logging\\1.1.1\\commons-logging-1.1.1.jar;C:\\apache-maven-3.9.4\\org\\codehaus\\jackson\\jackson-core-asl\\1.9.11\\jackson-core-asl-1.9.11.jar;C:\\apache-maven-3.9.4\\com\\google\\code\\findbugs\\annotations\\2.0.0\\annotations-2.0.0.jar;C:\\apache-maven-3.9.4\\com\\netflix\\servo\\servo-core\\0.7.2\\servo-core-0.7.2.jar com.lab4.client5.Client5Application
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.codehaus.groovy.reflection.CachedClass$3$1 (file:/C:/apache-maven-3.9.4/org/codehaus/groovy/groovy-all/2.3.10/groovy-all-2.3.10.jar) to method java.lang.Object.finalize()
WARNING: Please consider reporting this to the maintainers of org.codehaus.groovy.reflection.CachedClass$3$1
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

  .   ____          _            __ _ _
 /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\
( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\
 \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::       (v2.3.12.RELEASE)

2023-11-19 14:20:45.281  INFO 5312 --- [           main] com.lab4.client5.Client5Application      : No active profile set, falling back to default profiles: default
2023-11-19 14:20:45.688  INFO 5312 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=16afe799-b1a9-3e09-967a-882ee411b230
2023-11-19 14:20:45.987  INFO 5312 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2023-11-19 14:20:45.996  INFO 5312 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-11-19 14:20:45.996  INFO 5312 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.46]
2023-11-19 14:20:46.093  INFO 5312 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-11-19 14:20:46.093  INFO 5312 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 796 ms
2023-11-19 14:20:46.360  WARN 5312 --- [           main] o.s.c.n.a.ArchaiusAutoConfiguration      : No spring.application.name found, defaulting to 'application'
2023-11-19 14:20:46.362  WARN 5312 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
2023-11-19 14:20:46.362  INFO 5312 --- [           main] c.n.c.sources.URLConfigurationSource     : To enable URLs as dynamic configuration sources, define System property archaius.configurationSource.additionalUrls or make config.properties available on classpath.
2023-11-19 14:20:46.365  WARN 5312 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
2023-11-19 14:20:46.365  INFO 5312 --- [           main] c.n.c.sources.URLConfigurationSource     : To enable URLs as dynamic configuration sources, define System property archaius.configurationSource.additionalUrls or make config.properties available on classpath.
2023-11-19 14:20:46.464  INFO 5312 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2023-11-19 14:20:46.529  INFO 5312 --- [           main] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [static/index.html]
2023-11-19 14:20:46.656  WARN 5312 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'groovyMarkupConfigurer' defined in class path resource [org/springframework/boot/autoconfigure/groovy/template/GroovyTemplateAutoConfiguration$GroovyMarkupConfiguration.class]: Invocation of init method failed; nested exception is java.lang.reflect.InaccessibleObjectException: Unable to make jdk.internal.loader.ClassLoaders$AppClassLoader(jdk.internal.loader.ClassLoaders$PlatformClassLoader,jdk.internal.loader.URLClassPath) accessible: module java.base does not "opens jdk.internal.loader" to unnamed module @19fb8826
2023-11-19 14:20:46.657  INFO 5312 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
2023-11-19 14:20:46.667  WARN 5312 --- [           main] c.n.c.sources.URLConfigurationSource     : No URLs will be polled as dynamic configuration sources.
2023-11-19 14:20:46.668  INFO 5312 --- [           main] c.n.c.sources.URLConfigurationSource     : To enable URLs as dynamic configuration sources, define System property archaius.configurationSource.additionalUrls or make config.properties available on classpath.
2023-11-19 14:20:46.671  INFO 5312 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2023-11-19 14:20:46.683  INFO 5312 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2023-11-19 14:20:46.701 ERROR 5312 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'groovyMarkupConfigurer' defined in class path resource [org/springframework/boot/autoconfigure/groovy/template/GroovyTemplateAutoConfiguration$GroovyMarkupConfiguration.class]: Invocation of init method failed; nested exception is java.lang.reflect.InaccessibleObjectException: Unable to make jdk.internal.loader.ClassLoaders$AppClassLoader(jdk.internal.loader.ClassLoaders$PlatformClassLoader,jdk.internal.loader.URLClassPath) accessible: module java.base does not "opens jdk.internal.loader" to unnamed module @19fb8826
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1799) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:594) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:516) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:324) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:322) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:897) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:879) ~[spring-context-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:551) ~[spring-context-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:755) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:747) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:402) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:312) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1247) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1236) ~[spring-boot-2.3.12.RELEASE.jar:2.3.12.RELEASE]
	at com.lab4.client5.Client5Application.main(Client5Application.java:12) ~[classes/:na]
Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make jdk.internal.loader.ClassLoaders$AppClassLoader(jdk.internal.loader.ClassLoaders$PlatformClassLoader,jdk.internal.loader.URLClassPath) accessible: module java.base does not "opens jdk.internal.loader" to unnamed module @19fb8826
	at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:340) ~[na:na]
	at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:280) ~[na:na]
	at java.base/java.lang.reflect.Constructor.checkCanSetAccessible(Constructor.java:189) ~[na:na]
	at java.base/java.lang.reflect.Constructor.setAccessible(Constructor.java:182) ~[na:na]
	at org.codehaus.groovy.reflection.CachedConstructor$1.run(CachedConstructor.java:41) ~[groovy-all-2.3.10.jar:2.3.10]
	at java.base/java.security.AccessController.doPrivileged(Native Method) ~[na:na]
	at org.codehaus.groovy.reflection.CachedConstructor.<init>(CachedConstructor.java:39) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.CachedClass$2.initValue(CachedClass.java:75) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.CachedClass$2.initValue(CachedClass.java:65) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.util.LazyReference.getLocked(LazyReference.java:46) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.util.LazyReference.get(LazyReference.java:33) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.CachedClass.getConstructors(CachedClass.java:259) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassImpl.<init>(MetaClassImpl.java:213) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassImpl.<init>(MetaClassImpl.java:223) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassRegistry$MetaClassCreationHandle.createNormalMetaClass(MetaClassRegistry.java:168) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassRegistry$MetaClassCreationHandle.createWithCustomLookup(MetaClassRegistry.java:158) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.lang.MetaClassRegistry$MetaClassCreationHandle.create(MetaClassRegistry.java:141) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.ClassInfo.getMetaClassUnderLock(ClassInfo.java:221) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.ClassInfo.getMetaClass(ClassInfo.java:253) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.reflection.ClassInfo.getMetaClass(ClassInfo.java:263) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl.getMetaClass(MetaClassRegistryImpl.java:259) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.InvokerHelper.getMetaClass(InvokerHelper.java:855) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.InvokerHelper.invokePojoMethod(InvokerHelper.java:888) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.InvokerHelper.invokeMethod(InvokerHelper.java:880) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.castToBoolean(DefaultTypeTransformation.java:180) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.java:69) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.findASTTranformationClass(ASTTransformationCustomizer.groovy:207) ~[groovy-all-2.3.10.jar:2.3.10]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrapNoCoerce.invoke(StaticMetaMethodSite.java:148) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:99) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:53) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:157) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:169) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.findPhase(ASTTransformationCustomizer.groovy:224) ~[groovy-all-2.3.10.jar:2.3.10]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:566) ~[na:na]
	at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite$StaticMetaMethodSiteNoUnwrapNoCoerce.invoke(StaticMetaMethodSite.java:148) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.StaticMetaMethodSite.callStatic(StaticMetaMethodSite.java:99) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.CallSiteArray.defaultCallStatic(CallSiteArray.java:53) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:157) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.runtime.callsite.AbstractCallSite.callStatic(AbstractCallSite.java:169) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.<init>(ASTTransformationCustomizer.groovy:178) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.codehaus.groovy.control.customizers.ASTTransformationCustomizer.<init>(ASTTransformationCustomizer.groovy:186) ~[groovy-all-2.3.10.jar:2.3.10]
	at groovy.text.markup.MarkupTemplateEngine.<init>(MarkupTemplateEngine.java:88) ~[groovy-all-2.3.10.jar:2.3.10]
	at org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer.createTemplateEngine(GroovyMarkupConfigurer.java:160) ~[spring-webmvc-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer.afterPropertiesSet(GroovyMarkupConfigurer.java:153) ~[spring-webmvc-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1858) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1795) ~[spring-beans-5.2.15.RELEASE.jar:5.2.15.RELEASE]
	... 17 common frames omitted

Process finished with exit code 1
```

后来摆烂了，copy了同学调试好的 `pom.xml` 文件。结果竟然神奇地可以运行了

```bash
.   ____          _            __ _ _
 /\\\\ / ___'_ __ _ _(_)_ __  __ _ \\ \\ \\ \\
( ( )\\___ | '_ | '_| | '_ \\/ _` | \\ \\ \\ \\
 \\\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.5)

2023-11-19T23:17:16.939+03:00  INFO 18828 --- [           main] com.lab4.server.ServerApplication        : Starting ServerApplication using Java 17.0.8 with PID 18828 (C:\\source\\Lab-big_data\\lab4\\server\\target\\classes started by wsyconan in C:\\source\\Lab-big_data\\lab4\\server)
2023-11-19T23:17:16.942+03:00  INFO 18828 --- [           main] com.lab4.server.ServerApplication        : No active profile set, falling back to 1 default profile: "default"
2023-11-19T23:17:17.703+03:00  INFO 18828 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8081 (http)
2023-11-19T23:17:17.711+03:00  INFO 18828 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-11-19T23:17:17.711+03:00  INFO 18828 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.15]
2023-11-19T23:17:17.785+03:00  INFO 18828 --- [           main] o.a.c.c.C.[.[localhost].[/rest-api]      : Initializing Spring embedded WebApplicationContext
2023-11-19T23:17:17.785+03:00  INFO 18828 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 787 ms
2023-11-19T23:17:18.075+03:00  INFO 18828 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8081 (http) with context path '/rest-api'
2023-11-19T23:17:18.083+03:00  INFO 18828 --- [           main] com.lab4.server.ServerApplication        : Started ServerApplication in 1.524 seconds (process running for 1.973)
2023-11-19T23:17:36.743+03:00  INFO 18828 --- [nio-8081-exec-1] o.a.c.c.C.[.[localhost].[/rest-api]      : Initializing Spring DispatcherServlet 'dispatcherServlet'
2023-11-19T23:17:36.744+03:00  INFO 18828 --- [nio-8081-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2023-11-19T23:17:36.744+03:00  INFO 18828 --- [nio-8081-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 0 ms
```

思考了一下原因，可能是maven依赖错综复杂。直接copy人家已经能跑的 `pom.xml` 的确是解决问题最直接的办法。


最后我们来验证整个实验是否成功。
访问： http://localhost:8080 ，回车
发现页面可以加载出来：
![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311200002442.png)
先打开“浏览器开发者工具”，然后来到“Netword”标签下。此时还没有发送的请求和响应。
点一下页面上的“Get Data From Server”，发现ID和Name发生了变化，说明已经访问到了服务器并接收到返回的响应。
最后确实，在响应头中看到了我们在网关程序中添加的`My-Test-Header`。这就说明网关程序帮我们添加的HTTP头部字段`My-Test-Header` 被正确地转发到了服务器的端口上。实验成功！


### 4.1 实验中遇到的坑

1. 在 Google 浏览器的开发者工具中，在 `Response Headers` 里面始终看不到`my_test_header` 。但是换了火狐浏览器之后，就能看到了，说明代码没有写错
    
2. 再次点击 `Get Data From Server` 的时候有可能会发现*HTTP 304状态码。**
	![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311200004394.png)


    这不是一种错误，而是对客户端有缓存情况下服务端的一种响应。
    
    > **整个请求响应过程如下：**
    > 
    > 客户端在请求一个文件的时候，发现自己缓存的文件有 Last Modified ，那么在请求中会包含 If Modified Since ，这个时间就是缓存文件的 Last Modified 。因此，如果请求中包含 If Modified Since，就说明已经有缓存在客户端。服务端只要判断这个时间和当前请求的文件的修改时间就可以确定是返回 304 还是 200 。
    > 
    > 对于静态文件，例如：CSS、图片，服务器会自动完成 Last Modified 和 If Modified Since 的比较，完成缓存或者更新。但是对于动态页面，就是动态产生的页面，往往没有包含 Last Modified 信息，这样浏览器、网关等都不会做缓存，也就是在每次请求的时候都完成一个 200 的请求。
    > 
    > 因此，对于动态页面做缓存加速，首先要在 Response 的 HTTP Header 中增加 Last Modified 定义，其次根据 Request 中的 If Modified Since 和被请求内容的更新时间来返回 200 或者 304 。虽然在返回 304 的时候已经做了一次数据库查询，但是可以避免接下来更多的数据库查询，并且没有返回页面内容而只是一个 HTTP Header，从而大大的降低带宽的消耗，对于用户的感觉也是提高。当这些缓存有效的时候，通过 Fiddler 或HttpWatch 查看一个请求会得到这样的结果：
    > 
    > 第一次访问 200
    > 
    > 按F5刷新（第二次访问） 304
    > 
    > 按Ctrl+F5强制刷新 200 参考：[https://blog.csdn.net/testcs_dn/article/details/84833920/](https://blog.csdn.net/testcs_dn/article/details/84833920/)
    
    此时，只要按Ctrl+F5强制刷新，就能看到200状态啦！
    ![image.png](https://raw.githubusercontent.com/liyijiadou2020/picrepo/master/202311200003720.png)




