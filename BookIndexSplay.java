import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to make a book index with a splay tree.
 * @author Anish
 *
 */
public final class BookIndexSplay {
    
    /**
     * ArrayList that contains keys that have ranges. This is useful in order to
     * see whether a key needs to have a '-', ',', or neither.
     */
    private static ArrayList<String> rangeString = new ArrayList<String>();
    
    /**
     * Empty constructor in order to avoid extension
     * of this class.
     */
    private BookIndexSplay() {
        
    }
    
    /**
     * 
     * @param args
         command line arguments that would contain the filepath for the file
         to be read.
     * @throws IOException
         throws an input output exception.
     */
    public static void main(String[] args) throws IOException {
        // File file = new File("C:/Users/Anish/Desktop/test2.txt");
        long start = System.nanoTime();
        MySplayTree<String, String> b = new MySplayTree<>();
        File file = new File(args[0]);
        LinkedList<String> vals = readFile(file);

        for (int i = 0; i < vals.size() - 1; i += 2) {
           
            if (b.getValueFromKey(vals.get(i)) == null) {
                b.insert(vals.get(i), vals.get(i + 1));
            } else {
                String insertData = vals.get(i + 1);
                String tInsertData = insertData.trim();
                String treeData = b.getValueFromKey(vals.get(i));
                
                if (!treeData.equals(insertData)) {

                    if (treeData.contains("-") 
                            && !treeData.equals(tInsertData + "-")) {
                            //&& !treeData.contains(insertData)) {
                        b.update(vals.get(i), (treeData + insertData));
                        
                        
                    } else if (treeData.contains("-") 
                           && treeData.equals(tInsertData + "-")) {
                        b.update(vals.get(i), 
                            (treeData.substring(0, treeData.length() - 1)));
                    } else {
                        b.update(vals.get(i), (treeData + "," + insertData));
                    }

                }
               
            }
        }

        writeFile(file, b);
        long elapsedTime = System.nanoTime() - start;
        System.out.println("Elapsed Time: " + elapsedTime);
    }
    
    /**
     * Writes to a file.
     * @param file
         file to be written to.
     * @param b
         binary search tree that contains data.
     * @throws IOException
         throws an IOexception
     */
    public static void writeFile(File file, MySplayTree<String, String> b) 
        throws IOException {
        java.util.List<String> inO2 = b.inOrder();
        
        BufferedWriter bw = new BufferedWriter(new 
                FileWriter(file.getName() + ".index"));
        for (String s : inO2) {
         
            String output = "";
            if (!s.contains("!")) {
                output = s + ": " + b.getValueFromKey(s);
            } else {
                String s2 = "";
                StringBuilder space = new StringBuilder("");
                String[] parts = s.split("!");
                s2 = parts[parts.length - 1];
                for (int i = 0; i < parts.length - 1; i++) {
                    space.append("    ");
                }
                output = space + s2 + ": " + b.getValueFromKey(s);
            }
            System.out.println(output);
            bw.write(output);
            bw.newLine();
        }
        bw.close();
    }
    
    /**
     * Reads the file and saves the relevant strings to a list.
     * @param file
             filename of the file to read
     * @return
             list that contains the parsed strings to be stored in the tree
     * @throws IOException
             throws an input output exception when reading the file.
     */
    public static LinkedList<String> readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        // Pattern pattern = Pattern.compile("\\{(.*?)([|()]{2})?\\}");
        // Pattern pattern = Pattern.compile("\\{([^\\}\\|]*)(\\|\\()?\\}");
        Matcher matcher;
        LinkedList<String> list = new LinkedList<String>();
        ArrayList<String> al = new ArrayList<String>();

        while ((line = br.readLine()) != null) {
            String mod = "";
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                /*if (matcher.group(1).contains("|")) { 
                    //rangeString.add(matcher.group(1).substring(0,
                            //matcher.group(1).length() - 2));
                    list.add(matcher.group(1).substring(0,
                            matcher.group(1).length() - 2));  
                } else {
                    list.add(matcher.group(1));
                }*/
                al.add(matcher.group(1));
                
            }
            if (al.get(0).contains("|(")) {
                //al.set(1, (al.get(1) + "-"));
                list.add(al.get(0).substring(0,
                        al.get(0).length() - 2)); 
                list.add(al.get(1) + "-");
            } else {
                if (al.get(0).contains("|)")) {
                    list.add(al.get(0).substring(0, al.get(0).length() - 2));
                    list.add(al.get(1) + " ");
                } else {
                    list.add(al.get(0));
                    list.add(al.get(1));
                }
                //list.add(al.get(1));
            }
            al.clear();
        }
        br.close();
        /*for(String i: list){
            System.out.println(i);
        }*/
        return list;
    }

}

