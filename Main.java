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
            newConfig.setMapperContext(new Context());
            newConfig.setReducerContext(new Context());
            newConfig.setTitle("Testing");
            newConfig.addInputPath(args[0]);
            newConfig.addOutputPath(args[1]);
            new Job(newConfig).runJob();
        }else{
            System.out.println("Insufficient number of arguments\n" +
                    "java -jar mapReduce.jar inputfile outputfile");
        }
    }
    @SuppressWarnings("WeakerAccess")
    public static class map {
        public map(String values, Context context) {

        }
    }
    @SuppressWarnings("WeakerAccess")
    public static class reduce {
        public reduce(Object key, Iterable<Integer> values, Context context) {

        }
    }
}