package utils.config;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import static utils.config.ServerConfiguration.getSocketAddresses;

public class ConnectConfiguration {
    private String name;
    private int type;
    private String connectId;
    private String connectString;
    private long idlePeriod;
    private int maxConnections;
    private long requestTimeout = 15000L;

    public ConnectConfiguration() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConnectId() {
        return this.connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public String getConnectString() {
        return this.connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public long getIdlePeriod() {
        return this.idlePeriod;
    }

    public void setIdlePeriod(long idlePeriod) {
        this.idlePeriod = idlePeriod;
    }

    public int getMaxConnections() {
        return this.maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public long getRequestTimeout() {
        return this.requestTimeout;
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public long[] getConnectArray() {
        if (null != this.connectId && !this.connectId.isEmpty()) {
            String[] ids = this.connectId.split(";");
            long[] connectId = new long[ids.length];
            int i = 0;

            for(int iSize = ids.length; i < iSize; ++i) {
                connectId[i] = Long.parseLong(ids[i]);
            }

            return connectId;
        } else {
            return null;
        }
    }

    public List<Long> getConnectList() {
        if (null != this.connectId && !this.connectId.isEmpty()) {
            String[] ids = this.connectId.split(";");
            List<Long> connectId = new ArrayList<>(ids.length);
            int i = 0;

            for(int iSize = ids.length; i < iSize; ++i) {
                connectId.add(Long.parseLong(ids[i]));
            }

            return connectId;
        } else {
            return null;
        }
    }

    public SocketAddress[] getAddressArray() {
        if (null != this.connectString && !this.connectString.isEmpty()) {
            String[] hosts = this.connectString.split(";");
            return getSocketAddresses(hosts);
        } else {
            return null;
        }
    }

    public List<SocketAddress> getAddressList() {
        if (null != this.connectString && !this.connectString.isEmpty()) {
            String[] hosts = this.connectString.split(";");
            List<SocketAddress> addresses = new ArrayList<>(hosts.length);
            return getSocketAddresses(hosts, addresses);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "ConnectConfiguration{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", connectId='" + connectId + '\'' +
                ", connectString='" + connectString + '\'' +
                ", idlePeriod=" + idlePeriod +
                ", maxConnections=" + maxConnections +
                ", requestTimeout=" + requestTimeout +
                '}';
    }
}
