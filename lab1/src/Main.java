import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import java.io.*;


// обфускирование
public class Main {

    public static void main(String[] args) {

        try {
            System.out.println("[Args number] " + args.length);
            for (String s : args) {
                System.out.println("[Mode] " + s + "\t");
            }

            String password = "46EBA22EF5204DD5B110A1F730513965"; //
            SAXReader saxReader = new SAXReader();

            // Режим 1. obfuscate xml file: Encrypting attribute values and node content using the AES algorithm
            if (args.length > 0 && args[0].equals("obf")) { // параметр запуска
                Obfuscatror obfuscatror1 = new Obfuscatror(password);

                Document document = saxReader.read(new File("employees.xml"));
                String obfuscated = "obfuscated.xml";
                obfuscatror1.obfuscate(document); //
                System.out.println("> Finished obfuscation in " + obfuscated);
                obfuscatror1.writeXml(document, obfuscated);

            // Режим 2. deobfuscate xml file:
            } else if (args.length > 0 && args[0].equals("deobf")) {
                Obfuscatror obfuscatror2 = new Obfuscatror(password);

                Document document2 = saxReader.read(new File("obfuscated.xml"));
                String deobfuscated = "deobfuscated.xml";
                obfuscatror2.deobfuscate(document2);
                obfuscatror2.writeXml(document2, deobfuscated);
                System.out.println("> Finished deobfuscation in " + deobfuscated);
            } else {
                System.out.println("[ERROR!] Invalid mode specified. Please provide either 'obf' or 'deobf' as a command-line argument.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}