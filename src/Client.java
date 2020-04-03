import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public class Client {
    private static final int MAX_BATCH_COUNT = 10;
    private static int percentageOfWrites = 50;
    private static boolean isInitializer = false;

    public static void main(String[] args) {
        Logger logger = new Logger("log.txt");
        try {
            Graph stub = (Graph) Naming.lookup("rmi://localhost:5000/root");
            BatchGenerator generator = new BatchGenerator();

            String batch = "";
            if(isInitializer) {
                batch = generator.generateBatchFromUserInput();
                isInitializer = false;
                logger.log(Thread.currentThread().getId(), Logger.LogType.INITIALIZER, batch, System.currentTimeMillis());
                String batchResult = stub.initGraph(batch.split("\n"));
                logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());
            }
            else {
                batch = generator.generateBatchRandomly(percentageOfWrites, stub.getGraphSize(), MAX_BATCH_COUNT);
                logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH, batch, System.currentTimeMillis());
                String batchResult = stub.apply(batch.split("\n"));
                logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
