package edu.edi.arep.parcial;

import java.io.*;
import java.net.*;

public class Fachada {
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String GET_URL = "http://localhost:36000";

    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        
        Boolean coneccion = conect();
                Socket clientSocket = null;
                while (coneccion) {
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
                }
        
                
                
                
            }
            
        public static Boolean conect() throws MalformedURLException, IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
        System.out.println("GET DONE");
        return true;
    }


    }
