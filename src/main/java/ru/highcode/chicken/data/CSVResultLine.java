package ru.highcode.chicken.data;

import com.opencsv.bean.CsvBindByPosition;

public class CSVResultLine {
    @CsvBindByPosition(position = 0, required = true)
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
