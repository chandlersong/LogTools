package org.logtools.core.logprocess.plugin.exxecutetimefilterobjects;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class SummaryResult {

    private DescriptiveStatistics statistics = new DescriptiveStatistics();

    private String message;

    public DescriptiveStatistics getStatistics() {
        return statistics;
    }

    public void addValue(double v) {
        statistics.addValue(v);
    }

    public void setStatistics(DescriptiveStatistics statistics) {
        this.statistics = statistics;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
