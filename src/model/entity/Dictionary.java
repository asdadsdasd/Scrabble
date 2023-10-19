package model.entity;
import java.io.*;
import java.util.*;

public class Dictionary {

    private static List<String> listOfWords = new ArrayList<>();

    private static Dictionary dictionary = new Dictionary();

    private Dictionary() {
        FileReader fr;
        try {
            fr = new FileReader("\\Users\\Reversi\\java_projects\\buysell\\scrabble\\src\\resources\\russian.txt");
            Scanner scanner = new Scanner(fr);
            while (scanner.hasNextLine()){
                listOfWords.add(scanner.nextLine());
            }
            fr.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dictionary getDictionary(){
        if(dictionary == null){
            dictionary = new Dictionary();
        }
        return dictionary;
    }

    public static List<String> getListOfWords(){
        return Collections.unmodifiableList(listOfWords);
    }

    public static void addWord(String word){
        FileWriter fw;
        try {
            fw = new FileWriter(".\\src\\resources\\russian.txt", true);
            fw.write("\n" + word);
            listOfWords.add(word);
            fw.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isContainsWord(String word){
        return listOfWords.contains(word);
    }

    public static String getRandomWord(int len) {
        Random random = new Random();
        String word = listOfWords.get(random.nextInt(listOfWords.size()));
        while(word.length() != len){
            word = listOfWords.get(random.nextInt(listOfWords.size()));
        }
        return word;
    }
}
