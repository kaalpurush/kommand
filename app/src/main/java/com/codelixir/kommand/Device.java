package com.codelixir.kommand;

public class Device {

    public String host_name;
    public String host_mac;
    public String host_ip;
    public int host_port;


    public Device(String host_name, String host_mac, String host_ip, int host_port) {
        this.host_name = host_name;
        this.host_mac = host_mac;
        this.host_ip = host_ip;
        this.host_port = host_port;
    }

    public String getIpWithPort() {
        return host_ip + ":" + host_port;
    }

}