package com.asb.spandan2014.utils;

import java.util.List;
import com.asb.spandan2014.db.eo.GameRulesEO;


/**
 * UserState used across the app
 * 
 * @author arjun
 * 
 */
public class UserState {
  private static UserState instance;

  private GameRulesEO gameRulesEO;

  private int gameId;

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public GameRulesEO getGameRulesEO() {
    return gameRulesEO;
  }

  public void setGameRulesEO(GameRulesEO gameRulesEO) {
    this.gameRulesEO = gameRulesEO;
  }

  public static synchronized UserState getInstance() {
    if (instance == null) {
      instance = new UserState();
    }
    return instance;
  }

}
