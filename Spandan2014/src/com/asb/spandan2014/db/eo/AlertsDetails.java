package com.asb.spandan2014.db.eo;

public class AlertsDetails {

  private String alertTime;
  private String alertFrom;
  private String alertMessage;

  public AlertsDetails(String alertTime, String alertFrom, String alertMessage) {
    super();
    this.alertTime = alertTime;
    this.alertFrom = alertFrom;
    this.alertMessage = alertMessage;
  }

  public String getAlertTime() {
    return alertTime;
  }

  public void setAlertTime(String alertTime) {
    this.alertTime = alertTime;
  }

  public String getAlertFrom() {
    return alertFrom;
  }

  public void setAlertFrom(String alertFrom) {
    this.alertFrom = alertFrom;
  }

  public String getAlertMessage() {
    return alertMessage;
  }

  public void setAlertMessage(String alertMessage) {
    this.alertMessage = alertMessage;
  }

}
