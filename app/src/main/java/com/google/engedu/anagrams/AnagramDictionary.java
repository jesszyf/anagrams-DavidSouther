/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.engedu.anagrams;

import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    // keys is the corted, arraylist is all words after sorting = key
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();

    private List<String> masterList = new ArrayList<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null)
        {
            String word = line.trim();

            masterList.add(word);

            // key
            String temp = sortLetters(word);
            // if not contain, create the new list
            if(!lettersToWord.containsKey(temp))
            {
                lettersToWord.put(temp, new ArrayList<String>());
            }
            lettersToWord.get(temp).add(word);



        }
    }

    // word and base are anagrams but not same
    public boolean isGoodWord(String word, String base)
    {

        return getAnagramsWithOneMoreLetter(base).contains(word);
    }

    public List<String> getAnagrams(String targetWord)
    {
        ArrayList<String> result = new ArrayList<String>();

        String targetKey = sortLetters(targetWord);
        if (!lettersToWord.containsKey(targetKey))
        {
            return new ArrayList<>();
        }

        return lettersToWord.get(targetKey);
    }

    @VisibleForTesting
    // same length -> same letters
    // (stringBuilder.remove, remove letter appear in base from word, remove the first one appear)
    // check if threre is stuff in stringBuilder.empty
    // sort the word and check if they are equal, alphabetically


    static boolean isAnagram(String first, String second)
    {
       return sortLetters(first).equals(sortLetters(second));
    }

    @VisibleForTesting
    static String sortLetters(String input)
    {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i = 'a'; i<='z'; i++)
        {
            char extraLetter = (char) i;

            String sortedAfter = sortLetters(word +extraLetter);

            // go through each element in the sorted key and add them to the result list
            result.addAll(getAnagrams(sortedAfter));
        }
        return result;
    }

    public String pickGoodStarterWord()
    {
        ArrayList<String> goodWords = new ArrayList<String>();
        for (String key: lettersToWord.keySet())
        {
            if(key.length()>=DEFAULT_WORD_LENGTH
                    && lettersToWord.get(key).size()>=MIN_NUM_ANAGRAMS
                    && key.length() <=MAX_WORD_LENGTH)
            {

                goodWords.addAll(lettersToWord.get(key));
            }

        }

        int random = (int)(Math.random()*(goodWords.size()));

        return goodWords.get(random);

//        String word;
//        List<String>  anagrams = new ArrayList<>();
//
//        do
//        {
//            word = masterList.get(Math.abs(random.nextInt()) % masterList.size());
//            if (word.length() <DEFAULT_WORD_LENGTH || word.length()>MAX_WORD_LENGTH)
//            {
//                continue;
//            }
//            anagrams = getAnagramsWithOneMoreLetter(word);
//
//        }
//        while(anagrams.size()<MIN_NUM_ANAGRAMS);
//        return word;
    }
}
