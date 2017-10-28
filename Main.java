
/**
 * Created by Oliver Bathurst on 13/10/2017.
 * All Rights Reserved
 * Unauthorized copying of this file via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Main {
    public static void main(String[] args) {
        if(args.length == 2) {
            Config newConfig = new Config();
            newConfig.setMapper(map.class);
            newConfig.setReducer(reduce.class);
            newConfig.setTitle("Testing");
            newConfig.addInputPath(args[0]);
            newConfig.addOutputPath(args[1]);
            //newConfig.setBlockSize(256);//not really needed
            //newConfig.setChunkSize(1);
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
            if(str[1] !=null && str[4] !=null) {
                context.write(str[1], str[4]); //country and pop
            }
            //context.write(str[0], str[1]);
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