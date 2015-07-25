package com.miwashi.model;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miwa01 on 15-07-24.
 */
public class Stats {

    private static final int DAYS_IN_WEEK = 7;
    private static final int DAYS_IN_MONTH = 32; //Add one as dates don't start on 0.
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;

    private Stat total = new Stat();
    private Stat[] minutely = new Stat[MINUTES_IN_HOUR];
    private Stat[] hourly = new Stat[HOURS_IN_DAY];
    private Stat[] daily = new Stat[DAYS_IN_MONTH];

    public class Stat{
        private String caption = "";
        private long starts =0;
        private long complete = 0;
        private long fails = 0;

        public Stat(){
            super();
        }

        public Stat(long starts, long complete, long fails){
            super();
            this.starts = starts;
            this.complete = complete;
            this.fails = fails;
        }

        public long getStarts() {
            return starts;
        }

        public void setStarts(long starts) {
            this.starts = starts;
        }

        public long getComplete() {
            return complete;
        }

        public void setComplete(long complete) {
            this.complete = complete;
        }

        public long getFails() {
            return fails;
        }

        public void setFails(long fails) {
            this.fails = fails;
        }

        public String getFailShare(){
            String result = "" + fails +"%";

            if(fails!=0){
                result = fails + " (" + Math.round((fails*100) / complete) + "%)";
            }

            return result;
        }

        public String getCompleteWithMissing(){
            String result = "" + complete +"%";

            if(fails!=0){
                result = complete + " (" + (starts - complete) + ")";
            }

            return result;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }
    }

    public Stats(){
        super();
        for(int i=0; i < DAYS_IN_MONTH; i++){
            daily[i]= new Stat();
        }

        for(int i=0; i < HOURS_IN_DAY; i++){
            hourly[i]= new Stat();
        }

        for(int i=0; i < MINUTES_IN_HOUR; i++){
            minutely[i]= new Stat();
        }
    }

    public void inc(ResultReport report){
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        int dayOfMonth = now.getDayOfMonth();
        int minuteOfHour = now.getMinuteOfHour();
        int nextMinuteOfHour = now.plusMinutes(1).getMinuteOfHour();


        if (report.isCompleted()) {
            total.complete++;
            hourly[hourOfDay].complete++;
            daily[dayOfMonth].complete++;
            minutely[minuteOfHour].complete++;
            minutely[nextMinuteOfHour].complete=0;
        }else{
            total.starts++;
            hourly[hourOfDay].starts++;
            daily[dayOfMonth].starts++;
            minutely[minuteOfHour].starts++;
            minutely[nextMinuteOfHour].starts=0;
        }

        if (report.isFailed()) {
            total.fails++;
            hourly[hourOfDay].fails++;
            daily[dayOfMonth].fails++;
            minutely[minuteOfHour].fails++;
            minutely[nextMinuteOfHour].fails=0;
        }
    }

    public Stat getTotal(){
        return  total;
    }

    public Stat getLast24Hours(){
        long total = 0;
        long complete = 0;
        long fails = 0;

        for(int i=0; i < hourly.length; i++){
            total+= hourly[i].getStarts();
            complete+= hourly[i].getComplete();
            fails+= hourly[i].getFails();
        }
        return  new Stat(total, complete, fails);
    }

    public Stat getLastHour(){
        long total = 0;
        long complete = 0;
        long fails = 0;

        for(int i=0; i < minutely.length; i++){
            total+= minutely[i].getStarts();
            complete+= minutely[i].getComplete();
            fails+= minutely[i].getFails();
        }
        return  new Stat(total, complete, fails);
    }

    public Stat getLastWeek(){
        long total = 0;
        long complete = 0;
        long fails = 0;

        DateTime now = DateTime.now();
        total= daily[now.getDayOfMonth()].getStarts();
        complete= daily[now.getDayOfMonth()].getComplete();
        fails= daily[now.getDayOfMonth()].getFails();
        for(int i=1; i < 7; i++){
            total+= daily[now.minusDays(i).getDayOfMonth()].getStarts();
            complete+= daily[now.minusDays(i).getDayOfMonth()].getComplete();
            fails+= daily[now.minusDays(i).getDayOfMonth()].getFails();
        }
        return  new Stat(total, complete, fails);
    }

    public Stat getToday(){
        long total = 0;
        long complete = 0;
        long fails = 0;

        DateTime now = DateTime.now();
        total= hourly[now.getHourOfDay()].getStarts();
        complete= hourly[now.getHourOfDay()].getComplete();
        fails= hourly[now.getHourOfDay()].getFails();
        int thisDay = now.getDayOfMonth();
        int indX = 1;
        while(thisDay == now.minusHours(indX).getDayOfMonth()){
            total+= hourly[now.minusHours(indX).getHourOfDay()].getStarts();
            complete+= hourly[now.minusHours(indX).getHourOfDay()].getComplete();
            fails+= hourly[now.minusHours(indX).getHourOfDay()].getFails();
            indX++;
        }
        return  new Stat(total, complete, fails);
    }

    public Collection<Stat> getHourly(){
        Collection<Stat> result = new ArrayList<Stat>();
        DateTime now = DateTime.now();
        for(int i =0; i < 24; i++){
            DateTime aHour = now.minusHours(i);
            hourly[aHour.getHourOfDay()].setCaption(aHour.toString("HH:00") + "-" +aHour.plusHours(1).toString("HH:00"));
            result.add(hourly[aHour.getHourOfDay()]);
        }
        return result;
    }

    public Collection<Stat> getDaily(){
        Collection<Stat> result = new ArrayList<Stat>();
        DateTime now = DateTime.now();
        for(int i =0; i < 7; i++){
            DateTime aDay = now.minusDays(i);
            daily[aDay.getDayOfMonth()].setCaption(aDay.toString("yyyy-MM-dd"));
            result.add(daily[aDay.getDayOfMonth()]);
        }
        return result;
    }

    public void resetHour(){
        DateTime now = DateTime.now();
        int hourOfDay = now.getHourOfDay();
        hourly[hourOfDay].starts=0;
        hourly[hourOfDay].complete=0;
        hourly[hourOfDay].fails=0;
    }

    public void resetDay(){
        DateTime now = DateTime.now();
        int dayOfMonth = now.getDayOfMonth();
        daily[dayOfMonth].starts=0;
        daily[dayOfMonth].complete=0;
        daily[dayOfMonth].fails=0;
    }
}
