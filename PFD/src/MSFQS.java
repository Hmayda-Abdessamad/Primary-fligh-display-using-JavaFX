
import java.util.*;
/** * @Class MSFQS * @Description Multilevel feedback queue scheduling algorithm * @Author Naren * @Date 2020/5/30 10:46 * @Version 1.0 */
public class MSFQS {

    /* Three queues */
    private static Queue<Progress> firstQueue = new LinkedList<>();
    private static Queue<Progress> secondQueue = new LinkedList<>();
    private static Queue<Progress> thirdQueue = new LinkedList<>();
    private static int firstTime; // First queue cpu Time slice
    private static int secondTime; // The second line cpu Time slice
    private static int thirdTime; // The third line cpu Time slice
    private static int proNum; // Number of processes
    private static Scanner sc = new Scanner(System.in);
    /** * Internal process class ： Simulation process */
    private static class Progress implements Comparable<Progress> {

        String id; // Process identifier
        int reachTime; // Arrival time
        int cpuTime; // The elapsed time
        int needTime; // It's still going to take time
        char state; // Process status
        /* Rearrange the output format */
        @Override
        public String toString() {

            System.out.println();
            return String.format(" process %s: %10d %7d %8d %7c\n", id, reachTime, cpuTime, needTime, state);
        }
        /* Rewrite comparator */
        @Override
        public int compareTo( Progress b ) {

// Press reachTime Sort from small to large
            return Float.compare(reachTime, b.reachTime);
        }
    }
    /** * Process scheduling algorithm ：Multi-stage feedback queue scheduling algorithm */
    private static void progressScheduling(Progress[] pro){

        int firstCpu = firstTime;
        int secondCpu = secondTime;
        int thirdCpu = thirdTime;
        int currentTime = 0;
        int num = 0;
//System.out.println(Arrays.toString(pro));
        /* When a process is not running or the process queue is not empty , per 1 Time slices are in units */
        while(num < proNum || !firstQueue.isEmpty() || !secondQueue.isEmpty() || !thirdQueue.isEmpty()){

            /* A process has arrived at the current moment , Add to the first line */
            while(num < proNum && pro[num].reachTime == currentTime)
                firstQueue.offer(pro[num++]);
// Print the status of each queue process in the last second
            viewMenu(currentTime);
            /* The current queue 1 Running process */
            if(!firstQueue.isEmpty()){

                if (secondQueue.peek() != null) secondQueue.peek().state = 'R';
                if (thirdQueue.peek() != null) thirdQueue.peek().state = 'R';
// It's still going to take time ：-1
                firstQueue.peek().needTime -= 1;
//CPU Time remaining ：-1
                firstTime -= 1;
// Update current time ：+1
                currentTime++;
// The process is running , state ：E.
                if(firstQueue.peek().needTime > 0){

                    firstQueue.peek().state = 'E';
// The current queue CPU When the time slice runs out and the process is not finished , Process out of the team , In the end of the second priority line
                    if(firstTime == 0) {

                        firstQueue.peek().state = 'R';
                        secondQueue.offer(firstQueue.poll());
                        firstTime = firstCpu;
                    }
                }
// The process is running , state ：F, Record the completion time and leave the team
                else if(firstQueue.peek().needTime == 0){

                    firstQueue.peek().state = 'F';
                    System.out.printf("\n The current moment ：%d, This process runs to the end ：\n",currentTime);
                    System.out.println(firstQueue.peek());
                    Objects.requireNonNull(firstQueue.poll());
                    firstTime = firstCpu;
                }
            }
            /* The current queue 2 Running process */
            else if(!secondQueue.isEmpty()){

                if (thirdQueue.peek() != null) thirdQueue.peek().state = 'R';
// It's still going to take time ：-1
                secondQueue.peek().needTime -= 1;
//CPU Time remaining ：-1
                secondTime -= 1;
// Update current time ：+1
                currentTime++;
// The process is running , state ：F, Record the completion time and leave the team
                if(secondQueue.peek().needTime == 0){

                    secondTime = secondCpu;
                    secondQueue.peek().state = 'F';
                    System.out.printf("\n The current moment ：%d, This process runs to the end ：\n",currentTime);
                    System.out.println(secondQueue.peek());
                    Objects.requireNonNull(secondQueue.poll());
                }
// The process is running , state ：E.
                else if(secondQueue.peek().needTime > 0){

                    secondQueue.peek().state = 'E';
// The current queue CPU When the time slice runs out and the process is not finished , Process out of the team , In the end of the second priority line
                    if(secondTime == 0) {

                        secondQueue.peek().state = 'R';
                        thirdQueue.offer(secondQueue.poll());
                        secondTime = secondCpu;
                    }
                }
            }
            /* The current queue 3 Running process */
            else if(!thirdQueue.isEmpty()){

// It's still going to take time ：-1
                thirdQueue.peek().needTime -= 1;
//CPU Time remaining ：-1
                thirdTime -= 1;
// Update current time ：+1
                currentTime++;
// The process is running , state ：R.
                if(thirdQueue.peek().needTime > 0){

                    thirdQueue.peek().state = 'E';
// The current queue CPU When the time slice runs out and the process is not finished , Process out of the team , In the end of the second priority line
                    if(thirdTime == 0) {

                        thirdQueue.peek().state = 'R';
                        thirdQueue.offer(thirdQueue.poll());
                        thirdTime = thirdCpu;
                    }
                }
// The process is running , state ：F, Record the completion time and leave the team
                else{

                    firstTime = firstCpu;
                    thirdQueue.peek().state = 'F';
                    System.out.printf("\n The current moment ：%d, This process runs to the end ：\n",currentTime);
                    System.out.println(thirdQueue.peek());
                    Objects.requireNonNull(thirdQueue.poll());
                }
            }
        }
    }
    /** * Panel input ： Get the process array */
    private static Progress[] operator(){

        System.out.println("-----------------3118004950 Chai Zheng -----------------\n");
        System.out.println(" Welcome to the multi-level queue feedback scheduling simulation system , Number of queues ：3.\n\n");
        System.out.println(" Please input the time slice length of each queue from high to low according to the queue priority ：");
        firstTime = sc.nextInt();
        secondTime = sc.nextInt();
        thirdTime = sc.nextInt();
        System.out.print( " Please enter the number of processes :" );
        proNum = sc.nextInt();
        /* Get the process array */
        Progress[] pro = new Progress[proNum];
        System.out.println( " Please enter the process identifier in turn , Process arrival time , Process run time :" );
        for( int i = 0; i < proNum; i++ ) {

            pro[i] = new Progress();
            pro[i].id = sc.next();
            pro[i].reachTime = sc.nextInt();
            pro[i].cpuTime = sc.nextInt();
            pro[i].needTime = pro[i].cpuTime;
            pro[i].state = 'R';
        }
// Follow the process compareTo() According to the time of arrival
        Arrays.sort(pro);
        return pro;
    }
    /** * Output panel ： Real time output of running results */
    private static void viewMenu(int currentTime){

        System.out.printf("\n The current moment ：%d\n",currentTime);
        System.out.println("---------------------------------------------");
        System.out.println(" Arrival time The elapsed time The rest of the time state ");
        if(firstQueue.isEmpty()) System.out.println(" Line one ： empty ");
        else System.out.println(" Line one ：\n"+ firstQueue.toString()
                .replace("[", "").replace("]", "")
                .replace(", ", ""));
        if(secondQueue.isEmpty()) System.out.println(" Queue two ： empty ");
        else System.out.println(" Queue two ：\n"+ secondQueue.toString()
                .replace("[", "").replace("]", "")
                .replace(", ", ""));
        if(thirdQueue.isEmpty()) System.out.println(" Line three ： empty ");
        else System.out.println(" Line three ：\n"+ thirdQueue.toString()
                .replace("[", "").replace("]", "")
                .replace(", ", ""));
        System.out.println("=============================================");
    }
    /** * main() */
    public static void main(String[] args) {

        progressScheduling(operator());
    }
}
