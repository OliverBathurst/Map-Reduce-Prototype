import java.util.ArrayList;

/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Config {
    private final ArrayList<String> inputFiles = new ArrayList<>();
    private int CHUNK_SIZE = 128;
    private boolean multiThreaded = false;
    private Class reduce, map;
    private String jobName, output;
    @SuppressWarnings("SameParameterValue")
    void setMultiThreaded(boolean b){
        multiThreaded = b;
    }
    boolean getMultiThreaded(){
        return multiThreaded;
    }
    void setReducerClass(Class reducer){
        reduce = reducer;
    }
    void setMapperClass(Class mapper){
        map = mapper;
    }
    @SuppressWarnings("SameParameterValue")
    void setTitle(String name){
        jobName = name;
    }
    void addInputPath(String inputPath){
        inputFiles.add(inputPath);
    }
    void addOutputPath(String outputPath){
        output = outputPath;
    }
    @SuppressWarnings("unused")
    void setChunkSize(int num){
        CHUNK_SIZE = num;
    }
    int getChunkSize(){
        return CHUNK_SIZE;
    }
    ArrayList<String> getInputPaths(){
        return inputFiles;
    }
    String getJobName(){
        return jobName;
    }
    String getOutputPath(){
        return output;
    }
    Class getReducerClass(){
        return reduce;
    }
    Class getMapperClass(){
        return map;
    }
}