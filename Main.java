import java.util.HashMap;
import java.util.Map;

class DNSRecord {
    private String domain;
    private String ipAddress;

    public DNSRecord(String domain, String ipAddress) {
        this.domain = domain;
        this.ipAddress = ipAddress;
    }

    public String getDomain() {
        return domain;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "Domain: " + domain + ", IP Address: " + ipAddress;
    }
}

class DNSServer {
    private String serverName;
    private Map<String, DNSRecord> dnsRecords;
    private DNSServer nextServer; // Next server in the hierarchy for recursive resolution

    public DNSServer(String serverName) {
        this.serverName = serverName;
        this.dnsRecords = new HashMap<>();
        this.nextServer = null;
    }

    public void addRecord(String domain, String ipAddress) {
        DNSRecord record = new DNSRecord(domain, ipAddress);
        dnsRecords.put(domain, record);
    }

    public void setNextServer(DNSServer nextServer) {
        this.nextServer = nextServer;
    }

    public DNSRecord iterativeQuery(String domain) {
        if (dnsRecords.containsKey(domain)) {
            System.out.println("[" + serverName + "] Found record for " + domain);
            return dnsRecords.get(domain);
        } else if (nextServer != null) {
            System.out.println("[" + serverName + "] Forwarding query to " + nextServer.serverName);
            return nextServer.iterativeQuery(domain);
        } else {
            System.out.println("[" + serverName + "] Record for " + domain + " not found.");
            return null;
        }
    }

    public DNSRecord recursiveQuery(String domain) {
        if (dnsRecords.containsKey(domain)) {
            System.out.println("[" + serverName + "] Found record for " + domain);
            return dnsRecords.get(domain);
        } else if (nextServer != null) {
            System.out.println("[" + serverName + "] Forwarding recursive query to " + nextServer.serverName);
            return nextServer.recursiveQuery(domain);
        } else {
            System.out.println("[" + serverName + "] Record for " + domain + " not found.");
            return null;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Create DNS Servers
        DNSServer rootServer = new DNSServer("Root DNS Server");
        DNSServer tldServer = new DNSServer("TLD DNS Server");
        DNSServer ispServer = new DNSServer("ISP DNS Server");

        // Connect servers in hierarchy
        rootServer.setNextServer(tldServer);
        tldServer.setNextServer(ispServer);

        // Add records to the servers
        rootServer.addRecord("com", "192.0.2.1");
        tldServer.addRecord("example.com", "192.0.2.2");
        ispServer.addRecord("www.example.com", "192.0.2.3");

        // Test Iterative Query
        System.out.println("\n** Iterative Query for 'www.example.com' **");
        DNSRecord result = rootServer.iterativeQuery("www.example.com");
        if (result != null) {
            System.out.println("Final Result: " + result);
        }

        // Test Recursive Query
        System.out.println("\n** Recursive Query for 'www.example.com' **");
        DNSRecord recursiveResult = rootServer.recursiveQuery("www.example.com");
        if (recursiveResult != null) {
            System.out.println("Final Result: " + recursiveResult);
        }
    }
}
