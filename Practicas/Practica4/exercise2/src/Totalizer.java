import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.imtp.leap.JICP.JICPAddress;

import java.io.*;

import static java.lang.System.exit;

public class Totalizer extends Agent {

    private boolean finished;
    private int total = 0;
    private String filename;

    @Override
    protected void setup() {

        Object[] args = getArguments();
        if (args != null) {

            if (args.length < 1) {
                System.out.println("\n[ERROR] param: filename when contain numbers\n");
                exit(1);
            }

            this.filename = (String) args[0];
            System.out.println("\n\nFilename: " + filename);


            try {
                System.out.println("\nWaiting... for an another container \n\n");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                exit(0);
            }

            finished = false;

            System.out.println("Migration to Container-1");
            ContainerID destination = new ContainerID("Container-1", null);
            doMove(destination);

        } else {
            System.out.println("\n[ERROR] param: filename when contain numbers\n");
            exit(1);
        }
    }

    @Override
    protected void afterMove() {
        System.out.println(Utils.nullSafeConcat("\nHere is ", here()));

        if (!finished) {
            try {
                readAndSum();
                doMove(new ContainerID("Main-Container", null));
                finished = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Utils.nullSafeConcat("\nTotal sum = ", total, "\n"));
            exit(0);
        }

    }

    private void readAndSum() {
        System.out.println(Utils.nullSafeConcat("\n[TOTALIZE] Filename: ",  filename));

        try(BufferedReader file = new BufferedReader(new FileReader(filename))) {
            String line;
            line = file.readLine();
            while (line != null) {
                try {
                    total += Integer.parseInt(line);
                    System.out.println(Utils.nullSafeConcat("[TOTALIZE] total: <", line, ">"));
                } catch (NumberFormatException e) {
                    System.out.println(Utils.nullSafeConcat("[TOTALIZE] this line isn't a total: <", line, ">"));

                } finally {
                    line = file.readLine();
                }
            }
            System.out.println(Utils.nullSafeConcat("[TOTALIZE] End\n"));
        } catch (Exception e) {
            e.printStackTrace();
            exit(0);

        }
    }
}

