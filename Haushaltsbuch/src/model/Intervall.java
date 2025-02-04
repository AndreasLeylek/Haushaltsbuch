package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public enum Intervall {

    WÖCHENTLICH(0),
    ZWEIWÖCHENTLICH(0),
    MONATLICH(1),
    VIERTELJAEHRLICH(3),
    HALBJAEHRLICH(6),
    JAEHRLICH(12);

    private final int monate;

    Intervall(int monate) {
        this.monate = monate;
    }

    public int getDays() {
        return monate;
    }

    public LocalDate addTo(LocalDate date) {
        switch (this) {
            case WÖCHENTLICH:
                return date.plusWeeks(1);
            case ZWEIWÖCHENTLICH:
                return date.plusWeeks(2);
            case MONATLICH:
                return date.plusMonths(1);
            case VIERTELJAEHRLICH:
                return date.plusMonths(3);
            case HALBJAEHRLICH:
                return date.plusMonths(6);
            case JAEHRLICH:
                return date.plusYears(1);
            default:
                throw new IllegalStateException("Ungültiger Intervall Wert");
        }
    }
}
