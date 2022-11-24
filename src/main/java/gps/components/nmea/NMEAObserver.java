package gps.components.nmea;

import gps.components.view.PositionUpdateListener;

import java.util.ArrayList;

public class NMEAObserver
{
    private final ArrayList<PositionUpdateListener> observers;

    public NMEAObserver()
    {
        observers = new ArrayList<>();
    }

    public void addObserver(final PositionUpdateListener observer)
    {
        observers.add(observer);
    }
    //Removes an observer

    public boolean removeObserver(final PositionUpdateListener observer)
    {
        return observers.remove(observer);
    }


    public void updateObservers(NMEAInfo info)
    {
        for(PositionUpdateListener observer: observers){
            observer.update(info);
        }
    }

}
