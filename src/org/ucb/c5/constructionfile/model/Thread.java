package org.ucb.c5.constructionfile.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Thread {
    private Queue<String> possible_Threads;
    //creating all possible threads
    public void initiate() throws Exception {
        String[] letters = {"A", "B", "C", "K", "L", "M", "N", "O", "P"};
        possible_Threads = new LinkedList<>(Arrays.asList(letters));

    }
    public String get() throws Exception {
        return possible_Threads.remove();
    }
}
