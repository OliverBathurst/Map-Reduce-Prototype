import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Oliver on 18/11/2017.
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
            ArrayList<String> tempStorage = new ArrayList<>(CHUNK_SIZE);
            int counter = 0;
            BufferedReader f = new BufferedReader(new FileReader(filepath));
            String line;

            while ((line = f.readLine()) != null ){
                if(line.length() > 0) {
                    if (counter != CHUNK_SIZE) {
                        tempStorage.add(line);
                        counter++;
                    } else { //if we reach chunk limit
                        chunks.add(new ArrayList<>(tempStorage)); //add the chunk to the chunks list
                        tempStorage.clear(); //clear the list for next chunk
                        counter = 0; //reset counter
                    }
                }
            }

            if (tempStorage.size() > 0){ //if there's any lines left in storage
                chunks.add(new ArrayList<>(tempStorage)); //create last chunk and add it
            }
            tempStorage.clear();
        }catch(Exception e){
            System.out.println("EXCEPTION: \n" + e.getMessage());
        }
    }
}