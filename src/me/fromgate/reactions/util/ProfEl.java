package me.fromgate.reactions.util;


public class ProfEl{
        long tick_num;
        String process;
        long execution_time;
        String comment;
        
        //
        private long start_time;
        private long stop_time;
        
        public ProfEl (String process, long tick_count){
            this.process = process;
            this.tick_num = tick_count;
            this.start_time = System.nanoTime();
        }
        
        public void stopCount(String comment){
            this.comment = comment;
            this.stop_time = System.nanoTime();
            this.execution_time = stop_time-start_time;
        }
        
    }