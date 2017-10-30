import java.util.ArrayList;

class Config {
    private ArrayList<String> inputFiles = new ArrayList<>();
    private int CHUNK_SIZE = 128;
    private boolean multiThreaded = false;
    private boolean shuffle = false;

    private Class reduce, map;
    private String jobName, input, output;

    @SuppressWarnings("SameParameterValue")
    void setShuffle(boolean b){
        shuffle = b;
    }
    boolean getShuffle(){
        return shuffle;
    }
    @SuppressWarnings("SameParameterValue")
    void setMultiThreaded(boolean b){
        multiThreaded = b;
    }
    boolean getMultiThreaded(){
        return multiThreaded;
    }
    void setReducer(Class reducer){
        reduce = reducer;
    }
    void setMapper(Class mapper){
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
    Class getReducer(){
        return reduce;
    }
    Class getMapper(){
        return map;
    }
}