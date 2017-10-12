class Main {

    public static void main(String[] args) {
        if(args.length == 2) {
            Config newConfig = new Config();
            newConfig.setMapper(map.class);
            newConfig.setReducer(reduce.class);
            newConfig.setComparator(comparator.class);
            newConfig.setContext(new Context());
            newConfig.setTitle("Testing");
            newConfig.addInputPath(args[0]);
            newConfig.addOutputPath(args[1]);

            Job newJob = new Job();
            newJob.setJobConfig(newConfig);
            newJob.runJob();
        }else{
            System.out.println("Insufficient number of arguments\n" +
                    "java -jar mapReduce.jar inputfile outputfile");
        }
    }
    public static class reduce extends Reducer{
        public reduce(String[] arguments) {}
    }
    public static class map extends Mapper{
        public map(String values, Context context) {
            //for(String str: arguments){
            //    System.out.println(str);
            //}
            //context.write(arguments[0], arguments[1]);
        }
    }
    public static class comparator{}
}