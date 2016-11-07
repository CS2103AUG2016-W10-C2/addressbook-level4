# A0116603Rreused
###### /java/seedu/priorityq/ui/util/GuiUtil.java
``` java

    /**
     * Returns a change listener that posts a WindowResizeEvent after the user
     * has finished resizing the application window.
     */
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

```
