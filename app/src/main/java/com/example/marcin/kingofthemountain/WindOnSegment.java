package com.example.marcin.kingofthemountain;

import android.util.Log;

import com.example.marcin.kingofthemountain.OpenWeatherMapAPI.Wind;
import com.example.marcin.kingofthemountain.StravaAPI.Segment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.Serializable;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by Marcin on 09-Apr-18.
 */

public class WindOnSegment implements Serializable{

    private Segment currentSegment;
    private Wind currentWind;
    double percentageHeadWind;
    double percentageTailWind;
    double percentageLeftWind;
    double percentageRightWind;

    final static int TAIL = 0;
    final static int RIGHT = 90;
    final static int HEAD = 180;
    final static int LEFT = 270;

    public WindOnSegment(Segment currentSegment, Wind currentWind) {
        this.currentSegment = currentSegment;
        this.currentWind = currentWind;
    }

    /***
     *
     * angleBetweenPoints - windAngle
     * 0 : wiatr przeciwny
     * 180 : wiatr zgodny
     * 90 : wiatr z prawej
     * 270 : wiatr z lewej
     */
    public void computeWindOnSegment(){
        List<LatLng> segmentRoute = currentSegment.getPointsDecoded();
        double totalTailWind = 0;
        double totalRightWind = 0;
        double totalHeadWind = 0;
        double totalLeftWind = 0;
        double totalLength = 0;

        int numberOfShortSectors = segmentRoute.size()-1;

        for (int i=0; i<numberOfShortSectors;i++){
            double sectorDirection = convertAngleTo360(SphericalUtil.computeHeading(segmentRoute.get(i+1), segmentRoute.get(i)));
            double sectorLength = SphericalUtil.computeDistanceBetween(segmentRoute.get(i+1), segmentRoute.get(i));
            double windOnShortSector = computeWindOnShortSector(currentWind.getDeg().doubleValue(), sectorDirection);

            if(windOnShortSector>=TAIL && windOnShortSector < RIGHT){
                totalTailWind += compatibilityWithWindDirection(windOnShortSector, TAIL, sectorLength);
                totalRightWind += compatibilityWithWindDirection(windOnShortSector, RIGHT, sectorLength);
            }
            else if(windOnShortSector>=RIGHT && windOnShortSector < HEAD){
                totalRightWind += compatibilityWithWindDirection(windOnShortSector, RIGHT, sectorLength);
                totalHeadWind += compatibilityWithWindDirection(windOnShortSector, HEAD, sectorLength);

            }
            else if(windOnShortSector>=HEAD && windOnShortSector < LEFT){
                totalHeadWind += compatibilityWithWindDirection(windOnShortSector, HEAD, sectorLength);
                totalLeftWind += compatibilityWithWindDirection(windOnShortSector, LEFT, sectorLength);
            }
            else if(windOnShortSector>=LEFT && windOnShortSector < 360){
                totalLeftWind += compatibilityWithWindDirection(windOnShortSector, LEFT, sectorLength);
                totalTailWind += compatibilityWithWindDirection(windOnShortSector, 360, sectorLength);

            }

            totalLength += sectorLength;
            Log.d("numberOfSector: ", String.valueOf(i));
            Log.d("Total Head Wind: ", String.valueOf(totalHeadWind));
            Log.d("Total Tail Wind: ", String.valueOf(totalTailWind));
            Log.d("Total Left Wind: ", String.valueOf(totalLeftWind));
            Log.d("Total Right Wind: ", String.valueOf(totalRightWind));
        }

        percentageHeadWind = totalHeadWind / totalLength * 100;
        percentageTailWind = totalTailWind / totalLength * 100;
        percentageLeftWind = totalLeftWind / totalLength * 100;
        percentageRightWind = totalRightWind / totalLength * 100;

    }

    private double computeWindOnShortSector(double windDirection, double sectorDirection){
        double windOnShortSector = sectorDirection - windDirection;

        if (windOnShortSector < 0)
            windOnShortSector += 360;

        return windOnShortSector;
    }

    /***
     * @return values from 0 to 1
     */
    private double compatibilityWithWindDirection(double computedWindPerSector, Integer referenceWind, double sectorLength){
        return (90 - abs(referenceWind - computedWindPerSector)) / 90 * sectorLength;
    }

    private double convertAngleTo360 (double angle180){
        double angle360 = angle180;
        if (angle180<0){
            angle360 = 360 + angle180;
        }
        return angle360;
    }

    public Segment getCurrentSegment() {
        return currentSegment;
    }

    public void setCurrentSegment(Segment currentSegment) {
        this.currentSegment = currentSegment;
    }

    public Wind getCurrentWind() {
        return currentWind;
    }

    public void setCurrentWind(Wind currentWind) {
        this.currentWind = currentWind;
    }

    public double getPercentageHeadWind() {
        return percentageHeadWind;
    }

    public double getPercentageTailWind() {
        return percentageTailWind;
    }

    public double getPercentageLeftWind() {
        return percentageLeftWind;
    }

    public double getPercentageRightWind() {
        return percentageRightWind;
    }
}
