package spell;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    Trie dictionary = new Trie();
    //public SpellCorrect

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner scanner = new Scanner(new File(dictionaryFileName));
        scanner.useDelimiter("(\\s+)+");
        while(scanner.hasNext()) {
            dictionary.add(scanner.next()); //This will read in the words and add to Trie
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();
        if (dictionary.find(inputWord) != null) { return inputWord; }
        TreeSet<String> edit1 = new TreeSet<>(), edit2 = new TreeSet<>(), temp = new TreeSet<>();
        edit1.add(inputWord);
        temp = getCandidates(edit1);
        edit1 = findCandidates(temp);
        if (edit1.size() == 1) { return edit1.first(); }
        else if (edit1.size() > 1) {
            return multiCandidates(edit1).first();
        }
        else {
            edit2 = getCandidates(temp);
            edit2 = findCandidates(edit2);
            if (edit2.size() == 1) { return edit2.first(); }
            else if (edit2.size() > 1) {
                return multiCandidates(edit2).first();
            }
        }
        return null;
    }


    //getcandidates
    public TreeSet<String> getCandidates(TreeSet<String> input) {
        TreeSet<String> temp = new TreeSet<>();
        for (String word : input) { //repeat for every word in the input
           //deletion
            for (int i = 0; i < word.length(); i++) {
                StringBuilder sb = new StringBuilder(word);
                sb.deleteCharAt(i);
                temp.add(sb.toString());
            }

            //transposition
            for (int i = 0; i < word.length()-1; i++) {
                char[] c = word.toCharArray();
                char tempChar = c[i];
                c[i] = c[i+1];
                c[i+1] = tempChar;
                String result = new String(c);
                temp.add(result);
            }

           //alteration
            for (int i = 0; i < word.length(); i++) {
                for (int j = 0; j < 26; j++) {
                    StringBuilder sb = new StringBuilder(word);
                    sb.setCharAt(i,  (char) (j + 'a'));
                    temp.add(sb.toString());
                }
            }

            //insertion
            for (int i = 0; i < word.length(); i++) {
                for (int j = 0; j < 26; j++) {
                    StringBuilder sb = new StringBuilder(word);
                    sb.insert(i,  (char) (j + 'a'));
                    temp.add(sb.toString());
                }
            }
            for (int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(word);
                sb.append((char) (j + 'a'));
                temp.add(sb.toString());
            }

        }
        return temp;
    }


    //multicandidates - if there is more than one candidate found
    public TreeSet<String> multiCandidates(TreeSet<String> input) {
        TreeSet<String> temp = new TreeSet<>();

        int count = 0, maxCount = 0;
        for (String word : input) {
            count = dictionary.find(word).getValue();
            if (count > maxCount) {
                temp.clear();
                temp.add(word);
                maxCount = count;
            }
            else if (count == maxCount) {
                temp.add(word);
            }
        }
        return temp;
    }

    //findcandidates
    public TreeSet<String> findCandidates(TreeSet<String> input) {
        TreeSet<String> temp = new TreeSet<>();
        for (String word : input) {
            if (dictionary.find(word) != null) {
                temp.add(word);
            }
        }
        return temp;
    }
}
