
class Config {
    private Class reduce, map, comparator;
    private String jobName, input, output;
    private Context c;

    void setReducer(Class reducer){
        reduce = reducer;
    }
    void setMapper(Class mapper){
        map = mapper;
    }
    void setComparator(Class compare){
        comparator = compare;
    }
    void setTitle(String name){
        jobName = name;
    }
    void addInputPath(String inputPath){
        input = inputPath;
    }
    void addOutputPath(String outputPath){
        output = outputPath;
    }
    void setContext(Context context){
        c = context;
    }
    Context getContext(){
        return c;
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
    Class getComparator(){
        return comparator;
    }
}