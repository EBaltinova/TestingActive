package ru.stqa.pft.sandbox;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DistanceTestSec {
    @Test
    public void testDistance() {

        Point p3 = new Point(7,6);
        Point p4 = new Point (2,9);
        Assert.assertEquals(p3.distance(p4),5.830951894845301);

    }
}