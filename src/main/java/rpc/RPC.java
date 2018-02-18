package rpc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class RPC {
    public static String rpc_call(String method, Params params) {
        //Create the json request object
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("jsonrpc", "2.0");
            jsonRequest.put("method", method);
            jsonRequest.put("params", params.getJsonArray());
            jsonRequest.put("id", UUID.randomUUID().hashCode());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return sendRPC(jsonRequest);
    }

    //TODO handle "ERROR" return
    private static String sendRPC(JSONObject j) {
        URL url;
        OutputStream out = null;
        try {
            url = new URL("http://127.0.0.1:8545");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setDoOutput(true);
            connection.connect();
            out = connection.getOutputStream();

            out.write(j.toString().getBytes());
            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                System.err.println("Status is not okay STATUS CODE:" + statusCode);
            }

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonObject resp = (JsonObject) parser.parse(new StringReader(response.toString()));

            JsonElement result = resp.get("result");
            JsonElement error = resp.get("error");
            if (result != null)
                System.err.println(result.toString());
            if (error != null)
                System.err.println(error.toString());
            assert result != null;
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return "ERROR";
    }
}
