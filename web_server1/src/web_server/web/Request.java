package web_server.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;



public class Request {
    public String method;
    public String url;
    String version;
    ArrayList<String> headerLine = new ArrayList<>();

    public Hashtable<String, String> postEntityBody = new Hashtable<>();
    String putEntityBody = "";

    public Request(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            //Request Line
            String str = reader.readLine();
            parseRequestLine(str);

            //Header Line
            while (!str.equals("")) {
                str = reader.readLine();
                parseRequestHeaderLine(str);
            }

            //Entity Body: POST
            if (method.equals(entity.Method.POST)) {
                while (str != null) {
                    str = reader.readLine();
                    System.out.println(str);

                    //boundary code
                    if (str.contains("-----"))
                        continue;

                    //Content-Disposition
                    if (str.contains("Content-Disposition")) {
                        try {
                            String[] contents = str.split("\\s+");
                            String[] names = contents[2].split("\"");
                            String key = names[1];

                            //cr lf
                            str = reader.readLine();
                            System.out.println(str);

                            //value
                            str = reader.readLine();
                            System.out.println(str);
                            String value = str;

                            postEntityBody.put(key, value);
                            continue;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Entity Body: PUT
            if (method.equals(entity.Method.PUT)) {
                while (str != null) {
                    str = reader.readLine();
                    System.out.println(str);

                    //boundary code
                    if (str.contains("-----"))
                        continue;

                    //Content-Disposition
                    if (str.contains("Content-Disposition")) {
                        try {
                            str = reader.readLine();
                            System.out.println(str);

                            while (str != null) {
                                str = reader.readLine();
                                System.out.println(str);
                                putEntityBody += str;
                            }
                            continue;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Parse Request Error");
        }
    }

    /**
     * ����Request Line
     *
     * @param str
     */
    private void parseRequestLine(String str) {
        System.out.println(str);
        String[] split = str.split("\\s+");
        try {
            method = split[0];
            if (!entity.Method.methods.contains(method)) {
                method = entity.Method.UNRECOGNIZED;
            }
            url = split[1];
            version = split[2];
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("RequestLine Error");
        }
    }

    /**
     * ����Header Line
     *
     * @param str
     */
    private void parseRequestHeaderLine(String str) {
        System.out.println(str);
        headerLine.add(str);
    }

}