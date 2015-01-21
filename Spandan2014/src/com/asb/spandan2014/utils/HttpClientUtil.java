package com.asb.spandan2014.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

public class HttpClientUtil {
  private static final String SPANDAN_REGISTRATION_URL = "http://iiitbspandan.appspot.com/ind_registration";
  private static final String REGISTRATION_URL = "https://spandan-2014.appspot.com/register";
  private static final String ALERTS_URL = "https://spandan-2014.appspot.com/alerts";

  public static String registerDevice(String regId) {
    try {

      String data = URLEncoder.encode("regId", "UTF-8") + "="
          + URLEncoder.encode(regId, "UTF-8");

      URLConnection conn = new URL(REGISTRATION_URL).openConnection();

      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(data);
      wr.flush();

      StringBuilder lineResp = new StringBuilder();
      ;
      String line = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      while ((line = br.readLine()) != null) {
        lineResp.append(line);
      }
      br.close();
      return lineResp.toString();

    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    } catch (ClientProtocolException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String fetchAlerts(int limit, int offset) {
    try {

      String data = URLEncoder.encode("limit", "UTF-8") + "="
          + URLEncoder.encode(String.valueOf(limit), "UTF-8") + "&"
          + URLEncoder.encode("offset", "UTF-8") + "="
          + URLEncoder.encode(String.valueOf(offset), "UTF-8");

      URLConnection conn = new URL(ALERTS_URL).openConnection();

      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(data);
      wr.flush();

      StringBuilder lineResp = new StringBuilder();
      ;
      String line = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      while ((line = br.readLine()) != null) {
        lineResp.append(line);
      }
      br.close();
      return lineResp.toString();

    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    } catch (ClientProtocolException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static String registerForIndividual(String name, String phone,
      String email, String yoj, List<String> events) {
    try {

      String data = URLEncoder.encode("name", "UTF-8") + "="
          + URLEncoder.encode(name, "UTF-8") + "&"
          + URLEncoder.encode("phone", "UTF-8") + "="
          + URLEncoder.encode(phone, "UTF-8") + "&"
          + URLEncoder.encode("email", "UTF-8") + "="
          + URLEncoder.encode(email, "UTF-8") + "&"
          + URLEncoder.encode("yoj", "UTF-8") + "="
          + URLEncoder.encode(yoj, "UTF-8");

      for (String event : events) {
        data += "&" + URLEncoder.encode("events", "UTF-8") + "="
            + URLEncoder.encode(event, "UTF-8");
      }

      URLConnection conn = new URL(SPANDAN_REGISTRATION_URL).openConnection();

      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(data);
      wr.flush();

      StringBuilder lineResp = new StringBuilder();
      ;
      String line = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(
          conn.getInputStream()));
      while ((line = br.readLine()) != null) {
        lineResp.append(line);
      }
      br.close();
      String response = lineResp.toString().replaceAll("<br />", "\n");
      return response;
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
