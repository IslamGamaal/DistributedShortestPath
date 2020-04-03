import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private static final int MAX_BATCH_COUNT = 10;
    private static int percentageOfWrites = 50;
    private static boolean isInitializer = true;

    public static void main(String[] args) {
        Logger logger = new Logger("log.txt");
        Scanner sc = new Scanner(System.in);
        System.out.print("Hello Client, to you want to initialize ? \n1-Yes\n2-No\n");
        int x = sc.nextInt();
        if(x == 1) isInitializer = true;
        else isInitializer = false;

        try {
            Graph stub = (Graph) Naming.lookup("rmi://localhost:5000/root");
            BatchGenerator generator = new BatchGenerator();
            String batch = "";
            if (isInitializer) {
                batch = generator.generateBatchFromUserInput();
                isInitializer = false;
                System.out.println("The initialization graph is: \n" + batch);
                logger.log(Thread.currentThread().getId(), Logger.LogType.INITIALIZER, batch, System.currentTimeMillis());
                String batchResult = stub.initGraph(batch);
                System.out.println("The resulted output: \n" + batchResult);
                logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());
            }
            while (true) {
                batch = generator.generateBatchRandomly(percentageOfWrites, stub.getGraphSize(), MAX_BATCH_COUNT);
                System.out.println("The batch is: \n" + batch);
                logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH, batch, System.currentTimeMillis());
                String batchResult = stub.exectue(batch);
                System.out.println("The batch result: \n" + batchResult);
                logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());
                Thread.sleep(5000);
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
