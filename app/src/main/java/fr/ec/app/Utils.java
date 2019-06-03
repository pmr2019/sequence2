package fr.ec.app;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Utils {

  public static Executor BACKGROUND = Executors.newFixedThreadPool(2);
}
