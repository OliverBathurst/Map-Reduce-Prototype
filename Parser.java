import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Oliver on 19/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Parser {
    private final ArrayList<ArrayList<String>> nodes = new ArrayList<>();
    private final String filepath;
    private int CHUNK_SIZE = 2048;

    Parser(String path, int size){
        this.filepath = path;
        this.CHUNK_SIZE = size;
    }

    ArrayList<ArrayList<String>> returnMap(){
        return nodes;
    }

    void run(){
        try {
            ArrayList<String> tempStorage = new ArrayList<>(CHUNK_SIZE);
            int counter = 0;

            Scanner scanner = new Scanner(new File(filepath));
            while(scanner.hasNextLine()){
                if(counter != CHUNK_SIZE) {
                    tempStorage.add(scanner.nextLine());
                    counter++;
                }else{
                    nodes.add(new ArrayList<>(tempStorage));
                    tempStorage.clear();
                    counter = 0;
                }
            }
            if (tempStorage.size() > 0){
                nodes.add(new ArrayList<>(tempStorage));
            }
            scanner.close();
        }catch(Exception e){
            System.out.println("EXCEPTION: \n" + e.getMessage());
        }
    }
}