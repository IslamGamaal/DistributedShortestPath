import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static long clientId;
    private static int maxBatchCount = 10;
    private static int percentageOfWrites = 50;
    private static boolean isInitializer = true;
    private static String filePath = "";
    public static void main(String[] args) {
        Random r = new Random();
        clientId = r.nextInt((900000 - 100000) + 1);
        Logger logger = new Logger("log.txt");
        Scanner sc = new Scanner(System.in);
        System.out.print("Hello Client, to you want to initialize ? \n1-Yes\n2-No\n");
        int inputType = 2;
        int init = sc.nextInt();
        switch (init) {
            case 1:
                isInitializer = true;
                System.out.print("How do you like to give input ? \n1-Input File\n2-User Input\n");
                inputType = sc.nextInt();
                break;
            case 2:
                isInitializer = false;
                break;
        }
        try {
            Graph stub = (Graph) Naming.lookup("rmi://localhost:5000/root");
            BatchGenerator generator = new BatchGenerator();
            String batch = "";
            if (isInitializer) {
                switch (inputType) {
                    case 1:
                        System.out.print("Enter file path: ");
                        filePath = sc.nextLine();
                        filePath = sc.nextLine();
                        batch = generator.generateBatchFromInputFile(filePath);
                        break;
                    default:
                        System.out.println("Start entering your Edges. (\"S\" when done)");
                        batch = generator.generateBatchFromUserInput();
                        break;
                }
                isInitializer = false;
                System.out.println("The initialization graph is: \n" + batch);
                logger.log(clientId, Logger.LogType.INITIALIZER, batch, System.currentTimeMillis());
                String batchResult = stub.initGraph(batch);
                System.out.println("The resulted output: \n" + batchResult);
                logger.log(clientId, Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());
            }

            while (true) {
                inputType = askForTypeOfInput(sc);
                switch (inputType) {
                    case 1:
                        batch = generator.generateBatchFromInputFile(filePath);
                        break;
                    case 3:
                        batch = generator.generateBatchRandomly(percentageOfWrites, stub.getGraphSize(), maxBatchCount);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Start entering your Operations. (\"F\" when done)");
                        batch = generator.generateBatchFromUserInput();
                        break;
                }
                System.out.println("The batch is: \n" + batch);
                logger.log(clientId, Logger.LogType.BATCH, batch, System.currentTimeMillis());
                String batchResult = stub.exectue(batch);
                System.out.println("The batch result: \n" + batchResult);
                logger.log(clientId, Logger.LogType.BATCH_RESULT, batchResult, System.currentTimeMillis());
                Thread.sleep(1000);
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

    private static int askForTypeOfInput(Scanner sc) {
        System.out.println("How do you like to give Batch input ? \n1-Input File\n2-User Input\n3-Random\n4-Exit");
        int inputType = sc.nextInt();
        switch(inputType) {
            case 1:
                System.out.println("Enter file path\n");
                filePath = sc.nextLine();
                filePath = sc.nextLine();
                break;
            case 2:
                break;
            case 3:
                System.out.println("Enter the Write percentage (0~100)");
                percentageOfWrites = sc.nextInt();
                System.out.println("Enter the Maximum Batch size wanted");
                maxBatchCount = sc.nextInt();
                break;
            case 4:
                System.out.println("Goodbye.");
        }
        return inputType;
    }
}
