package com.karolmalinowski.election.service.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UrlTools {
    /**
     * Reads content of url to string.
     * @param urlString
     * @return
     * @throws Exception
     */
    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
