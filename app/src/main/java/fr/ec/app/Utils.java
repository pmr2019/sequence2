package fr.ec.app;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {

  public static ExecutorService BACKGROUND = Executors.newFixedThreadPool(2);
}
