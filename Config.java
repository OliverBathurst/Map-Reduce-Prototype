class Config {
    private int CHUNK_SIZE = 128;
    private Class reduce, map;
    private String jobName, input, output;
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
        input = inputPath;
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
    String getJobName(){
        return jobName;
    }
    String getInputPath(){
        return input;
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