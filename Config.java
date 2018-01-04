import java.util.ArrayList;

/**
 * Configuration class
 * Used for setting up of variables used by the Job class
 * Crucially, setting of the designated reducer and mapper class is needed
 * for proper operation of the mapreducer (setReducerClass & setMapperClass)
 * Other options include turning multithreading on/off, creating output/input paths
 */
class Config {
    private final ArrayList<String> inputFiles = new ArrayList<>();
    private boolean multiThreaded = false;//multithreaded disabled by default
    private int CHUNK_SIZE = 256; //Determines when a new chunk should be created (in the input reader)
    private Class reduce, map;//the reduce and map classes are explicitly declared by the user in the main method (setMapperClass etc.)
    private String jobName, output;

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