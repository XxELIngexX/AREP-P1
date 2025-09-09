package edu.edi.arep.parcial;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        HashMap<String, String> diccionario = new HashMap<>();
        diccionario.put("nombre", "cesar");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        while (true) {
            try {
                System.out.println("Listo para recibir ... en el puerto:");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            String path = null;
            boolean firstLine = true;
            String method = null;
            URI request = null;
            String outputLine = null;
            String outData = null;

            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    try {
                        request = new URI(inputLine.split(" ")[1]);
                        path = request.getPath();
                        method = inputLine.split(" ")[0];
                        System.out.println("Method: " + method + " | Path: " + path);
                    } catch (URISyntaxException e) {
                        System.err.println("Invalid URI syntax: " + inputLine);
                    }
                    firstLine = false;
                }
                System.out.println("Recibi: " + inputLine);
                if (inputLine.isEmpty()) {
                    break;
                }
            }

            if (path == null || path.equals("/")) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>"
                        + "<html>"
                        + "<head>"
                        + "    <title>Form Example</title>"
                        + "    <meta charset='UTF-8'>"
                        + "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                        + "</head>"
                        + "<body>"
                        + "    <h1>Form with GET</h1>"
                        + "    <form action='/hello'>"
                        + "        <label for='name'>Name:</label><br>"
                        + "        <input type='text' id='name' name='name' value='John'><br><br>"
                        + "        <input type='button' value='Submit' onclick='loadGetMsg()'>"
                        + "    </form>"
                        + "    <div id='getrespmsg'></div>"
                        + "    <script>"
                        + "        function loadGetMsg() {"
                        + "            let nameVar = document.getElementById('name').value;"
                        + "            const xhttp = new XMLHttpRequest();"
                        + "            xhttp.onload = function () {"
                        + "                document.getElementById('getrespmsg').innerHTML ="
                        + "                    this.responseText;"
                        + "            }"
                        + "            xhttp.open('GET', '/hello?name=' + nameVar);"
                        + "            xhttp.send();"
                        + "        }"
                        + "    </script>"
                        + "</body>"
                        + "</html>";
            }
            if (method.equals("GET")) {
                String[] kv = null;
                if (request.getQuery() != null) {
                    kv = request.getQuery().split("&");

                }
                if (path.startsWith("/set")) {
                    // String v = diccionario.get(kv[0].split("=")[1]);
                    String newValue = kv[1].split("=")[1];
                    diccionario.put(kv[0].split("=")[1], newValue);
                    outData = "nuevo valor es: " + newValue;
                }
                if (path.startsWith("/get")) {
                    System.out.println("valor a buscar: " + kv[0].split("=")[1]);
                    String value = diccionario.get(kv[0].split("=")[1]);
                    outData = value;
                }
                if (path.startsWith("/add")) {
                    String newKey = kv[0].split("=")[1];
                    String newValue = kv[1].split("=")[1];
                    diccionario.put(newKey, newValue);
                    outData = "nuevo par llave valor: " + newKey + " " + newValue;
                }

            } else if (method.equals("POST")) {
            }

            outputLine = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n"
                    + "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<title>Title of the document</title>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<h1>" + outData + "\n"
                    + "</body>\n"
                    + "</html>\n";

            out.println(outputLine);
            out.close();
            in.close();
        
        clientSocket.close();
        //serverSocket.close();
    }
}
}