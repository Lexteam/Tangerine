/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.extra;

import xyz.lexteam.tangerine.util.JsonUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Optional;

public final class Hastebin {

    public static Optional<String> makeHaste(String content) {
        try {
            URL pathUrl = new URL("http://hastebin.com/documents");
            HttpURLConnection con = (HttpURLConnection) pathUrl.openConnection();

            con.setRequestMethod("POST");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(content);
            wr.flush();
            wr.close();

            return Optional.of("http://hastebin.com/" + JsonUtils.GSON.fromJson(
                    new InputStreamReader(con.getInputStream()), ResponseModel.class).getKey());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static class ResponseModel {

        private String key;

        public String getKey() {
            return this.key;
        }
    }
}
