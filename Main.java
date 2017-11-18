
/**
 * Created by Oliver on 18/11/2017.
 * Written by Oliver Bathurst <oliverbathurst12345@gmail.com>
 */

class Main {
    public static void main(String[] args) {
        Config newConfig = new Config();
        newConfig.setMapper(map.class);
        newConfig.setReducer(reduce.class);
        newConfig.setTitle("Testing");
        newConfig.addInputPath(args[0]);
        newConfig.addInputPath(args[1]); //add as many input paths as you want
        newConfig.addOutputPath(args[2]);
        //newConfig.setChunkSize(256);//not really needed, alter based on file size
        newConfig.setShuffle(false);
        newConfig.setMultiThreaded(false); //multithreading is slower in this instance
        new Job(newConfig).runJob();
    }
    @SuppressWarnings({"WeakerAccess, Annotator"})
    public static class map {
        public map(String values, Context context) {



            String[] str = values.split(","); //split with comma (csv)
            if(str[0] != null && str[1] != null && str[2] != null && str[3] != null && str[4] != null && str[5] != null){
                if(str[0].matches("[A-Z]{3}[0-9]{4}[A-Z]{2}[0-9]{1}") && str[1].matches("[A-Z]{3}[0-9]{4}[A-Z]{1}") && str[2].matches("[A-Z]{3}")
                        && str[3].matches("[A-Z]{3}") && str[4].matches("[0-9]{10}") && str[5].matches("[0-9]{1,4}")){
                    //passenger data
                    context.write(str[1], new Flight(str[0], str[2], str[3], str[4], str[5]));
                }
            }else if(str[0] != null && str[1] != null && str[2] != null && str[3] != null){
                if(str[0].matches("[A-Z]{3,20}") && str[1].matches("[A-Z]{3}") && str[2].matches("[0-9]{1,3}\\.[0-9]{3,13}") && str[3].matches("[0-9]{1,3}\\.[0-9]{3,13}")){
                    //airport data
                    context.write(str[0], new AirportData(str[1],str[2],str[3]));
                }
            }



        }
    }
    @SuppressWarnings("WeakerAccess")
    public static class reduce {
        @SuppressWarnings("unused")
        public reduce(Object key, Iterable<Object> values, Context context) {
            /*int a = 0;
            for(Object val: values){
                try {
                    a += Integer.parseInt(val.toString());
                }catch(Exception ignored){ }
            }
            context.write(key, a);*/
        }
    }
}