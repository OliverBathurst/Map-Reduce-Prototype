import java.io.File;
import java.io.FileNotFoundException;
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
    private final ArrayList<String> readRow = new ArrayList<>();
    private final String filepath;

    Parser(String path){
        this.filepath = path;
    }

    ArrayList<String> returnMap(){
        return readRow;
    }

    void run(){
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while(scanner.hasNextLine()){
                readRow.add(scanner.nextLine());
            }
            scanner.close();
        }catch(FileNotFoundException e){
            System.out.println("EXCEPTION: File not Found Exception\n"
                    + e.getMessage());
        }
    }
}