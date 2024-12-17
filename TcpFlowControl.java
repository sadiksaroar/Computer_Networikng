// package tcpflowcontrol;

import java.util.Random;
import java.util.Scanner;

public class TcpFlowControl {

    // Function to generate a random number of frames to send (within window size)
    public static int generateFrame(int winSize) {
        Random random = new Random();
        int noOfGeneratedFrame = random.nextInt(winSize) + 1; // At least 1 frame sent
        return noOfGeneratedFrame;
    }

    // Function to generate a random number of acknowledgments (within the number of sent frames)
    public static int generateAck(int noOfSent) {
        Random random = new Random();
        int noOfAckFrame = random.nextInt(noOfSent) + 1; // At least 1 frame acknowledged
        return noOfAckFrame;
    }

    public static void main(String[] args) {
        int noOfFrame, winSize, startFrame = 0, endFrame = 0, noOfAck = 0, noOfSent = 0;

        // Input scanner
        Scanner scn = new Scanner(System.in);

        // Input the total number of frames
        System.out.println("Enter the total number of frames to be sent: ");
        noOfFrame = scn.nextInt();

        // Input the window size
        System.out.println("Enter the window size: ");
        winSize = scn.nextInt();

        int dueFrame = noOfFrame; // Remaining frames to be sent

        // Loop until all frames are sent and acknowledged
        while (dueFrame > 0) {
            // Generate the number of frames to send (up to the window size)
            noOfSent = Math.min(generateFrame(winSize), dueFrame);

            // Update the ending frame index based on the number of sent frames
            endFrame = startFrame + noOfSent;

            // Send frames
            for (int i = startFrame + 1; i <= endFrame; i++) {
                System.out.println("Sending frame " + i);
            }

            // Generate the number of acknowledged frames
            noOfAck = Math.min(generateAck(noOfSent), dueFrame);

            // Update startFrame based on the number of acknowledged frames
            startFrame += noOfAck;

            // Print acknowledgment details
            System.out.println("Acknowledgment received for frames up to " + startFrame);

            // Update the number of remaining frames
            dueFrame -= noOfAck;

            System.out.println("\nFrames remaining: " + dueFrame + "\n");

            // Reset endFrame for the next window
            endFrame = startFrame;
        }

        System.out.println("The Sliding Window Protocol concludes here.");
        scn.close(); // Close scanner
    }
}
