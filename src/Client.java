import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public class Client {

    public static void main(String[] args) {
        Logger logger = new Logger("log.txt");
        try {
            GraphInterface stub = (GraphInterface) Naming.lookup("rmi://localhost:5000/root");
            BatchGenerator generator = new BatchGenerator();

            //String batch = generator.generateBatchFromInputFile("");
            //String batch = generator.generateBatchFromUserInput();
            String batch = generator.generateBatchRandomly(50, 10, 10);
            logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH, batch, System.currentTimeMillis());
            String batchResult = stub.executeBatch(batch);
            logger.log(Thread.currentThread().getId(), Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
