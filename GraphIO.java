import java.io.*;
import java.util.*;

public class GraphIO {

    public static void main(String[] args) {

        System.out.println("GIT");

        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("@Potato", 10);
        map.put("@Poop", 2);
        map.put("@Tahoe", 69);

        int loop = 1;

        while (loop == 1) {
            Scanner scn = new Scanner(System.in);
            System.out.println("1 to write to text file OR 2 to read from text file: ");
            if (scn.nextLine().equals("1")) {
                writeToFile(map);
            }
            else if (scn.nextLine().equals("2")) {
                writeToHashMapMain();
                loop = 0;
            }
            else {
                System.out.println("PLEASE SELECT 1 OR 2");
            }
        }
    }


    public static void writeToFile(HashMap<String, Integer> map) {
        String path = fileName(1);
        File graph = new File(path);
        BufferedWriter bf = null;

        try {
            System.out.println("try");
            bf = new BufferedWriter(new FileWriter(graph));
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                bf.write(entry.getKey() + ":" + entry.getValue());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch (Exception e) {
            }
        }
    }

    public static void writeToHashMapMain() {
        Map<String, Integer> mapFromTxtFile = writeToHashMap();

        for (Map.Entry<String, Integer> entry : mapFromTxtFile.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static Map<String, Integer> writeToHashMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        BufferedReader br = null;
        String path = fileName(2);

        try {
            File file = new File(path);
            br = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String user = parts[0].trim();
                String numRetweetsStr = parts[1].trim();
                int numRetweets = Integer.parseInt(numRetweetsStr);

                if (!user.equals("")) {
                    map.put(user, numRetweets);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
                ;
            }
        }
        return map;
    }


    // int passed in so that the code can differentiate between creating a new
    // file and loading a file
    public static String fileName(int rW) {
        int readOrWrite = rW;
        String name = "C:/Users/owenc/OneDrive/UCD Year 3 Sem 2/SWE Project/Git/SoftwareGroupProject/Graphs/"; //CHANGE THIS PATH AS NEEDED. MUST BE EXACT.

        if (readOrWrite == 1) {
            boolean exists = true;
            while (exists) {
                System.out.println("write file");
                Scanner scn = new Scanner(System.in);
                System.out.println("ENTER FILE NAME:");
                name = name + scn.nextLine() + ".txt";
                File f = new File(name);
                if (f.exists()) {
                    exists = true;
                    System.out.println("FILE ALREADY EXISTS");
                } 
                else {
                    exists = false;
                }
            }
        }
        else {
            boolean exists = false;
            while (!exists) {
                System.out.println("read file");
                Scanner scn = new Scanner(System.in);
                System.out.println("ENTER FILE NAME:");
                name = name + scn.nextLine() + ".txt";
                System.out.println(name);
                File f = new File(name);
                if (f.exists()) {
                    exists = true;
                } else {
                    exists = false;
                    System.out.println("FILE DOES NOT EXIST");
                }
            }
        }
        return name;
    }
}