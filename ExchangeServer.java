import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.io.BufferedReader;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.util.Scanner;

public class ExchangeServer {

    public static void main(String[] args) throws Exception {

        //Build the connection
        String url_str = "https://api.exchangeratesapi.io/latest?base=ZAR;symbols=USD";
        StringBuilder result = new StringBuilder();
        URL url = new URL(url_str);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        //Here we isolate the exact exchange rate from the result
        String[] arrResult = result.toString().split(":", 4);
        String[] arrResult2 = arrResult[2].split("}", 4);
        String exchangeRate = arrResult2[0];

        //Format the exchange rate to meet the specific pattern
        DecimalFormat myFormatter = new DecimalFormat("###.##");
        exchangeRate = myFormatter.format(Float.valueOf(exchangeRate));
        exchangeRate = "Good day. The current Rand/$ exchange rate is " + exchangeRate;

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            Socket connectionSocket = serverSocket.accept();
            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();
            Scanner scanner = new Scanner(inputToServer, "UTF-8");
            PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);
            serverPrintOut.println(exchangeRate);
            boolean done = false;

            while (!done && scanner.hasNextLine()) {
                String line2 = scanner.nextLine();

                if (line2.toLowerCase().trim().equals("exit")) {
                    done = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
