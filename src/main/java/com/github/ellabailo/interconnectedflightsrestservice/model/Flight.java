package com.github.ellabailo.interconnectedflightsrestservice.model;

public class Flight {
        private int number;

        private String departureTime;

        private String arrivalTime;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTimer(String departureTime) {
            this.departureTime = departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

}
