import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Logger {

    private String filePath;

    public Logger(String filePath) {
        this.filePath = filePath;
    }

    enum LogType {
        BATCH, BATCH_RESULT;
    }

    public void log(long client_id, LogType type, String content, long currentMillis) {
        Timestamp timestamp = new Timestamp(currentMillis);

        String logLine = "Client Number : " + client_id + "\t----\t" +  "at " +timestamp.toString() + "\t----\t";

        if(type.equals(LogType.BATCH)) logLine += "BATCHED: ";
        else logLine+= "RECEIVED: ";

        logLine += content.replaceAll("\n", ", ");
        logLine += "\n\n";

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(logLine);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
