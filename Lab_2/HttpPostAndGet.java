package post;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Post {
    public static void main(String[] args) throws MalformedURLException, IOException {
        // Opening the connection to a URL where we can post
        URL myUrl = new URL("https://jsonplaceholder.typicode.com/posts/");
        HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

        // Setting the method for this connection
        conn.setRequestMethod("POST");

        // Enabling output for the connection to allow posting
        conn.setDoOutput(true);

        // Setting headers for the POST request
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");

        // The JSON string we want to post
        String postedString = "{\"title\": \"foo\", \"body\": \"Hi!!! We have posted something!!! Yay!!!\", \"userId\": 1}";

        // Writing data to the output stream
        try (OutputStream out = conn.getOutputStream()) {
            byte[] input = postedString.getBytes("utf-8");
            out.write(input, 0, input.length);
        }

        // Reading the response from the server
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Message: " + conn.getResponseMessage());

        if (responseCode == HttpURLConnection.HTTP_CREATED) { // HTTP_CREATED is 201
            System.out.println("Post Successful!");

            // Reading response body
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String eachLine;
                while ((eachLine = buffer.readLine()) != null) {
                    response.append(eachLine.trim());
                }
                System.out.println("Response Content: " + response.toString());
            }
        } else {
            System.out.println("Post Failed! Response Code: " + responseCode);
        }

        // Disconnecting the connection
        conn.disconnect();
    }
}
