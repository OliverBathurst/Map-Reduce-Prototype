public class Main {

    public static void main(String[] args) {
        if(args.length == 2) {
            Config newConfig = new Config();
            newConfig.setMain(Main.class);
            newConfig.setMapper(mapper.class);
            newConfig.setReducer(reducer.class);
            newConfig.setComparator(comparator.class);
            newConfig.setTitle("Testing");
            newConfig.addInputPath(args[0]);
            newConfig.addOutputPath(args[1]);
            newConfig.setRegex(",");
            Job newJob = new Job(newConfig);
            newJob.runJob();
        }else{
            System.out.println("Insufficient number of arguments\n" +
                    "java -jar mapReduce.jar inputfile outputfile");
        }
    }
    private class reducer extends Reducer{
        public void reduce() {


        }
    }
    public static class mapper{
        public mapper(){}
        public mapper(String[] arguments) {
            System.out.println(arguments[0]);
        }
    }

    private class comparator{


    }

}