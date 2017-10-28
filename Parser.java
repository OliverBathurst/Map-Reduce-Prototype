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
    private final ArrayList<ArrayList<String>> chunks = new ArrayList<>();
    private final String filepath;
    private int CHUNK_SIZE = 2048; //default chunk size

    Parser(String path, int size){
        this.filepath = path;
        this.CHUNK_SIZE = size;
    }

    /**
     * Return the list of chunks to delegate to mappers
     */
    ArrayList<ArrayList<String>> returnMap(){
        return chunks;
    }

    /**
     * Reads in each line of the file into a chunk and every n lines creates a new chunk
     * where n is the CHUNK_SIZE that the user can choose in Config
     */
    void run(){
        try {
            ArrayList<String> tempStorage = new ArrayList<>(CHUNK_SIZE); //provide temp storage array list
            int counter = 0; //initialise the counter for checking chunk size

            Scanner scanner = new Scanner(new File(filepath)); //init scanner
            while(scanner.hasNextLine()){
                if(counter != CHUNK_SIZE) { //if the counter is not up to the specified chunk size
                    tempStorage.add(scanner.nextLine()); //add the line to the current chunk
                    counter++;
                }else{ //if we reach chunk limit
                    chunks.add(new ArrayList<>(tempStorage)); //add the chunk to the chunks list
                    tempStorage.clear(); //clear the list for next chunk
                    counter = 0; //reset counter
                }
            }
            if (tempStorage.size() > 0){ //if there's any lines left in storage
                chunks.add(new ArrayList<>(tempStorage)); //create last chunk and add it
            }
            scanner.close();
        }catch(Exception e){
            System.out.println("EXCEPTION: \n" + e.getMessage());
        }
    }
}