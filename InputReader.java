import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

/**
 * The input reader class reads in a single file, the input is split into chunks,
 * the chunk size is configurable by the user.
 */
class InputReader {
    private final ArrayList<ArrayList<String>> chunks = new ArrayList<>();//list of chunks to be assigned to mappers.
    private final String filepath;
    private final int CHUNK_SIZE; //default chunk size

    /**
     * Initialise with filepath and chunk size
     */
    InputReader(String path, int size){
        this.filepath = path;
        this.CHUNK_SIZE = size;
    }
    /**
     * Return the list of chunks to delegate to mappers
     */
    ArrayList<ArrayList<String>> returnChunks(){
        return chunks;
    }

    /**
     * Reads in each line of the file into a chunk and after every n lines read, it creates a new chunk,
     * where n is the CHUNK_SIZE that the user can configure. All chunks are added to a chunks list.
     * Each chunk is given to a unique mapper.
     */
    void run(){
        try {
            ArrayList<String> tempStorage = new ArrayList<>(CHUNK_SIZE);//storage for a chunk of lines (list of lines)

            BufferedReader f = new BufferedReader(new FileReader(filepath)); //open buffered reader for file
            String line;
            int counter = 0;//counter to indicate when to start a new chunk
            while ((line = f.readLine()) != null){//if line is not null
                if(line.trim().length() > 0) {//if line is not empty
                    if (counter != CHUNK_SIZE) {//if counter not equal to the chunk size, add line to the chunk and increment counter
                        tempStorage.add(line);
                        counter++;
                    } else { //if we reach chunk size limit, add the chunk to the chunks list
                        chunks.add(new ArrayList<>(tempStorage)); //add the chunk to the chunks list
                        tempStorage.clear(); //clear the list for the next chunk
                        counter = 0; //reset counter
                    }
                }
            }
            if (tempStorage.size() > 0){ //if there's any lines left in the last chunk
                chunks.add(new ArrayList<>(tempStorage)); //add it to the chunks list
            }
            tempStorage.clear();
        }catch(Exception e){
            System.out.println("EXCEPTION: \n" + e.getMessage());
        }
    }
}