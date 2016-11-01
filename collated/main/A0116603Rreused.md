# A0116603Rreused
###### /java/seedu/address/ui/util/GuiUtil.java
``` java
    public static ChangeListener<Number> getWindowResizeEventListener() {
        return new ChangeListener<Number>() {
            final Timer timer = new Timer();
            TimerTask task = null;
            final long delayTime = 200;

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {
                if (task != null) task.cancel();
                task = new TimerTask() {
                    @Override
                    public void run() {
                        EventsCenter.getInstance().post(new WindowResizeEvent(newValue));
                    }
                };
                timer.schedule(task, delayTime);
            }
        };
    }

    /**
     * Get the style class for floating task elements such as id, title and
     * description.
     * @param isMarked true, if the current task is marked as done
     * @return a string, the style class for the floating task
     */
    public static  String getTaskStyling(boolean isMarked) {
        return getDeadlineStyling(isMarked, null);
    }

    /**
     * Get the style class for deadlines. The style differs depending on whether the
     * task is marked as done. If it is not done, it differs depending on whether it
     * is overdue. If it is not overdue, it differs depending on whether the deadline
     * is due by the end of the current day.
     * @param isMarked true, if the current task is marked as done
     * @param deadline a datetime which will be compared to the current time to
     *                 determine the style class
     * @return a string, the style class for the deadline
     */
    public static String getDeadlineStyling(boolean isMarked, LocalDateTime deadline) {
        if (isMarked) {
            return PAST_STYLE_CLASS;
        }

        if (deadline == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);

        if (deadline.isBefore(now)) {
            return OVERDUE_STYLE_CLASS;
        }

        if (deadline.isAfter(now) && deadline.isBefore(midnight)) {
            return ACTIVE_STYLE_CLASS;
        }
        return "";
    }

    /**
     * Get the style class for events. The style differs depending on whether the
     * event is over. If it is not over, but currently ongoing, a different style
     * class is also applied.
     * @param startTime a datetime, the start of the event
     * @param endTime a datetime, the end of the event
     * @return a string, the style class for the event
     */
    public static String getEventStyling(LocalDateTime startTime, LocalDateTime endTime) {
        assert (startTime != null && endTime != null);
        LocalDateTime now = LocalDateTime.now();
        if (endTime.isBefore(now)) {
            return PAST_STYLE_CLASS;
        } else if (now.isAfter(startTime) && now.isBefore(endTime)) {
            return ACTIVE_STYLE_CLASS;
        }
        return "";
    }
}
```
