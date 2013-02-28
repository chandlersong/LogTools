package org.logtools.core.logprocess.plugin.commons;

import java.util.Comparator;

public class SortbyFrequencyComparator implements Comparator<SummaryResult> {

    public int compare(SummaryResult o1, SummaryResult o2) {
        Long value1 = o1.getStatistics().getN();
        Long value2 = o2.getStatistics().getN();
        return value2.intValue() - value1.intValue();
    }

}
