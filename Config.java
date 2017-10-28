/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Config {
    private int BLOCK_SIZE = 128, CHUNK_SIZE = 128;
    private Class reduce, map;
    private String jobName, input, output;
    private Context contextMapper, contextReducer;

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
    void setReducerContext(Context context){
        contextReducer = context;
    }
    void setMapperContext(Context context){
        contextMapper = context;
    }
    @SuppressWarnings("unused")
    void setBlockSize(int num){
        BLOCK_SIZE = num;
    }
    @SuppressWarnings("unused")
    void setChunkSize(int num){
        CHUNK_SIZE = num;
    }
    @SuppressWarnings("unused")
    int getBlockSize(){
        return BLOCK_SIZE;
    }
    int getChunkSize(){
        return CHUNK_SIZE;
    }
    Context getMapperContext(){
        return contextMapper;
    }
    Context getReducerContext(){
        return contextReducer;
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