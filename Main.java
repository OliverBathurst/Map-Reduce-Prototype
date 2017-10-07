public class Main {

    public static void main(String[] args) {
        if(args.length == 2) {
            Config newConfig = new Config();
            newConfig.setMapper(mapper.class);
            newConfig.setReducer(reducer.class);
            newConfig.setComparator(comparator.class);
            newConfig.setTitle("Testing");
            newConfig.addInputPath(args[0]);
            newConfig.addOutputPath(args[1]);
            Job newJob = new Job(newConfig);
            newJob.runJob();
        }else{
            System.out.println("Insufficient number of arguments\n" +
                    "java -jar mapReduce.jar inputfile outputfile");
        }
    }
    class reducer extends Reducer{
        void reduce() {


        }
    }
    class mapper extends Mapper{
        void map() {


        }
    }
    class comparator{


    }
}