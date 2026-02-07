package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;

public class SleepingSession {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final SleepQuality quality;
    private final Chronotype chronotype;

    public SleepingSession(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        this.start = start;
        this.end = end;
        this.quality = quality;
        chronotype = defineChronotype();
    }

    private Chronotype defineChronotype() {
        if (isNightSleep()) {
            int startHour = start.getHour();
            int endHour = end.getHour();
            if (((startHour >= 0 && startHour < 6) || startHour >= 23) && (endHour >= 9)) {
                return Chronotype.OWL;
            } else if ((startHour >= 18 && startHour < 22) && endHour < 7) {
                return Chronotype.LARK;
            } else {
                return Chronotype.PIGEON;
            }
        }
        return null;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public Chronotype getChronotype() {
        return chronotype;
    }

    public boolean isNightSleep() {
        LocalDateTime edgeLeft = end.toLocalDate().atTime(0, 0);
        LocalDateTime edgeRight = end.toLocalDate().atTime(6, 0);
        if ((start.isBefore(edgeLeft) && end.isAfter(edgeRight)) || (start.isAfter(edgeLeft) && end.isBefore(edgeRight))) {
            return true;
        } else if (start.isBefore(edgeLeft) && (end.isAfter(edgeLeft) && end.isBefore(edgeRight))) {
            return true;
        } else return (start.isAfter(edgeLeft) && start.isBefore(edgeRight)) && end.isAfter(edgeRight);
    }

    @Override
    public String toString() {
        return start.getHour() + ":" + start.getMinute() + " -> "
                + end.getHour() + ":" + end.getMinute() + "  " + chronotype;
    }
}
