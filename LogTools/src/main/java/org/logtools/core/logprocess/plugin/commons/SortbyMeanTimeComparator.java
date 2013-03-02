package org.logtools.core.logprocess.plugin.commons;

import java.util.Comparator;

public class SortbyMeanTimeComparator implements Comparator<SummaryResult> {

    public int compare(SummaryResult o1, SummaryResult o2) {
        Double value1 = o1.getStatistics().getMean();
        Double value2 = o2.getStatistics().getMean();
        Double delta = value2 - value1;

        int result = 0;
        if (delta >= 0) {
            result = (int) Math.ceil(delta);
        } else {
            result = (int) Math.floor(delta);
        }

        return result;
    }
}
