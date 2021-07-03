package com.mycompany.final_project.route;

import com.mycompany.final_project.spark.RouteBuilder;
import spark.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.lang.String.format;

public class HostNameRoute implements RouteBuilder {

    private final MyHost myHost = new MyHost();

    @Override
    public void configure(final Service spark, final String basePath) {

        spark.get(basePath + "/hostname", (request, response) -> myHost.toJson());
    }

    private static final class MyHost {

        String toJson() {

            return format("{\n\t\"localHostName\": \"%s\"\n}", findLocalHostName());
        }

        private String findLocalHostName() {
            try {
                return (InetAddress.getLocalHost()).getHostName();
            } catch (UnknownHostException uhe) {
                String host = uhe.getMessage(); // host = "hostname: hostname"
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        return host.substring(0, colon);
                    }
                }
                return "Unknown";
            }
        }
    }
}
