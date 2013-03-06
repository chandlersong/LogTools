
package org.logtools.core.logprocess.plugin.averageexecutetimeplugin;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class SummaryResult {

    private DescriptiveStatistics statistics = new DescriptiveStatistics();

    private String startLog;

    private String endLog;

    private Integer missingStart = 0;

    private Integer missingEnd = 0;

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
        return startLog + "-" + endLog;
    }

    public void missingStartIncrese() {
        missingStart++;
    }

    public void missingEndIncrese() {
        missingEnd++;
    }

    public String getStartLog() {
        return startLog;
    }

    public void setStartLog(String startLog) {
        this.startLog = startLog;
    }

    public String getEndLog() {
        return endLog;
    }

    public void setEndLog(String endLog) {
        this.endLog = endLog;
    }

    public Integer getMissingStart() {
        return missingStart;
    }

    public void setMissingStart(Integer missingStart) {
        this.missingStart = missingStart;
    }

    public Integer getMissingEnd() {
        return missingEnd;
    }

    public void setMissingEnd(Integer missingEnd) {
        this.missingEnd = missingEnd;
    }

}
