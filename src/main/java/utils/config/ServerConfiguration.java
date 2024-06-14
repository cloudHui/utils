package utils.config;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerConfiguration {
    private String name;
    private String hostString;

    public ServerConfiguration() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostString() {
        return this.hostString;
    }

    public void setHostString(String hostString) {
        this.hostString = hostString;
    }

    public boolean hasHostString() {
        return null != this.hostString && !this.hostString.isEmpty();
    }

    public SocketAddress[] getHostArray() {
        if (null != this.hostString && !this.hostString.isEmpty()) {
            String[] hosts = this.hostString.split(";");
            return getSocketAddresses(hosts);
        } else {
            return null;
        }
    }

    public static SocketAddress[] getSocketAddresses(String[] hosts) {
        SocketAddress[] addresses = new SocketAddress[hosts.length];
        int i = 0;

        for(int iSize = hosts.length; i < iSize; ++i) {
            String[] data = hosts[i].split(":");
            addresses[i] = new InetSocketAddress(data[0], Integer.parseInt(data[1]));
        }

        return addresses;
    }

    public List<SocketAddress> getHostList() {
        if (null != this.hostString && !this.hostString.isEmpty()) {
            String[] hosts = this.hostString.split(";");
            List<SocketAddress> addresses = new ArrayList<>(hosts.length);
            return getSocketAddresses(hosts, addresses);
        } else {
            return Collections.emptyList();
        }
    }

    public static List<SocketAddress> getSocketAddresses(String[] hosts, List<SocketAddress> addresses) {
        int i = 0;

        for(int iSize = hosts.length; i < iSize; ++i) {
            String[] data = hosts[i].split(":");
            addresses.add(new InetSocketAddress(data[0], Integer.parseInt(data[1])));
        }

        return addresses;
    }

    @Override
    public String toString() {
        return "ServerConfiguration{" +
                "name='" + name + '\'' +
                ", hostString='" + hostString + '\'' +
                '}';
    }
}
