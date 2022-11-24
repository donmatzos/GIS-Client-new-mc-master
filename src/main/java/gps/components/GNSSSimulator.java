package gps.components;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GNSSSimulator extends BufferedReader
{
    @Getter
    private final String filename;
    @Getter
    private final int sleep;
    @Getter
    private final String filter;

    public GNSSSimulator(final String filename, final int sleep, final String filter) throws FileNotFoundException
    {
        super(new FileReader(filename));
        this.filename = filename;
        this.sleep = sleep;
        this.filter = filter;
    }

    @Override
    public String readLine() throws IOException
    {

        String currentLine =  super.readLine();
        if (currentLine != null && currentLine.contains(filter))
        {
            try
            {
                Thread.sleep(sleep);
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return currentLine;
    }
}
