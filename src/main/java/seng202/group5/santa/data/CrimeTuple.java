package seng202.group5.santa.data;

/**
 * Custom tuple that contains a set amount of information about different crime types
 * Created by Group5
 */
public class CrimeTuple {
    private String name;
    private int count;
    private double frequency;
    private double pcArrests = 0.00;

    public CrimeTuple(String name, int count, double frequency, double pcArrests)
    {
        this.name = name;
        this.count = count;
        this.frequency = frequency;
        this.pcArrests = pcArrests;
    }

    public String getName()
    {
        return name;
    }

    public int getCount()
    {
        return count;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public double getPcArrests()
    {
        return pcArrests;
    }
}
