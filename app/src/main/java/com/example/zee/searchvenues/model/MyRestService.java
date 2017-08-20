package com.example.zee.searchvenues.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by zee on 8/17/2017.
 * Rest Service to communicate with server using HttpURLConnection
 */

public class MyRestService {

    public static String getData(String uri)
    {
        URL url;
        HttpsURLConnection myConnection=null;
        String errorInConnection="Error in Network Connection";
        BufferedReader reader=null;
        try {

                //create URL object from input uri
                 url = new URL(uri);

                //Open HTTP url connection
                 myConnection=(HttpsURLConnection)url.openConnection();


                 int status= myConnection.getResponseCode();

                // String builder to read input stream from server
                 StringBuilder sb=new StringBuilder();
            // if serer send bad request error
            if(status >=HttpsURLConnection.HTTP_BAD_REQUEST)
            {
                reader=new BufferedReader(new InputStreamReader(myConnection.getErrorStream()));
            }
            else
            {
                //read input stream from server
                reader=new BufferedReader(new InputStreamReader(myConnection.getInputStream()));

            }


            String line;
            //read line-by-line the input stream
            while ((line=reader.readLine())!=null)
            {
                sb.append(line + "\n");

            }
            //convert and return string buffer
            return sb.toString();

        }catch (IOException e)
        {
            e.printStackTrace();
            return errorInConnection;


        }finally {
            try {
                if(reader!=null)
                {
                    reader.close();

                }
                if (myConnection!=null)
                {
                    myConnection.disconnect();

                }


            }catch (IOException e)
            {
                e.printStackTrace();

            }
        }



    }
}
