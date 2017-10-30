
class Main {
    public static void main(String[] args) {
        if(args.length == 2) {
            Config newConfig = new Config();
            newConfig.setMapper(map.class);
            newConfig.setReducer(reduce.class);
            newConfig.setTitle("Testing");
            newConfig.addInputPath(args[0]);
            //newConfig.addInputPath(args[2]); add as many input paths as you want
            newConfig.addOutputPath(args[1]);
            //newConfig.setChunkSize(256);//not really needed, alter based on file size
            newConfig.setShuffle(false);
            newConfig.setMultiThreaded(false); //multithreading is slower in this instance
            new Job(newConfig).runJob();
        }else{
            System.out.println("Insufficient number of arguments\n" +
                    "java -jar mapReduce.jar inputfile outputfile");
        }
    }
    @SuppressWarnings("WeakerAccess")
    public static class map {
        public map(String values, Context context) {
            String[] str = values.split("\t"); //split with tab (tsv)
            context.write(str[0], str[1]);
        }
    }
    @SuppressWarnings("WeakerAccess")
    public static class reduce {
        public reduce(Object key, Iterable<Object> values, Context context) {
            int a = 0;
            for(Object val: values){
                try {
                    a += Integer.parseInt(val.toString());
                }catch(Exception ignored){ }
            }
            context.write(key, a);
        }
    }
}