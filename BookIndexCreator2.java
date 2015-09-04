package homework3;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookIndexCreator2 {
    private static ArrayList<String> rangeString = new ArrayList<String>();
    
    public static void main(String[] args) throws IOException{
        File file = new File("C:/Users/Anish/Desktop/test2.txt");
        MyBST<String,String> b = new MyBST<>();
        LinkedList<String> vals = readFile(file);
        //String[] o = vals.toArray();
        /*for(String s: vals){
            System.out.println(s);
        }*/
        
        for(int i=0; i<vals.size()-1; i+=2){
            //System.out.print("{"+vals.get(i)+", "+vals.get(i+1)+"}, ");
            if(b.getValueFromKey(vals.get(i)) == null)
                b.insert(vals.get(i), vals.get(i+1));
            else{
                String insert_data = vals.get(i+1);
                String tree_data = b.getValueFromKey(vals.get(i));
                //if(vals.get(i).contains("!")){
                if(!tree_data.equals(insert_data)){
                    if(rangeString.contains(vals.get(i))){
                        b.update(vals.get(i), (tree_data+"-"+insert_data));
                    }else{
                        b.update(vals.get(i), (tree_data+","+insert_data));
                    }
                     
                }
                //}else if(!vals.get(i).contains("!")){
                //b.update(vals.get(i), (tree_data+"-"+insert_data)); 
                //}     
                
            }
        }
        
        java.util.List<String> inO2 = b.inOrder();
        //System.out.println("");
        for(String s: inO2){
            //System.out.print("{"+s+", "+b.getValueFromKey(s)+"} ");
            String output = "";
            if(!s.contains("!")){
                output = s + ": " + b.getValueFromKey(s);
            }else{
                String s2 = ""; 
                StringBuilder space = new StringBuilder("");
                String[] parts = s.split("!");
                s2 = parts[parts.length-1]; 
                for(int i=0; i<parts.length-1; i++){
                    space.append("    ");
                }
                output = space + s2 + ": " + b.getValueFromKey(s);
            }
            System.out.println(output);
        }
        
        /*
         * Save the page numbers as strings and save those as the data.
         * When you see a repeated string, update the value at that node to
         * include that string. 
         * Make sure you consider the case of duplicates. 
         */
    }
    
    public static LinkedList<String> readFile(File file) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        //Pattern pattern = Pattern.compile("\\{(.*?)([|()]{2})?\\}");
        //Pattern pattern = Pattern.compile("\\{([^\\}\\|]*)(\\|\\()?\\}");
        Matcher matcher;
        LinkedList<String> list = new LinkedList<String>();
        while((line = br.readLine()) != null){
            
            matcher = pattern.matcher(line);
            while(matcher.find()){
                if(matcher.group(1).contains("|")){
                    rangeString.add(matcher.group(1).substring(0, matcher.group(1).length()-2));
                    list.add(matcher.group(1).substring(0, matcher.group(1).length()-2));
                }else{
                    list.add(matcher.group(1));
                }
            }
        }
        br.close();
        return list;
    }

    
}

