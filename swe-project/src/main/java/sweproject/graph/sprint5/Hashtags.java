package sweproject.graph.sprint5;

import sweproject.GetProperties;
import sweproject.graph.sprint3.Reader;
import sweproject.graph.sprint3.TwitterGraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Hashtags {

    public static TwitterGraph readHashtags(){
        GetProperties prop = new GetProperties();

        TwitterGraph hashtagGraph = new TwitterGraph();
        try{
            BufferedReader buf = new BufferedReader(new FileReader(prop.getGraphFilepath()));
            String lineJustFetched;

            while(true){
                lineJustFetched = buf.readLine();
                if(lineJustFetched == null){
                    break;
                }else{
                    String[] lineIn = lineJustFetched.split("\t");

                    // Using the provided data
                    // if (lineIn.length == 3 && lineIn[2].startsWith("RT @")) {

                    // Using the data we collected.
                    // if (lineIn.length == 5 && lineIn[2].startsWith("RT @")) {

                    if ((lineIn.length == 5 || lineIn.length == 3) && lineIn[2].contains("#")) {
                        String user = lineIn[1].trim();
                        String text = lineIn[2].replace("\n", " ").replace("\t", " ").replace(".", " ").replace(",", " ");
                        List<String> hashtags = checkHashtags(text);

                        for (String h : hashtags) {
                            if (h.contains("#")) {
                                hashtagGraph.addArc(user, h, 1);
                            }
                        }
                    }
                }
            }

            buf.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        return hashtagGraph;
    }

    public static List<String> checkHashtags(String input){
        String[] uncheckedHashtags = input.split(" ");
        List<String> confirmedHashtags = new ArrayList<>();
        for (String uncheckedHashtag : uncheckedHashtags) {
            if (uncheckedHashtag.contains("#")) {
                confirmedHashtags.add(removeLeadingChars(uncheckedHashtag).replaceAll("[^#a-zA-Z ]", ""));
            }
        }
        return confirmedHashtags;
    }

    public static String removeLeadingChars(String s) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && !(sb.charAt(0) == '#')) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    //ToDo: Assign mean stances for hashtags from user

    public static Map<String, Integer> assignHashtagStances(){
        System.out.println("Creating graph...");
        TwitterGraph graph = readHashtags();

        Map<String, Map<String, Integer>> map = graph.getEdges();
        System.out.println("Getting hashtags...");

        Map<String, Integer> users = Reader.readStances();

        ConcurrentHashMap<String, Integer> hashtagMap = new ConcurrentHashMap<>();

        for (String u : map.keySet()){
            for (String h : map.get(u).keySet()) {
                if(users.containsKey(u)) {
                    int stance = users.get(u);
                    if(hashtagMap.containsKey(h)) {
                        hashtagMap.put(h, hashtagMap.get(h) + stance);
                    } else {
                        hashtagMap.put(h, stance);
                    }
                }
            }
        }

        hashtagMap.replaceAll((h, v) -> v / (graph.getTotalRetweets(h)));

        return hashtagMap;
    }

    public static Map<String, Integer> assignUserStancesFromHashtags() {

        System.out.println("Creating graph...");
        TwitterGraph hashtags = Hashtags.readHashtags();
        // Hashmap of all Hashtag stances
        Map<String, Map<String, Integer>> initialHashmap = hashtags.getEdges();

        System.out.println("Getting hashtag stances...");
        Map<String, Integer> stances = Reader.readHashtagStances();

        // Hashmap with all other users that are going to be assigned a stance
        System.out.println("Getting users who don't have a stance...");
        ConcurrentHashMap<String, Integer> completeHashmap = new ConcurrentHashMap<>();

        for (String unassigned : initialHashmap.keySet()) {
            if(!stances.containsKey(unassigned)) {
                completeHashmap.put(unassigned, 0);
            }
        }

        for (String u : completeHashmap.keySet()){
            if(initialHashmap.containsKey(u)) {
                for (String h : initialHashmap.get(u).keySet()) {
                    if(stances.containsKey(h)) {
                        int stance = stances.get(h);
                        if (completeHashmap.containsKey(h)) {
                            completeHashmap.put(u, (completeHashmap.get(u) + stance));
                        } else {
                            completeHashmap.put(u, stance);
                        }
                    }
                }
            }
        }

        completeHashmap.replaceAll((h, v) -> v / (hashtags.getTotalRetweets(h)));

        return completeHashmap;
    }
}
