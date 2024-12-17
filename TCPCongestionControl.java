// package tcpcongestioncontrol;

import java.util.*;

public class TCPCongestionControl {

    private int cwnd;          // Congestion window size
    private int ssthresh;      // Slow start threshold
    private int rtt;           // Round-trip time counter
    private boolean congestion;

    public TCPCongestionControl(int init_ssthresh) {
        cwnd = 1;              // Initial congestion window size
        ssthresh = init_ssthresh; 
        congestion = false;    // Initially, no congestion
        rtt = 0;               // Round-trip time starts at 0
    }

    public void run() {
        System.out.println("Connected to the Server... ...");
        System.out.println("Enter the length of your data: ");
        Scanner scan = new Scanner(System.in);
        int len = scan.nextInt(); // Total data length
        int dataSeqNum = 0;

        System.out.println("Your data is starting to be sent ... ");

        while (dataSeqNum < len) {
            this.rtt++;
            System.out.println("\nRTT Number: " + this.rtt);
            System.out.println("--------------------------------");
            System.out.println("Previous cwnd size: " + cwnd);
            System.out.println("Current ssthresh value: " + ssthresh);

            if (!congestion) {
                if (cwnd < ssthresh) {
                    // Slow Start Phase
                    cwnd *= 2; // Exponential growth
                    System.out.println("...Slow Start phase running...");
                } else {
                    // Congestion Avoidance Phase
                    cwnd += 1; // Linear growth
                    System.out.println("...Congestion Avoidance phase running...");
                }
            }

            System.out.println("Updated cwnd size: " + cwnd);
            sendPacket(dataSeqNum);

            dataSeqNum += cwnd;
        }

        System.out.println("\nYour data transmission is completed. No more data to send.");
        System.out.println("Congestion Control mechanism concludes.\nIt took " + this.rtt 
                           + " transmission rounds to send the entire data.");
    }

    public void sendPacket(int dataSeqNum) {
        System.out.println("Data from " + (dataSeqNum + 1) + " - " + (dataSeqNum + cwnd) + " is being sent...\n");

        if (!receiveAcknowledgment()) {
            congestion = true;
            System.out.println("... but wait! Congestion has been detected!");

            if (timeout()) {
                handleTimeoutCongestion();
            } else {
                handleTripleDupAckCongestion();
            }
        } else {
            congestion = false;
        }
    }

    public boolean receiveAcknowledgment() {
        Random ack = new Random();
        return ack.nextBoolean(); // Randomly decide if congestion occurs
    }

    public boolean timeout() {
        Random rttRandom = new Random();
        return rttRandom.nextBoolean(); // Randomly decide between timeout or Triple Dup Ack
    }

    public void handleTimeoutCongestion() {
        // Severe congestion; reset cwnd and adjust ssthresh
        System.out.println("\nTimeout occurred. Handling timeout-based congestion: cwnd will reset to 1.");
        ssthresh = Math.max(cwnd / 2, 1); // Halve the cwnd, but minimum is 1
        cwnd = 1; // Reset to 1 (TCP Tahoe characteristic)
        retransmitPacket();
    }

    public void handleTripleDupAckCongestion() {
        // Severe congestion; reset cwnd and adjust ssthresh (same as timeout for TCP Tahoe)
        System.out.println("\nTriple Duplicate Ack detected. Handling congestion: cwnd will reset to 1.");
        ssthresh = Math.max(cwnd / 2, 1); // Halve the cwnd, but minimum is 1
        cwnd = 1; // Reset to 1 (TCP Tahoe characteristic)
        retransmitPacket();
    }

    public void retransmitPacket() {
        congestion = false;
        System.out.println("Retransmitting the lost packet after handling congestion...\n");
    }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Please input the initial ssthresh value: ");
        int ssthresh = scn.nextInt();

        TCPCongestionControl tahoe = new TCPCongestionControl(ssthresh);
        tahoe.run();
    }
}
